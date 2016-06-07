package com.trendiguru.infra;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendiguru.config.ConfigManager;

public class Kibana {
	
	private ConfigManager configManager = ConfigManager.getInstance();
	private static Logger log = Logger.getLogger(Kibana.class);
	
	public String read(String uri, String method, String postBody) {

		CloseableHttpClient httpclient = HttpClients.createDefault();
    	
    	HttpUriRequest request = null;
    	if (method.equals("POST")) {
    		log.info("http://" + configManager.getKibanaDomain() + "/" + uri);
    		request = new HttpPost("http://" + configManager.getKibanaDomain() + "/" + uri);
    		request.setHeader("Accept", "text/plain, */*; q=0.01");
    		request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    		request.setHeader("kbn-version", "4.5.0");

    		try {
				((HttpPost)request).setEntity(new StringEntity(postBody));
				//log.info(postBody);
				//log.info("post size: " + postBody.length());
			} catch (UnsupportedEncodingException e) {
				log.fatal(e);
				e.printStackTrace();
			}
    	} else {
    		log.info("http://" + configManager.getKibanaDomain() + "/" + uri);
    		//System.out.println("url: http://localhost:9000/" + uri);
    		request = new HttpGet("http://" + configManager.getKibanaDomain() + "/" + uri);
    		request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        	
    	}

    	// add header
    	request.setHeader("Authorization", "Basic a2liYW5hYWRtaW46ZXhhbXBsZXVzZXI=");
    	request.setHeader("Connection", "keep-alive");
    	request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
    	
    	CloseableHttpResponse response1 =null;
    	String resultBody = null;
    	
    	try {
			
    		response1 = httpclient.execute(request);
    			
    		    //System.out.println(response1.getStatusLine());
    		    if (response1.getStatusLine().getStatusCode() == 200) {
    		    	//log.info(response1.getStatusLine());
    		    	//InputStream is = entity1.getContent();
        		    resultBody = EntityUtils.toString(response1.getEntity());
        		    //log.info(resultBody);
        		    // do something useful with the response body
        		    // and ensure it is fully consumed
        		    HttpEntity entity1 = response1.getEntity();
        		    EntityUtils.consume(entity1);
    		    } else {
    		    	log.fatal(response1);
    		    	System.out.println(response1);
    		    }
    		    
    		    //log.info("------------------------------------");
    		    //System.out.println("------------------------------------");
    		    
    		    //EntityUtils.consume(entity1);
    		
		} catch (UnsupportedEncodingException e) {
			log.fatal(e);
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			log.fatal(e);
			e.printStackTrace();
		} catch (IOException e) {
			log.fatal(e);
			e.printStackTrace();
		}
    	finally {
		    try {
				response1.close();
			} catch (IOException e) {
				log.fatal(e);
				e.printStackTrace();
			}
		}
    	return resultBody;
    }
	
	public int parseEventsJson(String eventsJson) {
		int total = 0;
		JsonFactory jsonFactory = JsonFactory.getInstance();
		ObjectMapper mapper = jsonFactory.getMapper();
		try {
			JsonNode rootJson = mapper.readTree(eventsJson);
			total = rootJson.get("hits").get("total").intValue();
			if (total == 0) {
				log.info("No events to parse!");
				//return false;
			} else {
				//log.info("total results:" + total);
				for (int elem = 0; elem < total; elem++) {
					JsonNode event = rootJson.get("hits").get("hits").get(elem);
					
					//eg decoded clickUrl = https://api.shopstyle.com/action/apiVisitRetailer?id=522812989&pid=uid900-25284470-95
					String clickUrl = event.get("_source").get("clickUrl").textValue();
					log.info(clickUrl);
					
					//When is each report from each store available - yesterday?
					
					//TODO extract shop, item id for YESTERDAY
					
					
					
					
				}
				//return true;
			}			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//return false;
		return total;
	}
}
