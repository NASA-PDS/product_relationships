package tt;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import gov.nasa.pds.rel.es.client.EsClientFactory;


public class TestES
{

    public static void main(String[] args) throws Exception
    {
        EsClientFactory fac = new EsClientFactory("http://localhost");
        RestHighLevelClient client = fac.createHighLevelClient();
        
        SearchRequest searchRequest = new SearchRequest("registry"); 
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
        searchSourceBuilder.query(QueryBuilders.matchAllQuery()); 
        searchRequest.source(searchSourceBuilder);
        
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(searchResponse.getHits().getTotalHits().value);
        
        client.close();
    }

}
