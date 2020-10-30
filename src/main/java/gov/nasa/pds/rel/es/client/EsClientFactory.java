package gov.nasa.pds.rel.es.client;

import java.util.Properties;

import org.apache.http.HttpHost;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import gov.nasa.pds.rel.util.PropUtils;


public class EsClientFactory
{
    private static final String AUTH_TRUST_SELF_SIGNED = "trust.self-signed";
    
    private ClientConfigCB clientCB;
    private RequestConfigCB reqCB;
    private HttpHost host;
    
    
    public EsClientFactory(String esUrl) throws Exception
    {
        host = EsUtils.parseEsUrl(esUrl);
        clientCB = new ClientConfigCB();
        reqCB = new RequestConfigCB();
    }


    public RestHighLevelClient createHighLevelClient()
    {
        RestClientBuilder bld = RestClient.builder(host);
        bld.setHttpClientConfigCallback(clientCB);
        bld.setRequestConfigCallback(reqCB);
        
        RestHighLevelClient client = new RestHighLevelClient(bld);
        return client;
    }

    
    public void setConnectTimeoutSec(int sec)
    {
        reqCB.setConnectTimeoutSec(sec);
    }

    
    public void setSocketTimeoutSec(int sec)
    {
        reqCB.setSocketTimeoutSec(sec);
    }
    
    
    public void configureAuth(String configFilePath) throws Exception
    {
        if(configFilePath != null)
        {
            Properties props = PropUtils.loadProps(configFilePath);
            configureAuth(props);
        }
    }
    
    
    private void configureAuth(Properties props) throws Exception
    {
        if(props == null) return;

        // Trust self-signed certificates
        if(Boolean.TRUE.equals(PropUtils.getBoolean(props, AUTH_TRUST_SELF_SIGNED)))
        {
            clientCB.setTrustSelfSignedCert(true);
        }
        
        // Basic authentication
        String user = props.getProperty("user");
        String pass = props.getProperty("password");
        if(user != null && pass != null)
        {
            clientCB.setUserPass(user, pass);
        }
    }

}
