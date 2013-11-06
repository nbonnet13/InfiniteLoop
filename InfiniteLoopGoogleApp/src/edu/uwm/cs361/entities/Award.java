package edu.uwm.cs361.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

public class Award {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key award_id;
	
	@Persistent
	private String awardName;
	
	@Persistent
	private String awardDescription;
	
	@Persistent
	private Set<User> users;
	
	public Award(String name, String des) {
		awardName = name;
		awardDescription = des;
		users = new HashSet<User>();
	}
	
	public Key getAward_id(){
		return award_id;
	}
	
	public String getAwardName(){
		return awardName;
	}
	

	public String getAwardDescription(){
		return awardDescription;
	}
	
	public Set<User> getUsers(){
		return users;
	}
}