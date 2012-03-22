package com.cmd.server.async;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.mongodb.Mongo;

/**
 * Servlet implementation class AsyncTrapServlet
 * 
 * @WebServlet - Defines a servlet and its attributes
 */
@WebServlet(name="asyncTrapServlet", urlPatterns={"/trap"}, asyncSupported = true)
public class AsyncTrapServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(AsyncTrapServlet.class);
	
	private Mongo mongoInstance = null;
	private ScheduledThreadPoolExecutor executor = null;
	
	/**
	 * Called when a servlet is initialized.  
	 * Doesn't seem to be called until a POST or GET request occurs... need to figure out why?
	 * Establishes a connection with the MongoDB
	 */
	public void init() throws ServletException {
		log.debug("Initializing servlet");
			
		try {
			
			this.mongoInstance = new Mongo("localhost");
			log.debug("Established connection to MongoDB @ " + mongoInstance.getAddress());
			this.executor = new ScheduledThreadPoolExecutor(10);
			
		} catch (Exception e) {
			log.debug("Failed to establish connection to MongoDB!");
			e.printStackTrace();
		}
	}

	/**
	 * Handles GET requests
	 * curl 'http://localhost:8080/Radar_Server/trap'
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("GET request received.");
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.println("<title>Radar_Servlet</title> <body bgcolor=FFFFFF>");
	    out.println("<h2>GET request received @ " + this.mongoInstance.getAddress() + "</h2>");
	    out.close();
	}

	/**
	 * Handles POST requests to the server.
	 * curl -X POST http://localhost:8080/Radar_Server/trap
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("POST request received.");
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("POST request recieved.");
		
		// Starts an asynchronous request and add it to the executor
		AsyncContext aContext = request.startAsync(request, response);
		if (this.executor != null) {
			executor.execute(new InsertTrapService(aContext, this.mongoInstance));
		}
	}
	
	/**
	 * Called when a servlet is destroyed.
	 */
	public void Destroy() {
		log.debug("Servlet is being taken out of service.");
		if (this.mongoInstance != null) {
			this.mongoInstance.close();
			this.mongoInstance = null;
			log.debug("MongoDB connection closed");
		}
	}
	
}
