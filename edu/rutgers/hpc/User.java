package edu.rutgers.hpc;

import java.sql.Date;
import java.util.ArrayList;

import org.codehaus.jackson.annotate.*;
import org.ektorp.support.CouchDbDocument;



public class User extends CouchDbDocument{
	
	private String userID;
	private String password;
	private String Address;
	private String city;
	private String state;
	private String postalCode;
	private String email;
	private String phoneNo;
	private Date dateCreated;
	private Date dateUpdated;
	private String type;
	private ArrayList authors;

	public User()
	{
		setType("User");
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public Date getDateUpdated() {
		return dateUpdated;
	}
	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ArrayList getAuthors() {
		return authors;
	}
	public void setAuthors(ArrayList authors) {
		this.authors = authors;
	}
	

}
