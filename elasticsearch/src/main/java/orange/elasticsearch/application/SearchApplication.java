package orange.elasticsearch.application;

import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public interface SearchApplication {

    @Autowired
    TransportClient client = null;


    default String testDefault() {
        return "hello word";
    }

    static void test() {

    }
}
