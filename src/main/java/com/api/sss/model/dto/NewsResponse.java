package com.api.sss.model.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class NewsResponse {
    private List<Result> results;

    public static class Result {
        private String keyword;
        private String url;
        private String title;
        private String date;
    }

}