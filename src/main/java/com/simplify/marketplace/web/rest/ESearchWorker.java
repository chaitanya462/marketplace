package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.domain.ElasticWorker;
import com.simplify.marketplace.repository.ESearchWorkerRepository;
import java.util.ArrayList;
//import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ESearchWorker {

    @Autowired
    ESearchWorkerRepository workerRepo;

    @GetMapping("/workers/{id}")
    public ElasticWorker getWorker(@PathVariable String id) {
        return workerRepo.findById(id).get();
    }

    @PostMapping("/workers")
    public ElasticWorker postWorker(@RequestBody ElasticWorker EW) {
        return workerRepo.save(EW);
    }

    @GetMapping("/allworkers")
    public Iterable<ElasticWorker> getall() {
        return workerRepo.findAll();
    }

    @GetMapping("/allworkerscustom")
    public ArrayList<ElasticWorker> matchall() {
        return workerRepo.matchAll();
    }

    @DeleteMapping("/deleteAll")
    public String deleteAll() {
        workerRepo.deleteAll();
        return "Records Deleted Successfully";
    }

    @GetMapping("/workerSearchQuery/{SearchQuery}")
    public ArrayList<ElasticWorker> searchWorkers(@PathVariable String SearchQuery) {
        return workerRepo.searchQuery(SearchQuery);
    }

    @GetMapping("/searchByEngagementType/{name}")
    public ArrayList<ElasticWorker> searchByEngagementType(@PathVariable String name) {
        return workerRepo.searchByEngagementType(name);
    }

    @GetMapping("/searchByEmploymentType/{name}")
    public ArrayList<ElasticWorker> searchByEmploymentType(@PathVariable String name) {
        return workerRepo.searchByEmploymentType(name);
    }

    @GetMapping("/searchBylocationType/{name}")
    public ArrayList<ElasticWorker> searchBylocationType(@PathVariable String name) {
        return workerRepo.searchBylocationType(name);
    }

    @GetMapping("/searchByfiltersOr/{Category}/{SubCategory}/{location}/{searchText}")
    public ArrayList<ElasticWorker> searchByfiltersOr(
        @PathVariable("Category") String Category,
        @PathVariable("SubCategory") String subcategory,
        @PathVariable("location") String location,
        @PathVariable("searchText") String searchText
    ) {
        return workerRepo.allfiltersOr(Category, subcategory, location, searchText);
    }

    @GetMapping("/searchByfilters/{Category}/{SubCategory}/{location}/{searchText}")
    public ArrayList<ElasticWorker> searchByfilters(
        @PathVariable("Category") String Category,
        @PathVariable("SubCategory") String subcategory,
        @PathVariable("location") String location,
        @PathVariable("searchText") String searchText
    ) {
        return workerRepo.allfilters(Category, subcategory, location, searchText);
    }

    @GetMapping("/searchByCategoryText/{Category}/{searchText}")
    public ArrayList<ElasticWorker> searchByCategoryText(
        @PathVariable("Category") String Category,
        @PathVariable("searchText") String searchText
    ) {
        return workerRepo.searchByCategoryText(Category, searchText);
    }

    @GetMapping("/searchByCategorySubText/{Category}/{SubCategory}/{searchText}")
    public ArrayList<ElasticWorker> searchByCategorySubText(
        @PathVariable("Category") String Category,
        @PathVariable("SubCategory") String subcategory,
        @PathVariable("searchText") String searchText
    ) {
        return workerRepo.searchByCategorySubText(Category, subcategory, searchText);
    }

    @GetMapping("/searchByfilters/{location}/{searchText}")
    public ArrayList<ElasticWorker> searchByLocationText(
        @PathVariable("location") String location,
        @PathVariable("searchText") String searchText
    ) {
        return workerRepo.searchByLocationText(searchText, location);
    }
}
