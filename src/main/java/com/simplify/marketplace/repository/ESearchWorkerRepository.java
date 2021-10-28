package com.simplify.marketplace.repository;

import com.simplify.marketplace.domain.ElasticWorker;
import java.util.ArrayList;
//import java.util.Map;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

public interface ESearchWorkerRepository extends ElasticsearchRepository<ElasticWorker, String> {
    @Query("{\"match_all\":{}}")
    public ArrayList<ElasticWorker> matchAll();

    @Query(
        "{\r\n" +
        "    \"bool\": {\r\n" +
        "      \"should\": [\r\n" +
        "        {\r\n" +
        "          \"multi_match\": {\r\n" +
        "            \"query\": \"?0\",\r\n" +
        "            \"type\": \"phrase\",\r\n" +
        "            \"boost\": 100, \r\n" +
        "            \"fields\": [\"*\"]\r\n" +
        "          }}\r\n" +
        "          ,\r\n" +
        "          {\r\n" +
        "          \r\n" +
        "            \"query_string\": {\r\n" +
        "            \"query\": \"?0\",\r\n" +
        "            \"boost\": 10, \r\n" +
        "            \"default_operator\": \"AND\"\r\n" +
        "             \r\n" +
        "          }\r\n" +
        "            \r\n" +
        "          \r\n" +
        "          \r\n" +
        "            } ,\r\n" +
        "        \r\n" +
        "            {\r\n" +
        "           \r\n" +
        "          \"query_string\": {\r\n" +
        "            \"query\": \"?0\",\r\n" +
        "            \"boost\": 1, \r\n" +
        "            \"default_operator\": \"OR\"\r\n" +
        "             \r\n" +
        "          }\r\n" +
        "            }\r\n" +
        "          \r\n" +
        "          \r\n" +
        "        \r\n" +
        "      ]\r\n" +
        "    }\r\n" +
        "  }"
    )
    public ArrayList<ElasticWorker> searchQuery(@Param("str") String parameter);

    @Query("{\r\n" + "   \"match\":{\r\n" + "     \"jobPreferences.engagementType\":\"?0\"\r\n" + "     \r\n" + "   }\r\n" + "  }")
    public ArrayList<ElasticWorker> searchByEngagementType(String str);

    @Query("{\r\n" + "   \"match\":{\r\n" + "     \"jobPreferences.employmentType\":\"?0\"\r\n" + "     \r\n" + "   }\r\n" + "  }")
    public ArrayList<ElasticWorker> searchByEmploymentType(String str);

    @Query("{\r\n" + "   \"match\":{\r\n" + "     \"jobPreferences.locationType\":\"?0\"\r\n" + "     \r\n" + "   }\r\n" + "  }")
    public ArrayList<ElasticWorker> searchBylocationType(String str);

    @Query(
        "{\r\n" +
        "    \"bool\": {\r\n" +
        "      \"must\": [\r\n" +
        "        {\r\n" +
        "          \"match\": {\r\n" +
        "            \"Category\": \"?0\"\r\n" +
        "          }\r\n" +
        "        }\r\n" +
        "      ]\r\n" +
        "      , \"should\": [\r\n" +
        "        {\r\n" +
        "          \r\n" +
        "         \"bool\": {\r\n" +
        "           \"must\": [\r\n" +
        "             {\r\n" +
        "               \"match\": {\r\n" +
        "                 \"jobPreferences.subCategory.name\": \"?1\"\r\n" +
        "               }\r\n" +
        "             }\r\n" +
        "             ,\r\n" +
        "             {\r\n" +
        "               \r\n" +
        "                \"match\": {\r\n" +
        "                 \"jobPreferences.locationPrefrences.pincode\": \"?2\"\r\n" +
        "               }\r\n" +
        "             },\r\n" +
        "             {\r\n" +
        "               \"bool\": {\r\n" +
        "      \"should\": [\r\n" +
        "        {\r\n" +
        "          \"multi_match\": {\r\n" +
        "            \"query\": \"?3\",\r\n" +
        "            \"type\": \"phrase\",\r\n" +
        "            \"boost\": 100, \r\n" +
        "            \"fields\": [\"*\"]\r\n" +
        "          }}\r\n" +
        "          ,\r\n" +
        "          {\r\n" +
        "          \r\n" +
        "            \"query_string\": {\r\n" +
        "            \"query\": \"?3\",\r\n" +
        "            \"boost\": 10, \r\n" +
        "            \"default_operator\": \"AND\"\r\n" +
        "             \r\n" +
        "          }\r\n" +
        "            \r\n" +
        "          \r\n" +
        "          \r\n" +
        "            } ,\r\n" +
        "        \r\n" +
        "            {\r\n" +
        "           \r\n" +
        "          \"query_string\": {\r\n" +
        "            \"query\": \"?3\",\r\n" +
        "            \"boost\": 1, \r\n" +
        "            \"default_operator\": \"OR\"\r\n" +
        "             \r\n" +
        "          }\r\n" +
        "            }\r\n" +
        "          \r\n" +
        "          \r\n" +
        "        \r\n" +
        "      ]\r\n" +
        "    }\r\n" +
        "             }\r\n" +
        "           ]\r\n" +
        "         }\r\n" +
        "          \r\n" +
        "          \r\n" +
        "        }\r\n" +
        "      ]\r\n" +
        "    }\r\n" +
        "  }"
    )
    public ArrayList<ElasticWorker> allfiltersOr(String Category, String SubCategory, String location, String searchText);

    @Query(
        "{\r\n" +
        "   \"bool\": {\r\n" +
        "     \"must\": [\r\n" +
        "       {\r\n" +
        "          \"match\": {\r\n" +
        "            \"Category\": \"?0\"\r\n" +
        "          }\r\n" +
        "       },\r\n" +
        "       {\r\n" +
        "         \"match\": {\r\n" +
        "                 \"jobPreferences.subCategory.name\": \"?1\"\r\n" +
        "               }\r\n" +
        "       }\r\n" +
        "       ,\r\n" +
        "       {\r\n" +
        "        \"bool\": {\r\n" +
        "      \"should\": [\r\n" +
        "        {\r\n" +
        "          \"multi_match\": {\r\n" +
        "            \"query\": \"?3\",\r\n" +
        "            \"type\": \"phrase\",\r\n" +
        "            \"boost\": 100, \r\n" +
        "            \"fields\": [\"*\"]\r\n" +
        "          }}\r\n" +
        "          ,\r\n" +
        "          {\r\n" +
        "          \r\n" +
        "            \"query_string\": {\r\n" +
        "            \"query\": \"?3\",\r\n" +
        "            \"boost\": 10, \r\n" +
        "            \"default_operator\": \"AND\"\r\n" +
        "             \r\n" +
        "          }\r\n" +
        "            \r\n" +
        "          \r\n" +
        "          \r\n" +
        "            } ,\r\n" +
        "        \r\n" +
        "            {\r\n" +
        "           \r\n" +
        "          \"query_string\": {\r\n" +
        "            \"query\": \"?3\",\r\n" +
        "            \"boost\": 1, \r\n" +
        "            \"default_operator\": \"OR\"\r\n" +
        "             \r\n" +
        "          }\r\n" +
        "            }\r\n" +
        "          \r\n" +
        "          \r\n" +
        "        \r\n" +
        "      ]\r\n" +
        "    }\r\n" +
        "       }\r\n" +
        "       ,\r\n" +
        "       {\r\n" +
        "         \"match\": {\r\n" +
        "                 \"jobPreferences.locationPrefrences.city\": \"?2\"\r\n" +
        "               }\r\n" +
        "       }\r\n" +
        "     ]\r\n" +
        "   }\r\n" +
        "  }"
    )
    public ArrayList<ElasticWorker> allfilters(String Category, String SubCategory, String location, String searchText);

    @Query(
        "{\r\n" +
        "   \"bool\": {\r\n" +
        "     \"must\": [\r\n" +
        "       {\r\n" +
        "          \"match\": {\r\n" +
        "            \"Category\": \"?0\"\r\n" +
        "          }\r\n" +
        "       }\r\n" +
        "       \r\n" +
        "       ,\r\n" +
        "       {\r\n" +
        "        \"bool\": {\r\n" +
        "      \"should\": [\r\n" +
        "        {\r\n" +
        "          \"multi_match\": {\r\n" +
        "            \"query\": \"?1\",\r\n" +
        "            \"type\": \"phrase\",\r\n" +
        "            \"boost\": 100, \r\n" +
        "            \"fields\": [\"*\"]\r\n" +
        "          }}\r\n" +
        "          ,\r\n" +
        "          {\r\n" +
        "          \r\n" +
        "            \"query_string\": {\r\n" +
        "            \"query\": \"?1\",\r\n" +
        "            \"boost\": 10, \r\n" +
        "            \"default_operator\": \"AND\"\r\n" +
        "             \r\n" +
        "          }\r\n" +
        "            \r\n" +
        "          \r\n" +
        "          \r\n" +
        "            } ,\r\n" +
        "        \r\n" +
        "            {\r\n" +
        "           \r\n" +
        "          \"query_string\": {\r\n" +
        "            \"query\": \"?1\",\r\n" +
        "            \"boost\": 1, \r\n" +
        "            \"default_operator\": \"OR\"\r\n" +
        "             \r\n" +
        "          }\r\n" +
        "            }\r\n" +
        "          \r\n" +
        "          \r\n" +
        "        \r\n" +
        "      ]\r\n" +
        "    }\r\n" +
        "       }\r\n" +
        "       \r\n" +
        "     ]\r\n" +
        "   }\r\n" +
        "  }"
    )
    public ArrayList<ElasticWorker> searchByCategoryText(String Category, String searchText);

    @Query(
        "{\r\n" +
        "   \"bool\": {\r\n" +
        "     \"must\": [\r\n" +
        "       {\r\n" +
        "          \"match\": {\r\n" +
        "            \"Category\": \"?0\"\r\n" +
        "          }\r\n" +
        "       },\r\n" +
        "       {\r\n" +
        "         \"match\": {\r\n" +
        "                 \"jobPreferences.subCategory.name\": \"?1\"\r\n" +
        "               }\r\n" +
        "       }\r\n" +
        "       ,\r\n" +
        "       {\r\n" +
        "        \"bool\": {\r\n" +
        "      \"should\": [\r\n" +
        "        {\r\n" +
        "          \"multi_match\": {\r\n" +
        "            \"query\": \"?2\",\r\n" +
        "            \"type\": \"phrase\",\r\n" +
        "            \"boost\": 100, \r\n" +
        "            \"fields\": [\"*\"]\r\n" +
        "          }}\r\n" +
        "          ,\r\n" +
        "          {\r\n" +
        "          \r\n" +
        "            \"query_string\": {\r\n" +
        "            \"query\": \"?2\",\r\n" +
        "            \"boost\": 10, \r\n" +
        "            \"default_operator\": \"AND\"\r\n" +
        "             \r\n" +
        "          }\r\n" +
        "            \r\n" +
        "          \r\n" +
        "          \r\n" +
        "            } ,\r\n" +
        "        \r\n" +
        "            {\r\n" +
        "           \r\n" +
        "          \"query_string\": {\r\n" +
        "            \"query\": \"?2\",\r\n" +
        "            \"boost\": 1, \r\n" +
        "            \"default_operator\": \"OR\"\r\n" +
        "             \r\n" +
        "          }\r\n" +
        "            }\r\n" +
        "          \r\n" +
        "          \r\n" +
        "        \r\n" +
        "      ]\r\n" +
        "    }\r\n" +
        "       }\r\n" +
        "       \r\n" +
        "     ]\r\n" +
        "   }\r\n" +
        "  }"
    )
    public ArrayList<ElasticWorker> searchByCategorySubText(String Category, String SubCategory, String searchText);

    @Query(
        "{\r\n" +
        "   \"bool\": {\r\n" +
        "     \"must\": [\r\n" +
        "       \r\n" +
        "       {\r\n" +
        "        \"bool\": {\r\n" +
        "      \"should\": [\r\n" +
        "        {\r\n" +
        "          \"multi_match\": {\r\n" +
        "            \"query\": \"?0\",\r\n" +
        "            \"type\": \"phrase\",\r\n" +
        "            \"boost\": 100, \r\n" +
        "            \"fields\": [\"*\"]\r\n" +
        "          }}\r\n" +
        "          ,\r\n" +
        "          {\r\n" +
        "          \r\n" +
        "            \"query_string\": {\r\n" +
        "            \"query\": \"?0\",\r\n" +
        "            \"boost\": 10, \r\n" +
        "            \"default_operator\": \"AND\"\r\n" +
        "             \r\n" +
        "          }\r\n" +
        "            \r\n" +
        "          \r\n" +
        "          \r\n" +
        "            } ,\r\n" +
        "        \r\n" +
        "            {\r\n" +
        "           \r\n" +
        "          \"query_string\": {\r\n" +
        "            \"query\": \"?0\",\r\n" +
        "            \"boost\": 1, \r\n" +
        "            \"default_operator\": \"OR\"\r\n" +
        "             \r\n" +
        "          }\r\n" +
        "            }\r\n" +
        "          \r\n" +
        "          \r\n" +
        "        \r\n" +
        "      ]\r\n" +
        "    }\r\n" +
        "       }\r\n" +
        "       ,\r\n" +
        "       {\r\n" +
        "         \"match\": {\r\n" +
        "                 \"jobPreferences.locationPrefrences.city\": \"?1\"\r\n" +
        "               }\r\n" +
        "       }\r\n" +
        "     ]\r\n" +
        "   }\r\n" +
        "  }"
    )
    public ArrayList<ElasticWorker> searchByLocationText(String searchText, String location);

    @Query(
        "{\r\n" +
        "    \"bool\": {\r\n" +
        "      \"must\": [\r\n" +
        "        {\r\n" +
        "          \"match_phrase\": {\r\n" +
        "            \"employments.jobTitle\": \"?0\"\r\n" +
        "          }\r\n" +
        "        },\r\n" +
        "        {\r\n" +
        "          \"match\": {\r\n" +
        "            \"jobPreferences.locationPrefrences.location.city\": \"?1\"\r\n" +
        "          }\r\n" +
        "        }\r\n" +
        "      ]\r\n" +
        "    }\r\n" +
        "  }"
    )
    public ArrayList<ElasticWorker> searchByDesignationAndLocation(String designation, String location);

    @Query(
        "{\r\n" +
        "    \"bool\": {\r\n" +
        "      \"must\": [\r\n" +
        "        {\r\n" +
        "          \"match_phrase\": {\r\n" +
        "            \"employments.jobTitle\": \"?0\"\r\n" +
        "          }\r\n" +
        "        },\r\n" +
        "        {\r\n" +
        "          \"match\": {\r\n" +
        "            \"jobPreferences.locationPrefrences.location.city\": \"?1\"\r\n" +
        "          }\r\n" +
        "        },\r\n" +
        "        {\r\n" +
        "          \"match\": {\r\n" +
        "            \"Category\": \"?2\"\r\n" +
        "          }\r\n" +
        "        }\r\n" +
        "      ]\r\n" +
        "    }\r\n" +
        "  }"
    )
    public ArrayList<ElasticWorker> searchByDesignationAndLocationAndCategory(String designation, String location, String Category);

    @Query(
        "{\r\n" +
        "    \"bool\": {\r\n" +
        "      \"must\": [\r\n" +
        "        {\r\n" +
        "          \"match_phrase\": {\r\n" +
        "            \"employments.jobTitle\": \"?0\"\r\n" +
        "          }\r\n" +
        "        },\r\n" +
        "        {\r\n" +
        "          \"match\": {\r\n" +
        "            \"jobPreferences.locationPrefrences.location.city\": \"?1\"\r\n" +
        "          }\r\n" +
        "        },\r\n" +
        "        {\r\n" +
        "          \"match\": {\r\n" +
        "            \"Category\": \"?2\"\r\n" +
        "          }\r\n" +
        "        }\r\n" +
        "        ,\r\n" +
        "        {\r\n" +
        "          \"match\": {\r\n" +
        "            \"jobPreferences.subCategory.name\": \"?3\"\r\n" +
        "          }\r\n" +
        "        }\r\n" +
        "      ]\r\n" +
        "    }\r\n" +
        "  }"
    )
    public ArrayList<ElasticWorker> searchByDesignationLocationAndCategorySub(
        String designation,
        String location,
        String Category,
        String sub
    );

    @Query(
        "{\r\n" +
        "    \"bool\": {\r\n" +
        "      \"must\": [\r\n" +
        "        {\r\n" +
        "          \"match_phrase\": {\r\n" +
        "            \"employments.jobTitle\": \"?0\"\r\n" +
        "          }\r\n" +
        "        },\r\n" +
        "        {\r\n" +
        "          \"match_phrase\": {\r\n" +
        "            \"jobPreferences.locationPrefrences.location.city\": \"?1\"\r\n" +
        "          }\r\n" +
        "        },\r\n" +
        "        {\r\n" +
        "          \"match_phrase\": {\r\n" +
        "            \"Category\": \"?2\"\r\n" +
        "          }\r\n" +
        "        }\r\n" +
        "        ,\r\n" +
        "        {\r\n" +
        "          \"match_phrase\": {\r\n" +
        "            \"jobPreferences.subCategory.name\": \"?3\"\r\n" +
        "          }\r\n" +
        "        }\r\n" +
        "        ,\r\n" +
        "        {\r\n" +
        "          \"match_phrase\": {\r\n" +
        "            \"skills.skillName\": \"?4\"\r\n" +
        "          }\r\n" +
        "        }\r\n" +
        "      ]\r\n" +
        "    }\r\n" +
        "  }\r\n" +
        ""
    )
    public ArrayList<ElasticWorker> searchByDesignationLocationAndCategorySubAndSkill(
        String designation,
        String location,
        String Category,
        String sub,
        String Skill
    );

    @Query(
        "{\r\n" +
        "    \"bool\": {\r\n" +
        "      \"must\": [\r\n" +
        "        {\r\n" +
        "          \"match_phrase\": {\r\n" +
        "            \"employments.jobTitle\": \"?0\"\r\n" +
        "          }\r\n" +
        "        },\r\n" +
        "        {\r\n" +
        "          \"match_phrase\": {\r\n" +
        "            \"jobPreferences.locationPrefrences.location.city\": \"?1\"\r\n" +
        "          }\r\n" +
        "        },\r\n" +
        "        \r\n" +
        "        {\r\n" +
        "          \"match_phrase\": {\r\n" +
        "            \"skills.skillName\": \"?2\"\r\n" +
        "          }\r\n" +
        "        }\r\n" +
        "      ]\r\n" +
        "    }\r\n" +
        "  }\r\n" +
        "\r\n" +
        ""
    )
    public ArrayList<ElasticWorker> searchByDesignationLocationAndSkill(String designation, String location, String Skill);

    @Query(
        "{\n" +
        "    \"bool\": {\n" +
        "      \"must\": [\n" +
        "        {\n" +
        "          \"match_phrase\": {\n" +
        "            \"employments.jobTitle\": \"?0\"\n" +
        "          }\n" +
        "        },\n" +
        "        {\n" +
        "          \"match_phrase\": {\n" +
        "            \"jobPreferences.locationPrefrences.location.city\": \"?1\"\n" +
        "          }\n" +
        "        },\n" +
        "        {\n" +
        "          \"match_phrase\": {\n" +
        "            \"Category\": \"?2\"\n" +
        "          }\n" +
        "        }\n" +
        "        \n" +
        "        ,\n" +
        "        {\n" +
        "          \"match_phrase\": {\n" +
        "            \"skills.skillName\": \"?3\"\n" +
        "          }\n" +
        "        }\n" +
        "      ]\n" +
        "    }\n" +
        "  }"
    )
    public ArrayList<ElasticWorker> searchByDesignationLocationAndCategoryAndSkill(
        String designation,
        String location,
        String category,
        String skill
    );
}
