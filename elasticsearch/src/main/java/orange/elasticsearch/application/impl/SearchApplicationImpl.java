package orange.elasticsearch.application.impl;

import orange.elasticsearch.application.SearchApplication;
import orange.elasticsearch.application.TestApplication;
import org.springframework.stereotype.Component;

@Component
public class SearchApplicationImpl implements SearchApplication, TestApplication {

//    @Autowired
//    private TransportClient client;


    public void add(String name) {

        TestApplication.test();

//        try {
//            XContentBuilder content = XContentFactory.jsonBuilder()
//                    .startObject()
//                    .field("name", name)
//                    .endObject();
//
//            IndexResponse result = this.client.prepareIndex("medcl", "folks")
//                    .setSource(content)
//                    .get();
//            System.out.println(result.getId());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void deleteByName(String name) {
//        BulkRequestBuilder bulkRequest = client.prepareBulk();
//        SearchResponse response = client.prepareSearch("medcl").setTypes("folks")
//                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//                .setQuery(QueryBuilders.termQuery("name", name))
//                .setFrom(0).setSize(20).setExplain(true).execute().actionGet();
//        for (SearchHit hit : response.getHits()) {
//            String id = hit.getId();
//            bulkRequest.add(client.prepareDelete("medcl", "folks", id).request());
//        }
//        BulkResponse bulkResponse = bulkRequest.get();
//
//        if (bulkResponse.hasFailures()) {
//            for (BulkItemResponse item : bulkResponse.getItems()) {
//                System.out.println(item.getFailureMessage());
//            }
//        } else {
//            System.out.println("delete ok");
//        }
    }

    @Override
    public String testDefault() {
        return null;
    }
}
