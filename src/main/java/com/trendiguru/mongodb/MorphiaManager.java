package com.trendiguru.mongodb;

import org.apache.log4j.Logger;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoClient;
import com.trendiguru.config.ConfigManager;
import com.trendiguru.entities.RevenueProcessed;

/**
 * {@link http://mongodb.github.io/morphia/1.1/getting-started/quick-tour/}
 * 
 * @author Jeremy
 *
 */
public class MorphiaManager {
	private static Logger log = Logger.getLogger(MorphiaManager.class);
	private static MorphiaManager INSTANCE = new MorphiaManager();
	final Morphia morphia = new Morphia();
	final Datastore datastore;
	private ConfigManager configManager;
	
	
	private MorphiaManager() {
		configManager = ConfigManager.getInstance();
		morphia.mapPackage("com.trendiguru.entities");
		datastore = morphia.createDatastore(new MongoClient(configManager.getMongoDomain()), "mydb");
		
		datastore.ensureIndexes();
	}
	
	public static MorphiaManager getInstance() {
		return INSTANCE;
	}
	
	public void insertRevenueProcessed(RevenueProcessed revenue) throws DuplicateKeyException {
		datastore.ensureIndexes();
		datastore.save(revenue);
	}
	
}
