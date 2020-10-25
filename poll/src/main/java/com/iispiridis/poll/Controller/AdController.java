package com.iispiridis.poll.Controller;


import com.iispiridis.poll.Models.Ad;
import com.iispiridis.poll.Payload.AdRequest;
import com.iispiridis.poll.Payload.AdResponse;
import com.iispiridis.poll.Payload.ApiResponse;
import com.iispiridis.poll.Payload.PagedResponse;
import com.iispiridis.poll.Repositories.UserRepository;
import com.iispiridis.poll.Security.CurrentUser;
import com.iispiridis.poll.Security.UserPrincipal;
import com.iispiridis.poll.Service.AdService;
import com.iispiridis.poll.Util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ads")
public class AdController
{
    @Autowired
    AdService adService;

    @Autowired
    UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createAd(@Valid @RequestBody AdRequest adRequest, @CurrentUser UserPrincipal currentUser)
    {
        Ad ad = adService.createAd(adRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{adId}")
                .buildAndExpand(ad.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, ""+ad.getId()+" Ad Created Successfully"));
    }

    @GetMapping("/{adId}")
    public AdResponse getAdById(@PathVariable("adId") Long id)
    {
        return adService.getAdById(id);
    }

    @GetMapping("/my")
    public List<AdResponse> getMyAds(@CurrentUser UserPrincipal currentUser)
    {
        return adService.getUserAds(currentUser.getId());
    }

    @GetMapping
    public PagedResponse<AdResponse> getAllAds(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                               @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size)
    {
        return adService.getAll(page, size);
    }





}
