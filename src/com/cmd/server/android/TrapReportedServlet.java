package com.cmd.server.android;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.mongodb.Mongo;

/**
 * Servlet implementation class TrapReportedServlet
 */
@WebServlet(name="trapReportedServlet", urlPatterns={"/trap"}, asyncSupported = true)
public class TrapReportedServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(TrapReportedServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.debug("received get");
	}
	
	/**
	 * Handles POST requests to the server.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("received post");
		Mongo mongoInstance = (Mongo) getServletContext().getAttribute(Config.MONGO_INSTANCE);
		AsyncContext ac = request.startAsync();
		Runnable worker = new CalculateAndInsertWorker(ac, mongoInstance);
		ac.start(worker);
		log.debug("after async");
	}
}
