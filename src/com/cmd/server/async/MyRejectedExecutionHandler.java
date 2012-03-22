package com.cmd.server.async;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

public class MyRejectedExecutionHandler implements RejectedExecutionHandler{
	
	private static final Logger log = Logger.getLogger(InsertTrapService.class);
	
	@Override
	public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
		// TODO Auto-generated method stub
		log.debug(runnable.toString() + " has been rejected !");
		
	}

}
