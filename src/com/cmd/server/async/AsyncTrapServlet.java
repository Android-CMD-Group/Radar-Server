package com.cmd.server.async;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.mongodb.Mongo;

/**
 * Servlet implementation class AsyncTrapServlet
 */
@WebServlet(name="asyncTrapServlet", urlPatterns={"/trap"}, asyncSupported = true)
public class AsyncTrapServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(AsyncTrapServlet.class);
	private Mongo mongoInstance = null;
	
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
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.debug("GET request received.");
	}

	/**
	 * Handles POST requests to the server.
	 * curl -X POST http://localhost:8080/Radar_Server/trap
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("POST request received.");
		//		AsyncContext ac = request.startAsync();
		//		Runnable worker = new CalculateAndInsertWorker(ac, mongoInstance);
		//		ac.start(worker);
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
