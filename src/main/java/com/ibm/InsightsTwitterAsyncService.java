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
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;

import com.ibm.json.java.JSONObject;
import com.ibm.twitter.TweetMessage;
import com.ibm.twitter.InsightsTwitter;
import com.ibm.watson.WatsonTranslate;

public class InsightsTwitterAsyncService implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(InsightsTwitterAsyncService.class);
	private AsyncContext ac;

	public InsightsTwitterAsyncService(AsyncContext context) {
		this.ac = context;
	}


	private String translate(WatsonTranslate watson, String message) {
    	return watson.translate(message);
    }


	@Override
	  public void run() {
		String term = ac.getRequest().getParameter("condition");
		String location = ac.getRequest().getParameter("location");
		
		boolean translate = Boolean.parseBoolean(ac.getRequest().getParameter("enable"));

		PrintWriter writer = null; 
		
		try {
			writer = ac.getResponse().getWriter();
		}
		catch(IOException e) {
			logger.error("could not write SSE {}", e.getMessage());
		}
		
		Locale locale = ac.getRequest().getLocale();
		WatsonTranslate watson = new WatsonTranslate(locale);
		
		if(!term.equals("") && !location.equals("")) {
			logger.debug("Requested condition {} and location {}", term, location);

			InsightsTwitter twitter = new InsightsTwitter();
			ArrayList<TweetMessage> tweetMessages = twitter.getTweetList(term, location).getTweetList();

			logger.debug("Current tweets {}", tweetMessages.toString());

			try {
				for(TweetMessage tweetMessage : tweetMessages) {
					JSONObject json = new JSONObject();
					JSONObject tweet = new JSONObject();
					// We need to put the tweet and link into an inner object
					// so that we can use a special formatter in bootstrap table

					json.put("screenName", tweetMessage.getMessage().getActor().getUserName());

					if(translate) {
						tweet.put("message", translate(watson, tweetMessage.getMessage().getBody()));
					}
					else {
						tweet.put("message", tweetMessage.getMessage().getBody());
					}

					tweet.put("link", tweetMessage.getMessage().getLink());

					json.put("tweet", tweet);

					writer.write(("data: " + json.toString() + "\n\n"));
					writer.flush();
				}
			}
			
			catch(NullPointerException e) {
				logger.error("Exception Twitter Async Service {}", e.getMessage());
			}
		}
		
		writer.write(("event: finished\n"));
		writer.write(("data: \n\n"));
		writer.flush();
		writer.close();
		ac.complete();	
	}

}
