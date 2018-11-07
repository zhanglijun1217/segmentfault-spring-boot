package com.zhanglijun.springbootdemo.web.client;

import com.zhanglijun.springbootdemo.User;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author 夸克
 * @date 2018/9/16 15:52
 */
public class RestClient {

    public static void main(String[] args) {
        // restTemplate的方式
        RestTemplate restTemplate = new RestTemplate();

        User user = restTemplate.getForObject("http://localhost:7001/json/user", User.class);
        System.out.println(user);

        // httpclient的方式
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        HttpClient httpClient = httpClientBuilder.build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        // 可以将factory传入restTemplate中，用httpClient去调用服务
        User factoryUser = restTemplate.getForObject("http://localhost:7001/json/user", User.class);
        System.out.println(factoryUser);
    }
}
