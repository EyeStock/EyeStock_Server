package com.api.sss.model.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class NewsRequest {
    private List<String> keywords;
    private int days;
    private int max_links;

}