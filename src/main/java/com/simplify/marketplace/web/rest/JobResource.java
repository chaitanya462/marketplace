package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.domain.VmsjobSave;
import com.simplify.marketplace.domain.VmsjobSubmit;
import com.simplify.marketplace.domain.Worker;
import com.simplify.marketplace.domain.JobPreference;
import com.simplify.marketplace.repository.VmsjobSaveRepository;
import com.simplify.marketplace.repository.VmsjobSubmitRepository;
import com.simplify.marketplace.service.JobPreferenceService;
import com.simplify.marketplace.repository.WorkerRepository;
import com.simplify.marketplace.service.WorkerService;
import com.simplify.marketplace.service.dto.WorkerDTO;
import com.simplify.marketplace.service.mapper.WorkerMapper;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.time.*;
import java.time.format.DateTimeFormatter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class JobResource {
    @Autowired
    JobPreferenceService jobPreferenceService;

    @Autowired
    WorkerService workerService;

    @Autowired
    WorkerRepository workerRepo;

    @Autowired
    VmsjobSubmitRepository vmsjobsubmitrepo;

    @Autowired
    WorkerMapper workermapper;

    @Autowired
    VmsjobSaveRepository vmsjobsaverepo;

    @GetMapping("/jobs")
    public JSONObject Getjoblist() throws Exception {
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

        // System.out.println("\n\n\n\n---------------------------\n\n\n\n");

        // System.out.println(EntityUtils.toString(response.getEntity()));
        String result = EntityUtils.toString(response.getEntity());
        JSONParser parser = new JSONParser();
        JSONObject jsn = (JSONObject) parser.parse(result);
        String jwt = jsn.get("token").toString();

        // System.out.println("\n\n\n\n---------------------------\n\n\n\n");

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
    public JSONObject Getjob(@PathVariable String id) throws Exception {
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

        // System.out.println("\n\n\n\n---------------------------\n\n\n\n");

        // System.out.println(EntityUtils.toString(response.getEntity()));
        String result = EntityUtils.toString(response.getEntity());
        JSONParser parser = new JSONParser();
        JSONObject jsn = (JSONObject) parser.parse(result);
        String jwt = jsn.get("token").toString();

        // System.out.println("\n\n\n\n---------------------------\n\n\n\n");

        HttpGet get = new HttpGet(
            "https://qa-services.simplifysandbox.net/job-manager/programs/99e3e918-3f69-4938-8ccb-46a86b45bc7a/jobs/" + id
        );

        // add request headers
        get.addHeader("Authorization", "Bearer " + jwt);
        HttpResponse getresponse = httpClient.execute(get);
        String getresult = EntityUtils.toString(getresponse.getEntity());
        JSONParser getparser = new JSONParser();
        JSONObject job = (JSONObject) getparser.parse(getresult);
        return job;
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/createVMSCandidate")
    public JSONObject postsubmisstionRecord() throws Exception {
        HttpPost post = new HttpPost("https://qa-services.simplifysandbox.net/authenticate");

        JSONObject json = new JSONObject();
        json.put("username", "Gauravj@simplifyvms.com");
        json.put("password", "Gaurav@17");

        StringEntity params = new StringEntity(json.toString());
        post.addHeader("content-type", "application/json");
        post.setEntity(params);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(post);

        String result = EntityUtils.toString(response.getEntity());
        JSONParser parser = new JSONParser();
        JSONObject jsn = (JSONObject) parser.parse(result);

        //token fetched
        String jwt = jsn.get("token").toString();

        //post request
        HttpPost postmap = new HttpPost("https://qa-services.simplifysandbox.net/configurator/candidates");

        //headers
        postmap.addHeader("Authorization", "Bearer " + jwt);
        postmap.addHeader("content-type", "application/json");

        //payload
        JSONObject payload = new JSONObject();
        payload.put("first_name", "chaitanya");
        payload.put("last_name", "chaitanya");
        payload.put("email", "chaitanya792@gmail.com");
        payload.put("phone_number", "7993588594");
        payload.put("name_prefix", "cha");
        StringEntity body = new StringEntity(payload.toString());
        postmap.setEntity(body);

        //http call
        HttpResponse getresponse = httpClient.execute(postmap);

        String getresult = EntityUtils.toString(getresponse.getEntity());
        JSONParser getparser = new JSONParser();

        JSONObject cadidateCreationResponse = (JSONObject) getparser.parse(getresult);
        return cadidateCreationResponse;
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/cadidateSubmission/{worker_id}/{job_id}")
    public JSONObject candidateSubmissionRecord(@PathVariable("worker_id") Long workerId, @PathVariable("job_id") String job_id)
        throws Exception {
        //    	job_id = "645aaf45-572b-4499-b05b-d54cac36600c";
        Worker worker = workerRepo.findById(workerId).get();
        Worker workerpatch = new Worker();
        workerpatch.setId(workerId);
        JSONObject candidate = new JSONObject();
        JSONObject finalbody = new JSONObject();
        JSONArray cadidateArray = new JSONArray();
        JSONObject payload = new JSONObject();

        HttpPost post = new HttpPost("https://qa-services.simplifysandbox.net/authenticate");

        JSONObject json = new JSONObject();
        json.put("username", "Gauravj@simplifyvms.com");
        json.put("password", "Gaurav@17");

        StringEntity params = new StringEntity(json.toString());
        post.addHeader("content-type", "application/json");
        post.setEntity(params);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(post);

        String result = EntityUtils.toString(response.getEntity());
        JSONParser parser = new JSONParser();
        JSONObject jsn = (JSONObject) parser.parse(result);

        //token fetched
        String jwt = jsn.get("token").toString();

        String worker_candidate_id = worker.getCandidate_id();

        if (worker_candidate_id != null) {
            payload.put("candidate_id", worker_candidate_id);
        } else {
            //post request
            HttpPost postmap = new HttpPost("https://qa-services.simplifysandbox.net/configurator/candidates");

            //headers
            postmap.addHeader("Authorization", "Bearer " + jwt);
            postmap.addHeader("content-type", "application/json");

            //payload

            payload.put("first_name", worker.getFirstName());
            payload.put("last_name", worker.getLastName());
            payload.put("email", "arun@gmail.com");
            payload.put("phone_number", worker.getPrimaryPhone());
            payload.put("name_prefix", "Mr");
            StringEntity body = new StringEntity(payload.toString());
            postmap.setEntity(body);

            //http call
            HttpResponse getresponse = httpClient.execute(postmap);

            String getresult = EntityUtils.toString(getresponse.getEntity());
            JSONParser getparser = new JSONParser();

            JSONObject cadidateCreationResponse = (JSONObject) getparser.parse(getresult);
            System.out.println("\n\n\n\n......"+cadidateCreationResponse+".....\n\n\n");


            payload.put("candidate_id", cadidateCreationResponse.get("id"));
            System.out.println("\n\n\n\n..........."+cadidateCreationResponse.get("id")+"\n\n\n");
            workerpatch.setCandidate_id((String) cadidateCreationResponse.get("id"));
        }

        //submission record payload;

        String available_date = "", available_start_date = "", available_end_date = "";

        payload.put("vendor_id", "a3c0c26d-c5bb-4e2a-ab04-99a705639366");
        JobPreference jobPreference=new JobPreference();
        for(JobPreference x:jobPreferenceService.findOneWorker(workerId))
            jobPreference=x;
        DateTimeFormatter dateformatter= DateTimeFormatter.ofPattern("dd/MM/YYYY");
        payload.put("available_start_date", ""+dateformatter.format(jobPreference.getAvailableFrom()));
        payload.put("available_end_date", ""+dateformatter.format(jobPreference.getAvailableTo()));
        JSONArray ratesArray = new JSONArray();
        JSONObject rate = new JSONObject();
        rate.put("rate_factor", "ST");
        rate.put("bill_rate", 100.0);
        rate.put("pay_rate", 80.00);
        ratesArray.add(rate);
        payload.put("rates", ratesArray);

        cadidateArray.add(payload);

        finalbody.put("candidates", cadidateArray);

        System.out.println("\n\n\n\n.....final----\n."+finalbody+".....\n\n\n");
        //cadidate submission api

        HttpPost submissionMapping = new HttpPost(
            "https://qa-services.simplifysandbox.net/configurator/programs/99e3e918-3f69-4938-8ccb-46a86b45bc7a/jobs/" +
            job_id +
            "/candidates/submit"
        );
        StringEntity requestBody = new StringEntity(finalbody.toString());
        System.out.println("\n\n\n\n......"+requestBody+".....\n\n\n");

        submissionMapping.setEntity(requestBody);
        submissionMapping.addHeader("Authorization", "Bearer " + jwt);
        submissionMapping.addHeader("content-type", "application/json");
        System.out.println("\n\n\n\n......273.....\n\n\n");
        HttpResponse submissionResponse = httpClient.execute(submissionMapping);
        String getresult = EntityUtils.toString(submissionResponse.getEntity());
        JSONParser getparser = new JSONParser();
        JSONObject submissionRecordRes = (JSONObject) getparser.parse(getresult);
        if (submissionRecordRes.get("error") != null) {
            return submissionRecordRes;
        }
        System.out.println("\n\n\n\n......"+submissionRecordRes+".....\n\n\n");
        VmsjobSubmit vmsjobSubmit = new VmsjobSubmit();
        vmsjobSubmit.setVmsjobsubmitName("645aaf45-572b-4499-b05b-d54cac36600c");
        vmsjobSubmit.setSubmissionId((String) submissionRecordRes.get("id"));
        // System.out.println("\n\n\n\n\n" + submissionRecordRes + "\n\n\n\n\n");
        VmsjobSubmit vmsres = vmsjobsubmitrepo.save(vmsjobSubmit);

        workerpatch.setVmsjobsubmits(workerService.getworkervmsjobsubmits(workerId));
        workerpatch.addVmsjobsubmit(vmsres);
        System.out.println("\n\n\n\n......296.....\n\n\n");
        WorkerDTO s = workermapper.toDto(workerpatch);

        workerService.partialUpdate(s);

        return submissionRecordRes;
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/workerJobSave/{worker_id}/{job_id}")
    public WorkerDTO workerJobSave(
        @PathVariable("worker_id") Long worker_id,
        @PathVariable("job_id") String job_id
    ) {
        Worker worker = workerRepo.findById(worker_id).get();
        Worker workerpatch = new Worker();
        boolean check=false;
        VmsjobSave temp=new VmsjobSave();
        for(VmsjobSave x: worker.getVmsjobsaves()){
            if((x.getVmsjobsaveName()).equals(job_id)){
                check=true;
                temp=x;
                break;
            }
        }
        workerpatch.setId(worker_id);
        if (!check) {
            VmsjobSave vmsjobsave = vmsjobsaverepo.findByVmsjobsaveName(job_id);
            if(vmsjobsave == null){
                vmsjobsave = new VmsjobSave();
                vmsjobsave.setVmsjobsaveName(job_id);
                vmsjobsave = vmsjobsaverepo.save(vmsjobsave);
            }
            workerpatch=workerpatch.addVmsjobsave(vmsjobsave);
        }
        else{
            workerpatch= workerpatch.removeVmsjobsave(temp);
        }
        WorkerDTO s = workermapper.toDto(workerpatch);
        return workerService.partialUpdate(s).get();
    }
}
