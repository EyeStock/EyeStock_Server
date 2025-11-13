package com.api.sss.model.dto.request;

import lombok.Getter;

@Getter
public class NewsRequest {
    private String question;
    private int days;
    private int max_links;

}