package sjs.assignment.sjsassignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sjs.assignment.sjsassignment.dto.UserRequestDto;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ApiService {

    private RestTemplate restTemplate;

    public String getScrap(String token, UserRequestDto userRequestDto ) {
        String url = "https://codetest.3o3.co.kr/scrap/";
        HttpHeaders httpheaders = new HttpHeaders();
        httpheaders.set("Authorization","Bearer " + token);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("name", userRequestDto.getName())
                .queryParam("regNo", userRequestDto.getRegNo());

        HttpEntity<String> entity = new HttpEntity<>(httpheaders);
        String str = restTemplate.exchange(builder.toUriString(), HttpMethod.POST,entity,String.class).getBody();
        return str;
    }
}
