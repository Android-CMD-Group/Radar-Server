package edu.cmd.radar.server.controller.report;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

import edu.cmd.radar.server.model.TrapInfo;

public class CalculateAndInsertWorker implements Runnable {

	private static final Logger log = Logger
			.getLogger(CalculateAndInsertWorker.class);
	private AsyncContext context;
	private Mongo mongoInstance;

	public CalculateAndInsertWorker(AsyncContext context, Mongo mongo) {
		this.context = context;
		mongoInstance = mongo;
	}

	@Override
	public void run() {
		log.debug("In async thread");
		String data = getJSONRequest();
		TrapInfo info = jsonToTrapInfo(data);
		insertToMongo(info);
		sendResponseAccepted();
		log.debug("after response sent");
	}

	private void sendResponseAccepted() {
		HttpServletResponse response = (HttpServletResponse) context
				.getResponse();
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		context.complete();
	}

	private TrapInfo jsonToTrapInfo(String json) {
		ObjectMapper mapper = new ObjectMapper();
		TrapInfo info = null;
		try {
			info = mapper.readValue(json, TrapInfo.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug("Pojo = "+ info.toString());
		return info;
	}

	private String getJSONRequest() {
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		String data = null;
		try {
			BufferedReader reader = request.getReader();
			StringBuilder sb = new StringBuilder();
			String line = reader.readLine();
			while (line != null) {
				sb.append(line);
				line = reader.readLine();
			}
			reader.close();
			data = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.debug("Json = "+ data);
		return data;
	}
	
	private void insertToMongo(TrapInfo info){
		DB db = mongoInstance.getDB(Config.RADAR_DATABASE);
		DBCollection rawTrapCollection = db.getCollection(Config.RAW_TRAP_COLLECTION);
		BasicDBObject trap = new BasicDBObject();
		
        trap.put("loc", info.getLoc());
        trap.put("accuracy", info.getAccuracy());
        trap.put("speed", info.getSpeed());
        trap.put("bearing", info.getBearing());
        trap.put("timeReported", info.getTimeReported());
        trap.put("timeOfLocation", info.getTimeOfLocation());
        trap.put("id", info.getId());

        rawTrapCollection.insert(trap);
		
	}
}
