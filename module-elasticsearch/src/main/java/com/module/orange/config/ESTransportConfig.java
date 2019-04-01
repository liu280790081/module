package com.module.orange.config;//package com.orange.search.config;
//
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.TransportAddress;
//import org.elasticsearch.transport.client.PreBuiltTransportClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
//@Configuration
//public class ESTransportConfig {
//
//    private String host = "39.105.65.204";
//
//    private int port = 9300;
//
//    @Bean
//    public TransportClient client() throws UnknownHostException {
//        Settings settings = Settings.builder().build();
//
//        TransportClient client = new PreBuiltTransportClient(settings)
//                .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
//        return client;
//    }
//
//
//}
