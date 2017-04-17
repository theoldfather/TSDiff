package com.oldfather.alfred;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.glassfish.jersey.client.ClientProperties.PROXY_URI;


/**
 * Created by theoldfather on 4/8/17.
 */
public class Query {
    public final String fred_uri = "https://api.stlouisfed.org/fred";
    WebTarget fred;
    Invocation.Builder fred_http;
    String apiKey;
    String fileType;
    List<String> paths;
    HashMap<String,Object> queryParams = new HashMap<>(10);

    private Query(String apiKey, String fileType, List<String> paths, HashMap<String,Object> queryParams){
        this.apiKey = apiKey;
        this.fileType = fileType;
        this.paths = paths;
        this.queryParams = queryParams;
    }

    public static class QueryBuilder{
        private String _api_key;
        private String _fileType;
        private List<String> _paths = new ArrayList<>();
        private HashMap<String,Object> _queryParams = new HashMap<>(10);

        public QueryBuilder(){

        }

        public QueryBuilder(String api_key, String fileType){
            this.setApiKey(api_key);
            this.setFileType(fileType);
        }

        public QueryBuilder setApiKey(String api_key){
            this._api_key = api_key;
            this.addQueryParam("api_key",api_key);
            return this;
        }

        public QueryBuilder setFileType(String fileType){
            this._fileType = fileType;
            this.addQueryParam("file_type", fileType);
            return this;
        }

        public QueryBuilder addPath(String path){
            this._paths.add(path);
            return this;
        }

        public QueryBuilder addQueryParam(String arg, String value){
            this._queryParams.put(arg,value);
            return this;
        }

        public QueryBuilder addQueryParam(String arg, Integer value){
            this._queryParams.put(arg,value);
            return this;
        }

        public Query createQuery(){
            return new Query(_api_key,_fileType, _paths, _queryParams);
        }
    }

    private void makeWebTarget(){
        ClientConfig config = new ClientConfig();
        config.register(JacksonJaxbJsonProvider.class);
        Client client = ClientBuilder.newClient(config);
        WebTarget fred = client.target(fred_uri);

        // add resources deeper that the root uri
        for(String path: this.paths){
            fred = fred.path(path);
        }

        // add query parameters for the get request
        for(Map.Entry param: queryParams.entrySet()){
            if(param.getKey().toString().compareTo("release_id")==0){
                fred = fred.queryParam(param.getKey().toString(),(Integer) param.getValue());
            }else{
                fred = fred.queryParam(param.getKey().toString(),param.getValue().toString());
            }
        }
        this.fred = fred;
    }

    private void buildInvocation(){
        this.fred_http = fred.request(MediaType.TEXT_PLAIN_TYPE);
        //fred_http.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
    }

    public Response execute(){
        this.makeWebTarget();
        this.buildInvocation();
        Response res = fred_http.get();
        return res;
    }

}
