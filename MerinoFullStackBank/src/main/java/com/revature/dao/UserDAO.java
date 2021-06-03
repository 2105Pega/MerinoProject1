package com.revature.dao;


import java.util.ArrayList;

import com.revature.users.User;

public interface UserDAO {
	
	public User getUser(String username);
	public User getUser(int userID);
	public ArrayList<String> getUserList();
}
