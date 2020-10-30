package com.iispiridis.poll.Service;


import com.iispiridis.poll.Models.DBImage;
import com.iispiridis.poll.Repositories.AdRepository;
import com.iispiridis.poll.Repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ImageService
{

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AdRepository adRepository;

    public DBImage store(MultipartFile file, Long adId) throws IOException
    {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        DBImage dbImage = new DBImage(filename, file.getContentType(), file.getBytes());
        dbImage.setAd(adRepository.findById(adId).get());

        return imageRepository.save(dbImage);
    }

    public DBImage getImage(Long id)
    {
        return imageRepository.findById(id).get();
    }

    public Stream<DBImage> getAllImages()
    {
        return imageRepository.findAll().stream();
    }

    public List<DBImage> getImagesFromAdId(Long id) { return imageRepository.findAllByAd(id); }
}
