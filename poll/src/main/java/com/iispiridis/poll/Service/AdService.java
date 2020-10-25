package com.iispiridis.poll.Service;

import com.iispiridis.poll.Models.Ad;
import com.iispiridis.poll.Models.User;
import com.iispiridis.poll.Payload.AdRequest;
import com.iispiridis.poll.Payload.AdResponse;
import com.iispiridis.poll.Payload.PagedResponse;
import com.iispiridis.poll.Repositories.AdRepository;
import com.iispiridis.poll.Repositories.UserRepository;
import com.iispiridis.poll.Util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AdService
{
    @Autowired
    AdRepository adRepository;

    @Autowired
    UserRepository userRepository;

    public Ad createAd(AdRequest adRequest) {
        Ad ad = new Ad();
        ad.setTitle(adRequest.getTitle());
        ad.setDescription(adRequest.getDescription());

        return adRepository.save(ad);
    }

    public AdResponse getAdById(Long id)
    {
        Optional<Ad> ad = adRepository.findById(id);
        Ad a;
        if ( ad.isEmpty() ) return null;
        else
        {
            a = ad.get();
            User u  = userRepository.findById(a.getCreatedBy()).get();

            AdResponse adResponse = new AdResponse();
            adResponse.setCreatedAt(a.getCreatedAt());
            adResponse.setUser(u);
            adResponse.setDescription(a.getDescription());
            adResponse.setTitle(a.getTitle());
            adResponse.setId(a.getId());

            return adResponse;
        }
    }

    public List<AdResponse> getUserAds(Long userid)
    {
        List<Ad> ads = adRepository.findByUserId(userid);
        List<AdResponse> responseList = new ArrayList<>();

        for (Ad a : ads)
        {
            AdResponse x = ModelMapper.mapAdResponse(a, userRepository.findById(userid).get());
            responseList.add(x);
        }

        return responseList;
    }

    public PagedResponse<AdResponse> getAll(int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Ad> ads = adRepository.findAll(pageable);

        if(ads.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), ads.getNumber(),
                    ads.getSize(), ads.getTotalElements(), ads.getTotalPages(), ads.isLast());
        }

        List<AdResponse> adResponses = ads.map(a ->{
            return ModelMapper.mapAdResponse(a, userRepository.findById(a.getCreatedBy()).get());
        }).getContent();

        //PagedResponse<>(pollResponses, polls.getNumber(),
        //                polls.getSize(), polls.getTotalElements(), polls.getTotalPages(), polls.isLast());

        return new PagedResponse<>(adResponses, ads.getNumber(), ads.getSize(), ads.getTotalElements(), ads.getTotalPages(), ads.isLast());
    }
}
