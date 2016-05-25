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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.health.HealthData;
import com.ibm.watson.WatsonTranslate;


/**
 * Servlet implementation class TwitterServlet
 */
@WebServlet(urlPatterns = {"/Terms"}, asyncSupported = true)
public class HealthInformationServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(HealthInformationServlet.class);
	private static final long serialVersionUID = 1L;

	@JsonIgnoreProperties(ignoreUnknown=true)
	private static class TermDefinition {
		@JsonProperty("name")
		private String name = "";
		
		@JsonProperty("value")
		private String value = "";
		
		@JsonProperty("name")
		public String getName() {
			return name;
		}
		
		@JsonProperty("value")
		public String getValue() {
			return value;
		}
		
		@JsonProperty("name")
		public void setName(String name) {
			this.name = name;
		}
		
		@JsonProperty("value")
		public void setValue(String value) {
			this.value = value;
		}
	}
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HealthInformationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/json");
		response.setCharacterEncoding("UTF-8");
		
		String condition = request.getParameter("condition");
		boolean translate = Boolean.parseBoolean(request.getParameter("enable"));
		Locale locale = request.getLocale();
		
		OutputStream stream = response.getOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8");
		
		HealthData information = new HealthData();
		
		ObjectMapper mapper = new ObjectMapper();
	    TermDefinition definition = mapper.readValue(information.getHealthInformation(condition).replace("\n", ""), TermDefinition.class);
		
		if(translate) {
			WatsonTranslate watson = new WatsonTranslate(locale);
			definition.setValue(watson.translate(definition.getValue()));
		}
		
		try {
			writer.write(mapper.writeValueAsString(definition));
			writer.flush();
			writer.close();
		}
		catch (Exception e) {
			logger.error("Health information could not be retrieved");
			throw new IOException("Health information could not be retrieved");	
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
