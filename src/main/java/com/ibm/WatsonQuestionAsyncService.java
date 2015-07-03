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
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.AsyncContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.json.java.JSONObject;
import com.ibm.watson.WatsonQuestionAnswer;

public class WatsonQuestionAsyncService implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(WatsonQuestionAsyncService.class);
	private AsyncContext ac;

	public WatsonQuestionAsyncService(AsyncContext context) {
		this.ac = context;
	}

	@Override
	  public void run() {
		String healthCondition = ac.getRequest().getParameter("condition");
		// Use the US English locale, because Watson Question Answer only supports English
		ResourceBundle res = ResourceBundle.getBundle( "com.ibm.health", Locale.US);
		String questionPattern = res.getString("what_is");	
		
		String questionText = MessageFormat.format(questionPattern, healthCondition);
		logger.debug("Requested question {}", questionText);
		WatsonQuestionAnswer watson = new WatsonQuestionAnswer();
		List<Map<String,String>> answers = watson.getAnswers(questionText, "healthcare");
		logger.debug("Identfied answers {}", answers.toString());

		try {
			PrintWriter writer = ac.getResponse().getWriter();
			
			// Just grab the first answer in the list
			if(!answers.isEmpty()) {
				JSONObject json = new JSONObject();
				Map<String, String> answer = answers.get(0);
				json.put("question", questionText);
				json.put("answer", answer.get("text"));
				
				writer.write(("data: " + json.toString() + "\n\n"));
    			writer.flush();
			}
			
			questionPattern = res.getString("how_prevent");
			questionText = MessageFormat.format(questionPattern, healthCondition);
			logger.debug("Requested question {}", questionText);
			answers = watson.getAnswers(questionText, "healthcare");
			logger.debug("Identfied answers {}", answers.toString());
			
			// Just grab the first answer in the list
			if(!answers.isEmpty()) {
				JSONObject json = new JSONObject();
				Map<String, String> answer = answers.get(0);
				json.put("question", questionText);
				json.put("answer", answer.get("text"));
							
				writer.write(("data: " + json.toString() + "\n\n"));
			    writer.flush();
			}
			
			writer.write(("event: finished\n"));
			writer.write(("data: \n\n"));
			writer.flush();
			writer.close();
		}
		catch(IOException e) {
			logger.error("could not write SSE {}", e.getMessage());
		}
		catch(NullPointerException e) {
			logger.error("Exception Watson question Async Service {}", e.getMessage());
		}
		finally {
			ac.complete();
		}
	}

}
