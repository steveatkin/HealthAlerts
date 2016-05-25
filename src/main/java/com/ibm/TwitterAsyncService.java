package com.ibm;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.json.java.JSONObject;
import com.ibm.watson.WatsonTranslate;

import twitter4j.*;

import javax.servlet.AsyncContext;

public class TwitterAsyncService implements Runnable{

	private AsyncContext ac;
	private static final Logger logger = LoggerFactory.getLogger(TwitterAsyncService.class);

	public TwitterAsyncService(AsyncContext context) {
		this.ac = context;
	}
	
	@Override
	  public void run() {
	    PrintWriter writer = null;
	    String term = ac.getRequest().getParameter("condition");
		String location = ac.getRequest().getParameter("location");
		boolean translate = Boolean.parseBoolean(ac.getRequest().getParameter("enable"));
		Locale locale = ac.getRequest().getLocale();
		
		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
		WatsonTranslate watson = new WatsonTranslate(locale);
			
	    try {
	    	writer = ac.getResponse().getWriter();
	    }
	    catch (IOException e) {
	    	logger.error("could not write SSE {}", e.getMessage());
	    }

	    if(!term.equals("") && !location.equals("")) {
	    	logger.info("Requested condition {} and location {}", term, location);
	    	Query query = new Query(term + " " + location);
	    	query.setResultType(Query.RECENT);
	    	
	    	Twitter twitter = TwitterFactory.getSingleton();
	    	
	    	try {
	    		// Just get the first page of results to avoid exceeding the Twitter rate limit
	    		QueryResult result = twitter.search(query);
	        	
	    		List<Status> tweets = result.getTweets();
	        	
	    		logger.debug("Current tweets {}", tweets.toString());
	        	
	    		for (Status tweetMessage : tweets) {
	    			JSONObject json = new JSONObject();
	    			JSONObject tweet = new JSONObject();
	        
	    			json.put("screenName", tweetMessage.getUser().getScreenName());
	        		
	    			if(translate) {
	    				String message = watson.translate(tweetMessage.getText());
	    				tweet.put("message", message);
	    			}
	    			else {
	    				tweet.put("message", tweetMessage.getText());
	    			}
	        		
	    			tweet.put("date", dateFormatter.format(tweetMessage.getCreatedAt()));
	    			json.put("tweet", tweet);
	        			
	    			writer.write("data: " + json.toString() + "\n\n");
	    			writer.flush();
	    		}
	    	}
	    	catch(TwitterException e) {
	    		logger.error("Twitter Error {}",e.getMessage());
	    	}
	    }
	   
	    writer.write("event: finished\n");
		writer.write("data: \n\n");
		writer.flush();
		writer.close();
		ac.complete();
	}
}
