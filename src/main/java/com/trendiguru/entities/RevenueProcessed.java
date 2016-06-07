package com.trendiguru.entities;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Transient;

/**
 * 
 * @author Jeremy
 *
 */
@Entity("users")
public class RevenueProcessed {
	@Id
	ObjectId id;
	
	@Indexed(options = @IndexOptions(unique = true))
	Date revenueDate;
	
	Date dateProcessed;

	public RevenueProcessed(Date revenueDate) {
		this.revenueDate = revenueDate;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Date getRevenueDate() {
		return revenueDate;
	}

	public void setRevenueDate(Date revenueDate) {
		this.revenueDate = revenueDate;
	}

	public Date getDateProcessed() {
		return dateProcessed;
	}

	public void setDateProcessed(Date dateProcessed) {
		this.dateProcessed = dateProcessed;
	}
	
}
