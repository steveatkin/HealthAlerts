package com.ibm;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class WatsonQuestionServlet
 */
@WebServlet(urlPatterns = {"/Question"}, asyncSupported = true)
public class WatsonQuestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public WatsonQuestionServlet() {
        super();
    }


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// set the response type for server side events
		response.setContentType("text/event-stream");
    	response.setCharacterEncoding("UTF-8");

    	AsyncContext ac = request.startAsync();

    	ScheduledThreadPoolExecutor executer = new ScheduledThreadPoolExecutor(5);
        executer.execute(new WatsonQuestionAsyncService(ac));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
