package com.module.orange.controller;

import com.module.orange.dto.ElasticSearchResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
//
//    @Inject
//    private TransportClient client;

    //搜索自动联想
    @GetMapping("/keyword/{key}")
    public List<ElasticSearchResult> findIndexRecordByName(@PathVariable("key") String key) {

        System.out.println("hello word!" + key);
//        // 构造查询请求
//        QueryBuilder bq = QueryBuilders.matchQuery("name.pinyin", key);
//        SearchRequestBuilder searchRequest = client.prepareSearch("medcl").setTypes("folks");
//
//        // 设置查询条件和分页参数
//        int start = 0;
//        int size = 5;
//        searchRequest.setQuery(bq).setFrom(start).setSize(size);
//
//        // 获取返回值，并进行处理
//        SearchResponse response = searchRequest.execute().actionGet();
//        SearchHits shs = response.getHits();
//        List<ElasticSearchResult> esResultList = new ArrayList<>();
//        for (SearchHit hit : shs) {
//            ElasticSearchResult esResult = new ElasticSearchResult();
//            double score = hit.getScore();
//            BigDecimal b = new BigDecimal(score);
//            score = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
//            String name = (String) hit.getSourceAsMap().get("name");
//            System.out.println("score:" + score + "name:" + name);
//            esResult.setScore(score);
//            esResult.setName(name);
//            esResultList.add(esResult);
//        }
//        return esResultList;
        return null;
    }

}
