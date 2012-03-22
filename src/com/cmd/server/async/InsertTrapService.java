package com.cmd.server.async;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.mongodb.Mongo;

public class InsertTrapService implements Runnable {
	
	private static final Logger log = Logger.getLogger(InsertTrapService.class);
	
	private AsyncContext aContext = null;
	private Mongo mongoInstance = null;
	
	public InsertTrapService(AsyncContext aContext, Mongo mongo) {
		log.debug("TrapInsertService instantiated");
		this.aContext = aContext;
		this.mongoInstance = mongo;
	}
	
	@Override
	public void run() {
		log.debug("Starting async thread");
		log.debug("1) Insert the data into MongoDB");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpServletResponse response = (HttpServletResponse) aContext.getResponse();
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		
		log.debug("Ending async thread");
		this.aContext.complete();
	}

}
