package com.example.ai.awsdemo.model.dto;

import java.util.List;

public class QueryResponseDto {

    String queryId;
    String query;
    String queryAlias;
    List<AnswerResponseDto> answers;

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQueryAlias() {
        return queryAlias;
    }

    public void setQueryAlias(String queryAlias) {
        this.queryAlias = queryAlias;
    }

    public List<AnswerResponseDto> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerResponseDto> answers) {
        this.answers = answers;
    }
}
