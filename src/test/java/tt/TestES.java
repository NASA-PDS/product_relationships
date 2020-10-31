package tt;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.ToXContentObject;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import gov.nasa.pds.rel.es.client.EsClientFactory;
import gov.nasa.pds.rel.out.RDFTurtleWriter;


public class TestES
{

    public static void main(String[] args) throws Exception
    {
        test1();
        //testQuery();
    }
    
    
    public static void testQuery() throws Exception
    {
        SearchSourceBuilder src = createTermFilter("lidvid", "urn:nasa:pds:orex.spice::8.0");
        prettyPrint(src);
    }


    public static SearchSourceBuilder createTermFilter(String field, String value) throws Exception
    {
        SearchSourceBuilder src = new SearchSourceBuilder();
        
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.matchAllQuery());
        query.filter(QueryBuilders.termQuery(field, value));
        src.query(query);
        
        return src;
    }
    
    
    private static void prettyPrint(ToXContentObject obj) throws IOException
    {
        XContentBuilder pretty = XContentFactory.contentBuilder(XContentType.JSON).prettyPrint();
        String json = BytesReference.bytes(obj.toXContent(pretty, ToXContent.EMPTY_PARAMS)).utf8ToString();
        System.out.println(json);
        
    }
    
    
    public static void test1() throws Exception
    {
        EsClientFactory fac = new EsClientFactory("http://localhost");
        RestHighLevelClient client = fac.createHighLevelClient();
        
        SearchRequest searchRequest = new SearchRequest("registry"); 
        SearchSourceBuilder src = createTermFilter("lidvid", "urn:nasa:pds:orex.spice::8.0");
        searchRequest.source(src);
        
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();
        
        //System.out.println("Size = " + hits.length);
        
        SearchHit hit = hits[0];
        
        Map<String, Object> fields = hit.getSourceAsMap();
        
        toRDF(fields);
        
        client.close();
    }

    
    private static void toRDF(Map<String, Object> fields) throws Exception
    {
        RDFTurtleWriter writer = new RDFTurtleWriter(new File("/tmp/test.ttl"));
        
        String prodClass = fields.get("product_class").toString();
        String lidvid = fields.get("lidvid").toString();
        String lid = fields.get("lid").toString();
        String vid = fields.get("vid").toString();
        String title = fields.get("title").toString();

        writer.startRecord(lidvid);
        writer.writeLiteral("pds:product_class", prodClass);
        writer.writeLiteral("pds:title", title);
        writer.writeLiteral("pds:lid", lid);
        writer.writeLiteral("pds:vid", vid, "xsd:float");
                
        Object obj = fields.get("pds/Internal_Reference/pds/lid_reference");
        if(obj != null)
        {
            List list = (List)obj;
            for(Object item: list)
            {
                writer.writeIRI("pds:lid_ref", "<" + item.toString() + ">");
            }
        }

        obj = fields.get("pds/Internal_Reference/pds/lidvid_reference");
        if(obj != null)
        {
            List list = (List)obj;
            for(Object item: list)
            {
                writer.writeIRI("pds:lidvid_ref", "<" + item.toString() + ">");
            }
        }

        obj = fields.get("pds/Bundle_Member_Entry/pds/lidvid_reference");
        if(obj != null)
        {
            List list = (List)obj;
            for(Object item: list)
            {
                writer.writeIRI("pds:lidvid_ref", "<" + item.toString() + ">");
            }
        }

        
        writer.endRecord();
        
        writer.close();

    }
    
    
}
