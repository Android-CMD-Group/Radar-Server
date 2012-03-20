package edu.cmd.radar.server.controller.report;

import java.net.UnknownHostException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

@WebListener()
public class Config implements ServletContextListener{

	public static final String MONGO_INSTANCE = "MONGO_INSTANCE";
	public static final String RADAR_DATABASE = "radar";
	public static final String RAW_TRAP_COLLECTION = "rawTrapData";
	
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		 try {
			event.getServletContext().setAttribute(MONGO_INSTANCE, new Mongo("localhost"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
