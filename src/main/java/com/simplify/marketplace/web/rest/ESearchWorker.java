package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.domain.ElasticWorker;
//import com.simplify.marketplace.domain.Employment;
import com.simplify.marketplace.domain.EmploymentSuggestionEntity;
import com.simplify.marketplace.domain.SkillsSuggestionEntity;
//import com.simplify.marketplace.domain.SuggestionEntity;
import com.simplify.marketplace.repository.ESearchWorkerRepository;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.Map;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/search")
public class ESearchWorker {

    @Autowired
    ESearchWorkerRepository workerRepo;

    @Autowired
    RestHighLevelClient client;

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

    @GetMapping("/searchByDesignationAndlocation/{designation}/{location}")
    public ArrayList<ElasticWorker> searchByDesignationAndLocation(
        @PathVariable("designation") String designation,
        @PathVariable("location") String location
    ) {
        return workerRepo.searchByDesignationAndLocation(designation, location);
    }

    @GetMapping("/searchByDesignationAndLocationAndCategory/{designation}/{location}/{category}")
    public ArrayList<ElasticWorker> searchByDesignationAndLocationAndCategory(
        @PathVariable("designation") String designation,
        @PathVariable("location") String location,
        @PathVariable("category") String Category
    ) {
        return workerRepo.searchByDesignationAndLocationAndCategory(designation, location, Category);
    }

    @GetMapping("/searchByDesignationLocationAndCategorySub/{designation}/{location}/{category}/{subcategory}")
    public ArrayList<ElasticWorker> searchByDesignationLocationAndCategorySub(
        @PathVariable("designation") String designation,
        @PathVariable("location") String location,
        @PathVariable("category") String Category,
        @PathVariable("subcategory") String subcategory
    ) {
        return workerRepo.searchByDesignationLocationAndCategorySub(designation, location, Category, subcategory);
    }

    @GetMapping("/designationSuggestions/{prefix}")
    public ArrayList<EmploymentSuggestionEntity> getSuggestions(@PathVariable("prefix") String prefix) throws IOException {
        CompletionSuggestionBuilder completionSuggestionFuzzyBuilder = SuggestBuilders
            .completionSuggestion("Name")
            .prefix(prefix, Fuzziness.ZERO)
            .size(10)
            .skipDuplicates(true);

        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("suggest_user", completionSuggestionFuzzyBuilder);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("suggestionindex");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.suggest(suggestBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Suggest suggest = searchResponse.getSuggest();

        //        System.out.println("\n\n\n\n\n\n" + suggest + "\n\n\n\n\n\n");

        CompletionSuggestion termSuggestion = suggest.getSuggestion("suggest_user");

        ArrayList<EmploymentSuggestionEntity> ans = new ArrayList<>();

        for (CompletionSuggestion.Entry entry : termSuggestion.getEntries()) {
            for (CompletionSuggestion.Entry.Option option : entry) {
                Map<String, Object> val = option.getHit().getSourceAsMap();

                //		        	Employment emp  =(Employment)val.get("employments");

                //		        	System.out.println("\n\n\n\n\n"+val.get("employments")+"\n\n\n\n\n");
                EmploymentSuggestionEntity val1 = new EmploymentSuggestionEntity();

                val1.setId(option.getHit().getId());
                //		        	val1.setDesignation(emp.getJobTitle());
                //		        	val1.setId(option.getHit().getId());
                val1.setDesignation(option.getText().string());
                //
                //		        	val1.setSubCategory((String)val.get("subCategory"));
                //		        	val1.setSuggest((ArrayList<String>)val.get("suggest"));
                ans.add(val1);
            }
        }

        return ans;
    }

    @GetMapping("/skillsSuggestions/{prefix}")
    public ArrayList<SkillsSuggestionEntity> skillsgetSuggestions(@PathVariable("prefix") String prefix) throws IOException {
        CompletionSuggestionBuilder completionSuggestionFuzzyBuilder = SuggestBuilders
            .completionSuggestion("SkillName")
            .prefix(prefix, Fuzziness.ZERO)
            .size(10)
            .skipDuplicates(true);

        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("suggest_skills_user", completionSuggestionFuzzyBuilder);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("skillsuggestion");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.suggest(suggestBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Suggest suggest = searchResponse.getSuggest();

        //		 System.out.println("\n\n\n\n\n\n"+suggest+"\n\n\n\n\n\n");

        CompletionSuggestion termSuggestion = suggest.getSuggestion("suggest_skills_user");

        ArrayList<SkillsSuggestionEntity> ans = new ArrayList<>();

        for (CompletionSuggestion.Entry entry : termSuggestion.getEntries()) {
            for (CompletionSuggestion.Entry.Option option : entry) {
                Map<String, Object> val = option.getHit().getSourceAsMap();

                //		        	Employment emp  =(Employment)val.get("employments");

                //		        	System.out.println("\n\n\n\n\n"+val.get("employments")+"\n\n\n\n\n");
                SkillsSuggestionEntity val1 = new SkillsSuggestionEntity();

                val1.setId(option.getHit().getId());
                //		        	val1.setDesignation(emp.getJobTitle());
                //		        	val1.setId(option.getHit().getId());
                val1.setSkillSuggestion(option.getText().string());
                //
                //		        	val1.setSubCategory((String)val.get("subCategory"));
                //		        	val1.setSuggest((ArrayList<String>)val.get("suggest"));
                ans.add(val1);
            }
        }

        return ans;
    }

    @PostMapping("/searchByDesignationLocationAndCategorySubAndSkill/")
    public ArrayList<ElasticWorker> searchByDesignationLocationAndCategorySubAndSkill(@RequestBody Map<String, String> filters) {
        //    	System.out.println("\n\n\n\n\n\n"+filters+"\n\n\n\n\n\n");

        return workerRepo.searchByDesignationLocationAndCategorySubAndSkill(
            filters.get("designation"),
            filters.get("location"),
            filters.get("category"),
            filters.get("subcategory"),
            filters.get("skill")
        );
    }

    @GetMapping("/searchByDesignationLocationAndSkill/{designation}/{location}/{skill}")
    public ArrayList<ElasticWorker> searchByDesignationLocationAndSkill(
        @PathVariable("designation") String designation,
        @PathVariable("location") String location,
        @PathVariable("skill") String skill
    ) {
        return workerRepo.searchByDesignationLocationAndSkill(designation, location, skill);
    }

    @GetMapping("/searchByDesignationLocationAndCategoryAndSkill/{designation}/{location}/{Category}/{skill}")
    public ArrayList<ElasticWorker> searchByDesignationLocationAndCategoryAndSkill(
        @PathVariable("designation") String designation,
        @PathVariable("location") String location,
        @PathVariable("skill") String skill,
        @PathVariable("Category") String Category
    ) {
        return workerRepo.searchByDesignationLocationAndCategoryAndSkill(designation, location, Category, skill);
    }
}
