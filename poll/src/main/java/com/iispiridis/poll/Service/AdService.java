package com.iispiridis.poll.Service;

import com.iispiridis.poll.Models.Ad;
import com.iispiridis.poll.Models.Category;
import com.iispiridis.poll.Models.CategoryName;
import com.iispiridis.poll.Models.User;
import com.iispiridis.poll.Payload.AdRequest;
import com.iispiridis.poll.Payload.AdResponse;
import com.iispiridis.poll.Payload.PagedResponse;
import com.iispiridis.poll.Repositories.*;
import com.iispiridis.poll.Util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdService
{
    @Autowired
    AdRepository adRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    RatingService ratingService;

    @Autowired
    RatingRepository ratingRepository;

    public Ad createAd(AdRequest adRequest) {
        Ad ad = new Ad();

        ad.setTitle(adRequest.getTitle());
        ad.setDescription(adRequest.getDescription());

        return adRepository.save(ad);
    }

    public void deleteAd(Long id)
    {
        Ad a = adRepository.getOne(id);
        Set<Category> emptyCategories = new HashSet<>();
        a.setCategories(emptyCategories);


        imageRepository.deleteByAdId(id);
        Ad a2 = adRepository.save(a);

        commentRepository.deleteByAdId(id);
        ratingRepository.deleteByAdId(id);

        adRepository.delete(a2);
    }

    public Ad updateAd(AdRequest adRequest)
    {
        Ad ad = adRepository.findById(adRequest.getId()).get();

        ad.setTitle(adRequest.getTitle());
        ad.setDescription(adRequest.getDescription());
        ad.setLocation(adRequest.getLocation());

        if ( adRequest.getCategories().size() != 0 )
        {
            Set<Category> temp = new HashSet<>();
            for(Category c: adRequest.getCategories())
            {
                temp.add(categoryRepository.findByName(c.getName()).get()) ;
            }
            ad.setCategories(temp);
        }

        return adRepository.save(ad);
    }

    public AdResponse getAdById(Long id, Long currentUserId)
    {
        Ad a = adRepository.findById(id).get();
        User u  = userRepository.findById(a.getCreatedBy()).get();

        return ModelMapper.mapAdResponse(a, u, commentService.getMappedAdComments(id), ratingService.getAdRatingResponse(id, currentUserId));
    }


    public List<AdResponse> getUserAds(Long userid)
    {
        List<Ad> ads = adRepository.findByUserId(userid);
        List<AdResponse> responseList = new ArrayList<>();

        for (Ad a : ads)
        {
            AdResponse x = ModelMapper.mapAdResponse(a, userRepository.findById(userid).get(),
                    commentService.getMappedAdComments(a.getId()), ratingService.getAdRatingResponse(a.getId(), userid));
            responseList.add(x);
        }

        return responseList;
    }

    public PagedResponse<AdResponse> getAll(int page, int size, long currentUserId)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Ad> ads = adRepository.findAll(pageable);

        if(ads.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), ads.getNumber(),
                    ads.getSize(), ads.getTotalElements(), ads.getTotalPages(), ads.isLast());
        }

        List<AdResponse> adResponses = ads.map(a ->{
            return ModelMapper.mapAdResponse(a, userRepository.findById(a.getCreatedBy()).get(),
                    commentService.getMappedAdComments(a.getId()), ratingService.getAdRatingResponse(a.getId(), currentUserId) );
        }).getContent();


        return new PagedResponse<>(adResponses, ads.getNumber(), ads.getSize(), ads.getTotalElements(), ads.getTotalPages(), ads.isLast());
    }

    public PagedResponse<AdResponse> searchByCategory(int page, int size, List<String> categoryNames, long currentUserId)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        List<CategoryName> categoryNameList = new ArrayList<>();
        for (String s : categoryNames)
        {
            categoryNameList.add(CategoryName.valueOf(s));
        }

        System.out.println(categoryNameList);
        Page<Ad> ads = adRepository.findByCategory(pageable, categoryNameList, Long.valueOf(categoryNameList.size()) );

        if(ads.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), ads.getNumber(),
                    ads.getSize(), ads.getTotalElements(), ads.getTotalPages(), ads.isLast());
        }

        List<AdResponse> adResponses = ads.map(a ->{
            return ModelMapper.mapAdResponse(a, userRepository.findById(a.getCreatedBy()).get(),
                    commentService.getMappedAdComments(a.getId()),ratingService.getAdRatingResponse(a.getId(), currentUserId) );
        }).getContent();

        //PagedResponse<>(pollResponses, polls.getNumber(),
        //                polls.getSize(), polls.getTotalElements(), polls.getTotalPages(), polls.isLast());

        return new PagedResponse<>(adResponses, ads.getNumber(), ads.getSize(), ads.getTotalElements(), ads.getTotalPages(), ads.isLast());
    }
}
