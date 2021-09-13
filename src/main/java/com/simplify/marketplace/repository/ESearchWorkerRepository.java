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
        "{\"bool\": {\n" +
        "      \"should\": [\n" +
        "        \n" +
        "            {\n" +
        "           \n" +
        "          \"query_string\": {\n" +
        "            \"query\": \"?0\",\n" +
        "            \"boost\": 1, \n" +
        "            \"default_operator\": \"OR\"\n" +
        "             \n" +
        "          }\n" +
        "            }, {\n" +
        "          \n" +
        "            \"query_string\": {\n" +
        "            \"query\": \"?0\",\n" +
        "            \"boost\": 10, \n" +
        "            \"default_operator\": \"AND\"\n" +
        "             \n" +
        "          }},\n" +
        "          {\"multi_match\": {\n" +
        "            \"query\": \"?0\",\n" +
        "            \"type\": \"phrase\",\n" +
        "            \"boost\": 100, \n" +
        "            \"fields\": [\"name\",\"description\"]\n" +
        "          }}\n" +
        "            \n" +
        "          \n" +
        "          \n" +
        "            \n" +
        "          \n" +
        "        \n" +
        "      ]\n" +
        "    }}"
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
}
