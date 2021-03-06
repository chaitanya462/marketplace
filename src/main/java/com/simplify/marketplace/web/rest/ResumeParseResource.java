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
import java.util.*;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api")
public class ResumeParseResource {
    @Autowired
    Environment env;

    @PostMapping("/resume/parse")
    public JSONObject  Resumeparse(@RequestBody JSONObject resume) throws Exception{

        
        HttpPost post = new HttpPost(env.getProperty("spring.application.vmsdomain")+"authenticate");
        // add request parameter, form parameters
        JSONObject json = new JSONObject();
        json.put("username", ""+env.getProperty("spring.application.vmsUsername")); 
        json.put("password", ""+env.getProperty("spring.application.vmsPassword"));

        StringEntity params = new StringEntity(json.toString());
        post.addHeader("content-type", "application/json");
        post.setEntity(params);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(post);
        
        // System.out.println("\n\n\n\n---------------------------\n\n\n\n");
        
        // System.out.println(EntityUtils.toString(response.getEntity()));
        String result = EntityUtils.toString(response.getEntity());
        JSONParser parser = new JSONParser();  
        JSONObject jsn = (JSONObject) parser.parse(result);  
        String jwt= jsn.get("token").toString();
        
        // System.out.println("\n\n\n\n---------------------------\n\n\n\n");
        
        HttpPost parseResume = new HttpPost(env.getProperty("spring.application.vmsdomain")+"configurator/candidates/resumes/parse");

        // add request headers
        parseResume.addHeader("Authorization", "Bearer " + jwt);
        parseResume.addHeader("content-type", "application/json");

        params = new StringEntity(resume.toString());
        parseResume.setEntity(params);


        HttpResponse getresponse = httpClient.execute(parseResume);
        String getresult = EntityUtils.toString(getresponse.getEntity());
        JSONParser getparser = new JSONParser();  
        JSONObject  Parsed_resume = (JSONObject) getparser.parse(getresult);  
        return Parsed_resume;
    }

}

