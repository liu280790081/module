package com.module.orange.config;

import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.sniff.Sniffer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Configuration
public class ESRestConfig {

    private String host = "39.105.65.204";

    private int port = 9200;

    private String scheme = "http";


    private Path keyStorePath;
    private String keyStorePass;


    @Bean
    public RestClient ESRestClient() {
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(host, port, scheme));

        /**
         * 回调超时配置
         * Configuring requests timeouts can be done by providing an instance of RequestConfigCallback
         * while building the RestClient through its builder. The interface has one method that receives
         * an instance of org.apache.http.client.config.RequestConfig.Builder as an argument and has the
         * same return type. The request config builder can be modified and then returned. In the following
         * example we increase the connect timeout
         * (defaults to 1 second) and the socket timeout (defaults to 30 seconds)
         */
        builder.setRequestConfigCallback(requestConfigBuilder ->
                requestConfigBuilder
                        .setConnectTimeout(5000)
                        .setSocketTimeout(60000));

        /**
         * The Apache Http Async Client starts by default one dispatcher thread, and a number of worker
         * threads used by the connection manager, as many as the number of locally detected processors
         * (depending on what Runtime.getRuntime().availableProcessors() returns).
         */

        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            /* Disable preemptive authentication */
//            httpClientBuilder.disableAuthCaching();

            /* Basic authentication */
//        final CredentialsProvider credentialsProvider =
//                new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(AuthScope.ANY,
//                new UsernamePasswordCredentials("user", "password"));
//            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);

            /*
                Encrypted communication
                scheme需要改为 https
            */
//            KeyStore truststore = KeyStore.getInstance("jks");
//            try (InputStream is = Files.newInputStream(keyStorePath)) {
//                truststore.load(is, keyStorePass.toCharArray());
//            }
//            SSLContextBuilder sslBuilder = SSLContexts.custom()
//                    .loadTrustMaterial(truststore, null);
//            final SSLContext sslContext = sslBuilder.build();
//            httpClientBuilder.setSSLContext(sslContext);

            httpClientBuilder.setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(1).build());
            return httpClientBuilder;
        });

        builder.setNodeSelector(NodeSelector.SKIP_DEDICATED_MASTERS);
        return builder.build();
    }


//    @Bean
//    public Sniffer Sniffer(){
//
//        return Sniffer.builder(ESRestClient())
//                .setSniffIntervalMillis(60000).build();
//    }

}
