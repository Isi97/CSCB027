package com.iispiridis.poll.Controller;

import com.iispiridis.poll.Exception.ResourceNotFoundException;
import com.iispiridis.poll.Models.ContactInformation;
import com.iispiridis.poll.Models.Role;
import com.iispiridis.poll.Models.RoleName;
import com.iispiridis.poll.Models.User;
import com.iispiridis.poll.Payload.*;
import com.iispiridis.poll.Repositories.RoleRepository;
import com.iispiridis.poll.Repositories.UserRepository;
import com.iispiridis.poll.Security.CurrentUser;
import com.iispiridis.poll.Security.UserPrincipal;
import com.iispiridis.poll.Service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER') || hasRole('MODERATOR') ")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        userSummary.setContactInformation(userRepository.getUserContactInformation(userSummary.getId()));

        Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();
        List<String> authorityList = new ArrayList<>();

        for ( GrantedAuthority g : authorities)
        {
            authorityList.add(g.getAuthority());
        }

        userSummary.setAuthorities(authorityList);

        return userSummary;
    }

    @GetMapping("/user/{uid}/activities")
    public UserAdminViewResponse getUserActivities(@PathVariable(value="uid") Long id)
    {
        return adminService.getUserAdminView(id);
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


        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), user.getContactInformation());

        return userProfile;
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

    @GetMapping("users/all")
    @PreAuthorize("hasRole('MODERATOR') || hasRole('ADMIN')")
    public List<UserSummary> getAllUsers()
    {
        List<User> userList = userRepository.findAll();
        List<UserSummary> userSummaryList = new ArrayList<>();

        for ( User currentUser : userList)
        {
            UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
            userSummary.setEmail(currentUser.getEmail());

            List<String> authorities = new ArrayList<>();

            for ( Role r : currentUser.getRoles() )
            {
                authorities.add(r.getName().toString());
                System.out.println(r.getName().toString());
            }
            userSummary.setAuthorities(authorities);

            userSummaryList.add(userSummary);
        }

        return userSummaryList;
    }

    @PostMapping("/user/{id}/promote")
    public ResponseEntity<?> promoteUser(@PathVariable(value = "id") Long id)
    {
        User u = userRepository.getOne(id);
        Set<Role> roleSet = u.getRoles();
        roleSet.add(roleRepository.findByName(RoleName.ROLE_MODERATOR).get());

        userRepository.save(u);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("")
                .buildAndExpand().toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User Promoted"));
    }

    @PostMapping("/user/{id}/ban")
    public ResponseEntity<?> banUser(@PathVariable(value = "id") Long id)
    {
        User u = userRepository.getOne(id);
        Set<Role> roleSet = u.getRoles();
        roleSet.add(roleRepository.findByName(RoleName.ROLE_BANNED).get());

        userRepository.save(u);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("")
                .buildAndExpand().toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User Banned"));
    }

    @PostMapping("/user/{id}/unban")
    public ResponseEntity<?> unBanUser(@PathVariable(value = "id") Long id)
    {
        User u = userRepository.getOne(id);
        Set<Role> roleSet = u.getRoles();
        roleSet.remove(roleRepository.findByName(RoleName.ROLE_BANNED).get());

        userRepository.save(u);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("")
                .buildAndExpand().toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User UN-Banned"));
    }
}
