package com.module.orange.controller;

import com.module.orange.dto.ESTestParam;
import com.module.orange.dto.ESTestResult;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.RequestLine;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private RestClient restClient;

    /**
     * 搜索检测
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping("/test")
    public ESTestResult findIndexRecordByName(@Valid @RequestBody ESTestParam param) throws Exception {
        Request request = new Request(param.getMethod(), param.getUri());
        if(null != param.getParamMap() && param.getParamMap().size() > 0) {
            for (Map.Entry<String, String> entry: param.getParamMap().entrySet()) {
                request.addParameter(entry.getKey(), entry.getValue());
            }
        }

        Response response = restClient.performRequest(request);
        RequestLine requestLine = response.getRequestLine();
        HttpHost host = response.getHost();
        int statusCode = response.getStatusLine().getStatusCode();
        Header[] headers = response.getHeaders();
        String responseBody = EntityUtils.toString(response.getEntity());
        return new ESTestResult(requestLine, host, statusCode, headers, responseBody);
    }

}
