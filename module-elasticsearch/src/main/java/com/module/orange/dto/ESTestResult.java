package com.module.orange.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.RequestLine;

@Data
@NoArgsConstructor
public class ESTestResult {

    private RequestLine requestLine;
    private HttpHost host;
    private int statusCode;
    private Header[] headers;
    private String responseBody;

    public ESTestResult(int statusCode, String responseBody) {
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public ESTestResult(RequestLine requestLine, int statusCode, String responseBody) {
        this.requestLine = requestLine;
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public ESTestResult(RequestLine requestLine, HttpHost host, int statusCode, String responseBody) {
        this.requestLine = requestLine;
        this.host = host;
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public ESTestResult(RequestLine requestLine, HttpHost host, int statusCode, Header[] headers, String responseBody) {
        this.requestLine = requestLine;
        this.host = host;
        this.statusCode = statusCode;
        this.headers = headers;
        this.responseBody = responseBody;
    }
}
