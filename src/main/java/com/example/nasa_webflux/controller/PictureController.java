package com.example.nasa_webflux.controller;

import com.example.nasa_webflux.service.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pictures")
@RequiredArgsConstructor
public class PictureController {

    private final PictureService pictureService;

    @GetMapping(value = "/{sol}/largest", produces = MediaType.IMAGE_PNG_VALUE)
    public Mono<byte[]> getPicture(@PathVariable int sol) {
        return pictureService.findLargestPicture(sol);
    }
}
