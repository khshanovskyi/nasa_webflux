package com.example.nasa_webflux.service;

import com.example.nasa_webflux.entity.Photo;
import com.example.nasa_webflux.entity.Photos;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
public class PictureService {
    public Mono<byte[]> findLargestPicture(int sol) {
        URI uri = UriComponentsBuilder.fromHttpUrl("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos")
                .queryParam("sol", sol)
                .queryParam("api_key", "A8TWOxIOLYey639GppGaUsthUE3etmlDlYr1MfbS")
                .build()
                .toUri();

        return WebClient.create(uri.toString())
                .get()
                .retrieve()
                .bodyToMono(Photos.class)
                .flatMapMany(photos -> Flux.fromIterable(photos.getPhotos()))
                .map(Photo::getUrl)
                .flatMap(url -> getUriMono(url)
                        .flatMap(this::getResponseEntityMono))
                .reduce((o1, o2) -> o1.getHeaders().getContentLength() > o2.getHeaders().getContentLength() ? o1 : o2)
                .mapNotNull(HttpEntity::getBody);
    }

    private Mono<URI> getUriMono(String url) {
        return WebClient.create(url)
                .head()
                .retrieve()
                .toBodilessEntity()
                .mapNotNull(voidResponseEntity -> voidResponseEntity.getHeaders().getLocation());
    }

    private Mono<ResponseEntity<byte[]>> getResponseEntityMono(URI location) {
        return WebClient.create(location.toString())
                .mutate()
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(10_000_000))
                .build()
                .get()
                .retrieve()
                .toEntity(byte[].class);
    }


}
