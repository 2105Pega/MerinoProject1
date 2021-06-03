package com.revature.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.services.UserService;
import com.revature.users.User;

@Path("/controller")
public class Controller {
	
	private UserService uServ = new UserService();
	
	
	
	@Path("/login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String login(User user) {
		
//		User backendUser = uServ.getUser(user.getUserName());
		User backendUser = uServ.getUser("merinolu");
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(backendUser.toString());
		System.out.println("here");
		try {
			return mapper.writeValueAsString(backendUser);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		} return "";
	}
	
}
