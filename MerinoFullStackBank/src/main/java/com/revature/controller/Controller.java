package com.revature.controller;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.responses.Response;
import com.revature.services.CustomerService;
import com.revature.services.UserService;
import com.revature.users.Customer;
import com.revature.users.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/controller")
public class Controller {
	private static final Logger logger = LogManager.getLogger(Controller.class);
	private UserService uServ = new UserService();
	private CustomerService cServ = new CustomerService();

//	@Path("/login")
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	public String login(User user) {
//
//		User backendUser = uServ.getUser(user.getUserName());
//		if (user.getPassword().equals(backendUser.getPassword())) {
//
//			ObjectMapper mapper = new ObjectMapper();
//
//			try {
//				return mapper.writeValueAsString(backendUser);
//			} catch (JsonProcessingException e) {
//				// TODO Auto-generated catch block
//
//				e.printStackTrace();
//			}
//		} else {
//			Response response = new Response();
//			response.fail = true;
//			response.warning = "Username/Password didn't match";
//			ObjectMapper mapper = new ObjectMapper();
//			try {
//				return mapper.writeValueAsString(response);
//			} catch (JsonProcessingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//		return "";
//
//	}

	@Path("/login/{user}/{pass}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String login(@PathParam("user") String user, @PathParam("pass") String pass) {

		User backendUser = uServ.getUser(user);
		if (backendUser == null) {
			Response response = new Response();
			response.fail = true;
			response.warning = "Username doesn't exist.";
			ObjectMapper mapper = new ObjectMapper();
			try {
				return mapper.writeValueAsString(response);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (pass.equals(backendUser.getPassword())) {
			if (backendUser.getUserType() == 1) {
				Response response = new Response();
				Customer backendCustomer = cServ.getCustomer(backendUser.getUserID());
				response.fail = false;
//				response.warning = "Successful";
				response.userID = backendCustomer.getUserID();
				response.userName = backendCustomer.getUserName();
				response.password = backendCustomer.getPassword();
				response.firstName = backendCustomer.getFirstName();
				response.lastName = backendCustomer.getLastName();
				response.userType = 1;
				response.address = backendCustomer.getAddress();
				response.phone = backendCustomer.getPhone();
				response.accountList = backendCustomer.getAccountList();
				response.numberOfAccounts = backendCustomer.getNumberOfAccounts();
				ObjectMapper mapper = new ObjectMapper();

				try {
					return mapper.writeValueAsString(response);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				}
			}
			
			
		} else {
			Response response = new Response();
			response.fail = true;
			response.warning = "Username/Password didn't match.";
			ObjectMapper mapper = new ObjectMapper();
			try {
				return mapper.writeValueAsString(response);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return "";

	}
}
