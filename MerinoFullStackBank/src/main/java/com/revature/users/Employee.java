package com.revature.users;

public class Employee extends User {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5851399894036286017L;
	

	public Employee(int userID, String user, String pass, String firstName, String lastName, int userType) {
		super(userID, user, pass, firstName, lastName, 2);
		
		
	}
	

}
