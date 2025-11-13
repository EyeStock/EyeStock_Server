package com.api.sss.model.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class NewsResponse {
    private List<Result> results;

    @Getter
    @NoArgsConstructor
    public static class Result {
        private String keyword;
        private String url;
        private String title;
        private String date;
    }

}