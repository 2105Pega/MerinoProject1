package com.revature.users;



public class User{
	/**
	 * 
	 */
	private int userID;
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private int userType;
	
	
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public User(int userID, String user, String pass, String firstName, String lastName, int userType) {
		super();
		this.userID = userID;
		userName = user;
		password = pass;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userType = userType;
	}
	public User() {
		this.userID = 100;
		userName = "some";
		password = "thing";
		this.firstName = "for";
		this.lastName = "default";
		this.userType = 2;
	}
	
	
	public int getUserID() {
		return userID;
	}
	public int getUserType() {
		return userType;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public User(int userID, String user, String pass, String firstName, String lastName) {
		super();
		this.userID = userID;
		userName = user;
		password = pass;
		this.firstName = firstName;
		this.lastName = lastName;
		userType = 1;
	}
	
	@Override
	public String toString() {
		return "User [userName=" + userName + ", firstName=" + firstName + ", lastName="
				+ lastName + ", userID=" + userID + ", userType=" + userType + "]";
	}
	
	
}
