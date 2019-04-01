package com.module.orange.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.RequestLine;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
@NoArgsConstructor
public class ESTestParam {

    /**
     * uri
     */
    @NotBlank
    private String uri;

    /**
     * 请求方式
     */
    @NotBlank
    private String method;

    /**
     * 参数
     */
    private Map<String, String> paramMap;

}
