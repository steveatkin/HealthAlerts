/*
The MIT License (MIT)

Copyright (c) 2015 IBM

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.ibm;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;

import com.ibm.json.java.JSONObject;
import com.ibm.twitter.Sentiment;
import com.ibm.twitter.InsightsTwitter;

public class SentimentAsyncService implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(SentimentAsyncService.class);
	private AsyncContext ac;

	public SentimentAsyncService(AsyncContext context) {
		this.ac = context;
	}

	@Override
	  public void run() {
		String terms = ac.getRequest().getParameter("conditions");
		String location = ac.getRequest().getParameter("location");
		
		long positiveCount = 0;
		long neutralCount = 0;
		long negativeCount = 0;
		
		ArrayList<String> conditions = new ArrayList<String>(Arrays.asList(terms.split(",")));

		PrintWriter writer = null; 
		
		try {
			writer = ac.getResponse().getWriter();
		}
		catch(IOException e) {
			logger.error("could not write SSE {}", e.getMessage());
		}
		
		for(String condition : conditions) {
			logger.debug("Requested condition {} and location {}", condition, location);
			InsightsTwitter twitter = new InsightsTwitter();
			
			Sentiment sentiment = twitter.getSentimentCount(condition, location, "positive");
			logger.debug("Positive sentiment {}", sentiment.getCount());
			positiveCount += sentiment.getCount();
			
			sentiment = twitter.getSentimentCount(condition, location, "negative");
			logger.debug("Negative sentiment {}", sentiment.getCount());
			negativeCount += sentiment.getCount();
			
			sentiment = twitter.getSentimentCount(condition, location, "neutral");
			logger.debug("Neutral sentiment {}", sentiment.getCount());
			neutralCount += sentiment.getCount();
		}
		
		try {
			
			JSONObject json = new JSONObject();
	
			json.put("sentiment", "positive");
			json.put("count", positiveCount);
			writer.write(("data: " + json.toString() + "\n\n"));
			writer.flush();

			json.put("sentiment", "negative");
			json.put("count", negativeCount);
			writer.write(("data: " + json.toString() + "\n\n"));
			writer.flush();
			
			json.put("sentiment", "neutral");
			json.put("count", neutralCount);
			writer.write(("data: " + json.toString() + "\n\n"));
			writer.flush();

			writer.write(("event: finished\n"));
			writer.write(("data: \n\n"));
			writer.flush();
			writer.close();
		}
		catch(NullPointerException e) {
			logger.error("Exception Sentiment Async Service {}", e.getMessage());
		}
		finally {
			ac.complete();
		}
	}

}
