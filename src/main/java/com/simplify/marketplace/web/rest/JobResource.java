package com.simplify.marketplace.web.rest;

import org.springframework.web.bind.annotation.*;


import org.apache.http.entity.StringEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;


@RestController
@RequestMapping("/api")
public class JobResource {

    @GetMapping("/jobs")
    public JSONObject  Getjoblist() throws Exception{

        
        HttpPost post = new HttpPost("https://qa-services.simplifysandbox.net/authenticate");
        // add request parameter, form parameters
        JSONObject json = new JSONObject();
        json.put("username", "Gauravj@simplifyvms.com"); 
        json.put("password", "Gaurav@17");

        StringEntity params = new StringEntity(json.toString());
        post.addHeader("content-type", "application/json");
        post.setEntity(params);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(post);
        
        System.out.println("\n\n\n\n---------------------------\n\n\n\n");
        
        // System.out.println(EntityUtils.toString(response.getEntity()));
        String result = EntityUtils.toString(response.getEntity());
        JSONParser parser = new JSONParser();  
        JSONObject jsn = (JSONObject) parser.parse(result);  
        String jwt= jsn.get("token").toString();
        
        System.out.println("\n\n\n\n---------------------------\n\n\n\n");
        
        HttpGet get = new HttpGet("https://qa-services.simplifysandbox.net/job-manager/programs/99e3e918-3f69-4938-8ccb-46a86b45bc7a/jobs");

        // add request headers
        get.addHeader("Authorization", "Bearer " + jwt);
        HttpResponse getresponse = httpClient.execute(get);
        String getresult = EntityUtils.toString(getresponse.getEntity());
        JSONParser getparser = new JSONParser();  
        JSONObject jobs = (JSONObject) getparser.parse(getresult);  
        return jobs;
    }
    @GetMapping("/jobs/{id}")
    public JSONObject  Getjob(@PathVariable String id) throws Exception{

        
        HttpPost post = new HttpPost("https://qa-services.simplifysandbox.net/authenticate");
        // add request parameter, form parameters
        JSONObject json = new JSONObject();
        json.put("username", "Gauravj@simplifyvms.com"); 
        json.put("password", "Gaurav@17");

        StringEntity params = new StringEntity(json.toString());
        post.addHeader("content-type", "application/json");
        post.setEntity(params);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(post);
        
        System.out.println("\n\n\n\n---------------------------\n\n\n\n");
        
        // System.out.println(EntityUtils.toString(response.getEntity()));
        String result = EntityUtils.toString(response.getEntity());
        JSONParser parser = new JSONParser();  
        JSONObject jsn = (JSONObject) parser.parse(result);  
        String jwt= jsn.get("token").toString();
        
        System.out.println("\n\n\n\n---------------------------\n\n\n\n");
        
        HttpGet get = new HttpGet("https://qa-services.simplifysandbox.net/job-manager/programs/99e3e918-3f69-4938-8ccb-46a86b45bc7a/jobs/"+id);

        // add request headers
        get.addHeader("Authorization", "Bearer " + jwt);
        HttpResponse getresponse = httpClient.execute(get);
        String getresult = EntityUtils.toString(getresponse.getEntity());
        JSONParser getparser = new JSONParser();  
        JSONObject job = (JSONObject) getparser.parse(getresult);  
        return job;

    }
}

