package orange.elasticsearch.controller;

import orange.elasticsearch.dto.ESTestParam;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/search")
public class SearchController {

//    @Autowired
//    private RestClient restClient;

//    /**
//     * 搜索检测
//     * @param param
//     * @return
//     * @throws Exception
//     */
//    @PostMapping("/test")
//    public ESTestResult test(@Valid @RequestBody ESTestParam param) throws Exception {
//        Request request = new Request(param.getMethod(), param.getUri());
//        if(null != param.getParamMap() && param.getParamMap().size() > 0) {
//            for (Map.Entry<String, String> entry: param.getParamMap().entrySet()) {
//                request.addParameter(entry.getKey(), entry.getValue());
//            }
//        }
//
//        Response response = restClient.performRequest(request);
//        RequestLine requestLine = response.getRequestLine();
//        HttpHost host = response.getHost();
//        int statusCode = response.getStatusLine().getStatusCode();
//        Header[] headers = response.getHeaders();
//        String responseBody = EntityUtils.toString(response.getEntity());
//        return new ESTestResult(requestLine, host, statusCode, headers, responseBody);
//    }

    /**
     * 搜索检测
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping("/test")
    public String search(@Valid @RequestBody ESTestParam param) throws Exception {
        IndexRequest request = new IndexRequest(
                "posts",
                "doc",
                "1");
        String jsonString = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        request.source(jsonString, XContentType.JSON);
        return "sdfsdfds";
    }
}
