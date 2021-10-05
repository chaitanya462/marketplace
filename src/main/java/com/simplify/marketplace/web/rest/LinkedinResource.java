package com.simplify.marketplace.web.rest;

import org.springframework.web.bind.annotation.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

@RestController
@RequestMapping("/api")
public class LinkedinResource{

    @GetMapping("/token/{code}")
    public JSONObject  Gettoken(@PathVariable String code) throws Exception{
        HttpGet get = new HttpGet("https://www.linkedin.com/oauth/v2/accessToken?grant_type=authorization_code&client_id=78sw6g7e4nfe3e&client_secret=VasF4U08tYJMZbRE&code="+code+"&redirect_uri=http://marketplace.simplifysandbox.net/");
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse getresponse = httpClient.execute(get);
        String getresult = EntityUtils.toString(getresponse.getEntity());
        JSONParser getparser = new JSONParser();  
        JSONObject token = (JSONObject) getparser.parse(getresult);  

        HttpGet email = new HttpGet("https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))");
        email.addHeader("Authorization", "Bearer " + token.get("access_token"));
        HttpResponse emailresponse = httpClient.execute(email);
        String emailresult = EntityUtils.toString(emailresponse.getEntity());
        JSONObject emailjson = (JSONObject) getparser.parse(emailresult);
        return emailjson;
    
    }
}