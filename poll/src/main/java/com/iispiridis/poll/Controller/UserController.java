package com.iispiridis.poll.Controller;

import com.iispiridis.poll.Exception.ResourceNotFoundException;
import com.iispiridis.poll.Models.ContactInformation;
import com.iispiridis.poll.Models.User;
import com.iispiridis.poll.Payload.*;
import com.iispiridis.poll.Repositories.PollRepository;
import com.iispiridis.poll.Repositories.UserRepository;
import com.iispiridis.poll.Repositories.VoteRepository;
import com.iispiridis.poll.Security.CurrentUser;
import com.iispiridis.poll.Security.UserPrincipal;
import com.iispiridis.poll.Service.PollService;
import com.iispiridis.poll.Util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PollService pollService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        userSummary.setContactInformation(userRepository.getUserContactInformation(userSummary.getId()));
        return userSummary;
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        long pollCount = pollRepository.countByCreatedBy(user.getId());
        long voteCount = voteRepository.countByUserId(user.getId());

        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), pollCount, voteCount, user.getContactInformation());

        return userProfile;
    }

    @GetMapping("/users/{username}/polls")
    public PagedResponse<PollResponse> getPollsCreatedBy(@PathVariable(value = "username") String username,
                                                         @CurrentUser UserPrincipal currentUser,
                                                         @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return pollService.getPollsCreatedBy(username, currentUser, page, size);
    }


    @GetMapping("/users/{username}/votes")
    public PagedResponse<PollResponse> getPollsVotedBy(@PathVariable(value = "username") String username,
                                                       @CurrentUser UserPrincipal currentUser,
                                                       @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                       @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return pollService.getPollsVotedBy(username, currentUser, page, size);
    }

    @PostMapping("/users/{username}/contacts")
    public ResponseEntity<?> updateUserContactInformation(@PathVariable(value="username") String username, @RequestBody ContactInformationPayload contactpayload)
    {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty())
        {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        } else
        {
            User u = user.get();
            ContactInformation ci;

            if (u.getContactInformation() != null )  {
                ci = u.getContactInformation();
                ci.setId(u.getContactInformation().getId());
                ci.setAddress(contactpayload.getAddress());
                ci.setPhone(contactpayload.getPhone());
            }
            else
            {
                ci = new ContactInformation();
                ci.setAddress(contactpayload.getAddress());
                ci.setPhone(contactpayload.getPhone());
            }

            System.out.println(contactpayload.getAddress() + "\t" + contactpayload.getPhone() + "\t" + contactpayload.getId());
            System.out.println(ci.getAddress() + "\t" + ci.getPhone() + "\t" + ci.getId());

            u.setContactInformation(ci);
            userRepository.save(u);
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}/contacts")
                .buildAndExpand(username).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Contact information updated"));
    }

}
