package com.example.nasa_webflux.entity;

import lombok.Data;

import java.util.List;

@Data
public class Photos {
    private List<Photo> photos;
}
