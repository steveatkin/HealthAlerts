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

package com.ibm.watson;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;


public class WatsonQuestionAnswer {
	private static final Logger logger = LoggerFactory.getLogger(WatsonQuestionAnswer.class);

	private static String questionService = "question_and_answer";
	private static String baseURLQuestion = "";
	private static String usernameQuestion = "";
	private static String passwordQuestion = "";

	static {
		logger.info("IN VCAP SETUP for WATSON");
		processVCAP_Services();
	}

	private static void processVCAP_Services() {
		logger.info("Processing VCAP_SERVICES");

		JSONObject sysEnv = getVcapServices();

		logger.info("Looking for: "+ questionService);

      if (sysEnv != null && sysEnv.containsKey(questionService)) {
      	JSONArray services = (JSONArray)sysEnv.get(questionService);
				JSONObject service = (JSONObject)services.get(0);
				JSONObject credentials = (JSONObject)service.get("credentials");
				baseURLQuestion = (String)credentials.get("url");
				usernameQuestion = (String)credentials.get("username");
				passwordQuestion = (String)credentials.get("password");
				logger.info("baseURL  = "+baseURLQuestion);
				logger.info("username   = "+usernameQuestion);
				logger.info("password = "+passwordQuestion);
    	}
			else {
				logger.info("Attempting to use locally defined service credentials watson question");
				baseURLQuestion = System.getenv("WATSON_QUESTION");
				usernameQuestion = System.getenv("WATSON_QUESTION_USERNAME");
				passwordQuestion = System.getenv("WATSON_QUESTION_PASSWORD");
				logger.info("Watson Question url {} username {} and password {}", baseURLQuestion, usernameQuestion, passwordQuestion);
			}
    }

	private static JSONObject getVcapServices() {
        String envServices = System.getenv("VCAP_SERVICES");
        if (envServices == null) return null;
        JSONObject sysEnv = null;
        try {
        	 sysEnv = JSONObject.parse(envServices);
        } catch (IOException e) {
        	// Do nothing, fall through to defaults
        	logger.error("Error parsing VCAP_SERVICES: {} ", e.getMessage());
        }
        return sysEnv;
    }

	public WatsonQuestionAnswer() {

	}

	private List<Map<String,String>> formatAnswers(String resultJson) {
		List<Map<String,String>> ret = new ArrayList<Map<String,String>>();
		if(resultJson != null) {
			try {
				JSONArray pipelines = JSONArray.parse(resultJson);
				// the response has two pipelines, lets use the first one
				JSONObject answersJson = (JSONObject) pipelines.get(0);
				JSONArray answers = (JSONArray) ((JSONObject) answersJson.get("question")).get("evidencelist");

				for(int i = 0; i < answers.size();i++) {
					JSONObject answer = (JSONObject) answers.get(i);
					Map<String, String> map = new HashMap<String, String>();

					if(answer.get("value") != null) {
						double p = Double.parseDouble((String)answer.get("value"));
						p = Math.floor(p * 100);
						map.put("confidence",  Double.toString(p) + "%");
						map.put("text", (String)answer.get("text"));

						ret.add(map);
					}
				}
			} catch (IOException e) {
				logger.error("Error parsing the response {} ", e.getMessage());
			}
		}
		return ret;
	}

	public List<Map<String, String>> getAnswers(String questionText, String dataset) {
		List<Map<String, String>> answers = null;

		JSONObject questionJson = new JSONObject();
		questionJson.put("questionText",questionText);
		JSONObject evidenceRequest = new JSONObject();
		evidenceRequest.put("items",5);
		questionJson.put("evidenceRequest",evidenceRequest);

		JSONObject postData = new JSONObject();
    	postData.put("question",questionJson);

    	logger.debug("Watson question: {}", questionText);

		try {
			Executor executor = Executor.newInstance();
			URI serviceURI = new URI(baseURLQuestion + "/v1/question/"+dataset).normalize();
			String auth = usernameQuestion + ":" + passwordQuestion;
			String answersJson = executor.execute(Request.Post(serviceURI)
					.addHeader("Authorization", "Basic "+ Base64.encodeBase64String(auth.getBytes()))
				    .addHeader("Accept", "application/json")
				    .addHeader("X-SyncTimeout", "60")
				    .bodyString(postData.toString(), ContentType.APPLICATION_JSON)
				    ).returnContent().asString();

			answers = formatAnswers(answersJson);
			}
			catch(Exception e) {
				logger.error("Watson question error: {}", e.getMessage());
			}

		return answers;
	}

}
