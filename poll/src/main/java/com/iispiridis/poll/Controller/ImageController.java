package com.iispiridis.poll.Controller;

import com.iispiridis.poll.Models.DBImage;
import com.iispiridis.poll.Service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController
{
    @Autowired
    ImageService imageService;

    @PostMapping
    @CrossOrigin(origins = "http://localhost:3000/")
    ResponseEntity<?> receiveImage( @RequestParam("filekey") MultipartFile file, @RequestParam("adId") Long adId) throws IOException
    {
        if (!file.isEmpty()) {
            try{
                imageService.store(file, adId);
                return ResponseEntity.status(HttpStatus.OK).body("Image uploaded");
            } catch (Exception e)
            {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Error encountered while uploading image");
            }
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Image was not received");
    }

    @GetMapping("/{id}")
    public DBImage getFile(@PathVariable Long id) {
        DBImage fileDB = imageService.getImage(id);

        return fileDB;

        /*
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
        */
        //return new ResponseEntity<byte[]>(fileDB.getData(), HttpStatus.OK);

    }

    @GetMapping("/gallery/{adid}")
    public List<DBImage> getImagesFromAd(@PathVariable Long adid) {
        List<DBImage> data = imageService.getImagesFromAdId(adid);
        return data;

        /*
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
        */
        //return new ResponseEntity<byte[]>(fileDB.getData(), HttpStatus.OK);

    }
}
