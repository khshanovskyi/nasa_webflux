package com.example.nasa_webflux.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Photo {
    @JsonProperty("img_src")
    private String url;
}
