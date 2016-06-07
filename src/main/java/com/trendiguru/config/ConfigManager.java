package com.trendiguru.config;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.trendiguru.infra.JsonFactory;

public class ConfigManager {
	private static Logger log = Logger.getLogger(ConfigManager.class);
	private static ConfigManager INSTANCE = new ConfigManager();
	private JsonNode configJson;
	private String env = System.getProperty("env");
	
	private ConfigManager() {
		//String env = System.getProperty("env");
		
		JsonFactory jsonFactory = JsonFactory.getInstance();
		ObjectMapper mapper = jsonFactory.getMapper();
		//mapper.readValue
		try {
			URL url = this.getClass().getResource("/com/trendiguru/config/" + env + "Config.json");
			configJson = mapper.readTree(new File(url.toURI()));
		} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ConfigManager getInstance() {
		return INSTANCE;
	}
	
	public String getMongoDomain() {
		JsonNode n = configJson.get("mongo").get("domain");
		return validateParam(n);
		
		//return configJson.get("mongo").get("domain").textValue();
	}
	
	public String getKibanaDomain() {
		JsonNode n =  configJson.get("kibana").get("domain");
		return validateParam(n);
	}

	public String getEnv() {
		return env;
	}
	
	public boolean isLocal() {
		return env.equals("local") ? true : false;
	}

	public ArrayNode getEmails() {
		ArrayNode n = (ArrayNode)configJson.get("emails");
		//ArrayNode slaidsNode = (ArrayNode)
		return n;
	}
	
	private String validateParam(JsonNode value) {
		if (value == null) {
			log.fatal("json param missing from " + env + "Config.json");
			return null;
		} else {
			return value.textValue();
		}
	}
}
