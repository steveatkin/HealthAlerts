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

package com.ibm.health;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class HealthData {
	private static final Logger logger = LoggerFactory.getLogger(HealthData.class);
	private static String alertsService = "Alerts v1 : Sandbox 556609270cf2ecce6225ec76 prod";
	private static String baseURLAlerts = "";
	private static String clientId = "";
	private static String clientSecret = "";


	static {
		JSONObject sysEnv = getVcapServices();

		logger.info("Processing VCAP_SERVICES");
		logger.info("Looking for: "+ alertsService);

		if (sysEnv != null && sysEnv.containsKey(alertsService)) {
			JSONArray services = (JSONArray)sysEnv.get(alertsService);
			JSONObject service = (JSONObject)services.get(0);
			JSONObject credentials = (JSONObject)service.get("credentials");
			baseURLAlerts = (String)credentials.get("url");
			clientId = (String)credentials.get("client_id");
			clientSecret = (String)credentials.get("client_secret");
			logger.info("baseURL  = "+ baseURLAlerts);
			logger.info("username   = "+ clientId);
			logger.info("password = "+ clientSecret);
		}
		else {
			logger.info("Attempting to use locally defined service credentials");
			baseURLAlerts = System.getenv("ALERTS_URL");
			clientId = System.getenv("ALERTS_CLIENT_ID");
			clientSecret = System.getenv("ALERTS_CLIENT_SECRET");
			logger.debug("Alerts url {} username {} and password {}", baseURLAlerts, clientId, clientSecret);
		}
	}

	private static JSONObject getVcapServices() {
        String envServices = System.getenv("VCAP_SERVICES");
        JSONObject sysEnv = null;

        if (envServices == null) {
        	logger.info("VCAP Services not found, using predfined meta-information");
        	return null;
        }

        try {
        	sysEnv = JSONObject.parse(envServices);
        }
        catch(Exception e) {
        	logger.error("Error parsing VCAP_SERVICES: {}", e.getMessage());
        }

        return sysEnv;
    }

	public HealthData() {

	}
	
	public String getLocations() {
		String result = "";
		
		
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			    
			URIBuilder builder = new URIBuilder(baseURLAlerts + "/locations");
		    builder.setParameter("client_id", clientId)
		    .setParameter("client_secret", clientSecret);
			URI uri = builder.build();
			
			HttpGet httpGet = new HttpGet(uri);

			httpGet.setHeader("Content-Type", "text/plain");
			HttpResponse httpResponse = httpClient.execute(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				BufferedReader rd = new BufferedReader(
				        new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));

				StringBuilder everything = new StringBuilder();
			    String line;
			    while( (line = rd.readLine()) != null) {
			       everything.append(line);
			    }
			    result =  everything.toString();
			}
			else {
				logger.error("could not get locations {}", httpResponse.getStatusLine().getStatusCode());
			}

		}
		catch(Exception e) {
			logger.error("Locations error: {}", e.getMessage());
		}

		return result;
	}

	public String getAlerts(String location, String hscore, String lscore) {
		
		String result = "";

		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			    
			URIBuilder builder = new URIBuilder(baseURLAlerts + "/alerts");
		    builder.setParameter("client_id", clientId)
		    .setParameter("client_secret", clientSecret)
		    .setParameter("hscore", hscore)
		    .setParameter("lscore", lscore)
		    .setParameter("location", location);
			URI uri = builder.build();
			
			HttpGet httpGet = new HttpGet(uri);

			httpGet.setHeader("Content-Type", "text/plain");
			HttpResponse httpResponse = httpClient.execute(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				BufferedReader rd = new BufferedReader(
				        new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));

				StringBuilder everything = new StringBuilder();
			    String line;
			    while( (line = rd.readLine()) != null) {
			       everything.append(line);
			    }
			    result =  everything.toString();
			}
			else {
				logger.error("could not get alerts {}", httpResponse.getStatusLine().getStatusCode());
			}

		}
		catch(Exception e) {
			logger.error("Alerts error: {}", e.getMessage());
		}

		return result;
	}
	
	public String getHealthInformation(String condition) {
		
		String result = "";

		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			    
			URIBuilder builder = new URIBuilder(baseURLAlerts + "/terms");
		    builder.setParameter("client_id", clientId)
		    .setParameter("client_secret", clientSecret)
		    .setParameter("condition", condition);
			URI uri = builder.build();
			
			HttpGet httpGet = new HttpGet(uri);

			httpGet.setHeader("Content-Type", "text/plain");
			HttpResponse httpResponse = httpClient.execute(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				BufferedReader rd = new BufferedReader(
				        new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));

				StringBuilder everything = new StringBuilder();
			    String line;
			    while( (line = rd.readLine()) != null) {
			       everything.append(line);
			    }
			    result =  everything.toString();
			}
			else {
				logger.error("could not get health condition information {}", httpResponse.getStatusLine().getStatusCode());
			}

		}
		catch(Exception e) {
			logger.error("Health Information error: {}", e.getMessage());
		}

		return result;
	}

	public String getHealthNews(String condition, String location) {
		
		String result = "";

		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			    
			URIBuilder builder = new URIBuilder(baseURLAlerts + "/news");
		    builder.setParameter("client_id", clientId)
		    .setParameter("client_secret", clientSecret)
		    .setParameter("condition", condition)
		    .setParameter("location", location);
			URI uri = builder.build();
			
			HttpGet httpGet = new HttpGet(uri);

			httpGet.setHeader("Content-Type", "text/plain");
			HttpResponse httpResponse = httpClient.execute(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				BufferedReader rd = new BufferedReader(
				        new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));

				StringBuilder everything = new StringBuilder();
			    String line;
			    while( (line = rd.readLine()) != null) {
			       everything.append(line);
			    }
			    result =  everything.toString();
			}
			else {
				logger.error("could not get health condition news {}", httpResponse.getStatusLine().getStatusCode());
			}

		}
		catch(Exception e) {
			logger.error("Health News error: {}", e.getMessage());
		}

		return result;
	}
}
