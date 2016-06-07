package com.trendiguru.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.trendiguru.entities.RevenueProcessed;
import com.trendiguru.infra.Kibana;
import com.trendiguru.mongodb.MorphiaManager;

/**
 * Run X times a day
 * Connect to Kibana Sense
 * Download Ebay revenue report for date YYYY-MM-DD
 * Fetch all log entries with event = "Result Clicked" for the same date
 * Calculate revenue per click
 * write event to ES 
 * 
 * @author Jeremy
 *
 */
public class ToolManager {
	
	private static Logger log = Logger.getLogger(ToolManager.class);
	
	public static void main(String[] args) {
		
		String postBody = "{\r\n  \"query\": {\r\n    \"match_phrase\": {\r\n      \"event\": \"Result%20Clicked\"\r\n    }\r\n  }\r\n}";
		
		String yesterday = "2016.05.23";
		int pageSize = 10;
		int totalFetchedEvents = 0;
		Kibana k = new Kibana();
		boolean tryAgain = true;
		int from = 0;
		
		//1. fetch events from ElasticSearch
		while (tryAgain) {
			log.info("Fetching " + pageSize + " events from page: " + from);
	        String logEntries = k.read("api/sense/proxy?uri=http%3A%2F%2Flocalhost%3A9200%2Flogstash-" + yesterday + "%2F_search%3Fsize%3D" + pageSize + "&from%3D" + from, "POST", postBody);
	        //log.info(logEntries);
	        from += pageSize;
	        int numFetchedEvents = k.parseEventsJson(logEntries);
	        totalFetchedEvents += numFetchedEvents;
	        if (numFetchedEvents == 0 || numFetchedEvents < pageSize) {
	        	tryAgain = false;
	        } 
		}
		log.info("Fetched a total of " + totalFetchedEvents + " events");
		
		//2. parse json events for the clicked store and item id
				
		//3. find revenue for given item from store's daily report
		
		//4. insert "parsed_response_click" event into ElasticSearch
		
		//5. store in MongoDB that this date was parsed so it's not parsed again!
		Date yesterdayDate = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
		    yesterdayDate = formatter.parse(yesterday);
		} catch (ParseException e) {
			log.fatal(e);
		    e.printStackTrace();
		}
		RevenueProcessed revenue = new RevenueProcessed(yesterdayDate);
		MorphiaManager morphiaManager = MorphiaManager.getInstance();
		morphiaManager.insertRevenueProcessed(revenue);
        
    }
	
	public ToolManager() {
		
	}
	
	
}
