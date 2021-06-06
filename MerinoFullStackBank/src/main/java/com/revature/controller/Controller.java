package com.revature.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.accounts.Account;
import com.revature.accounts.OpenAccount;
import com.revature.exceptions.InvalidActionException;
import com.revature.responses.Response;
import com.revature.services.AccountService;
import com.revature.services.CustomerService;
import com.revature.services.EmployeeService;
import com.revature.services.UserService;
import com.revature.services.tServices;
import com.revature.transactions.DecisionAttempt;
import com.revature.transactions.InfoUpdateAttempt;
import com.revature.transactions.PasswordUpdateAttempt;
import com.revature.transactions.TransferAttempt;
import com.revature.transactions.WithdrawDepositAttempt;
import com.revature.users.CreateCustomer;
import com.revature.users.Customer;
import com.revature.users.Employee;
import com.revature.users.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/controller")
public class Controller {

	private static final Logger logger = LogManager.getLogger(Controller.class);

	private UserService uServ = new UserService();
	private CustomerService cServ = new CustomerService();
	private AccountService accServ = new AccountService();
	private tServices tServ = new tServices();
	private EmployeeService eServ = new EmployeeService();

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
				response.warning = "Successful";
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
			} else if (backendUser.getUserType() == 2) {
				Response response = new Response();
				Employee backendEmployee = eServ.getEmployee(backendUser.getUserID());
				response.fail = false;
				response.warning = "Successful";
				response.userID = backendEmployee.getUserID();
				response.userName = backendEmployee.getUserName();
				response.password = backendEmployee.getPassword();
				response.firstName = backendEmployee.getFirstName();
				response.lastName = backendEmployee.getLastName();
				response.userType = 2;
				response.employeeCustomerList = eServ.getCustomerList();
				ObjectMapper mapper = new ObjectMapper();

				try {
					return mapper.writeValueAsString(response);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				}
			} else {
				Response response = new Response();
				response.fail = true;
				response.warning = "Administrator login not implemented yet.";
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

	@Path("/account/{user}/{pass}/{acc}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String account(@PathParam("user") String user, @PathParam("pass") String pass, @PathParam("acc") int acc) {
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
				if (backendCustomer.getAccountList().contains(acc)) {
					Account backendAcc = accServ.getAccount(acc);

					response.fail = false;
					response.warning = "Successfully got the account";
					response.accountNumber = backendAcc.getAccountNumber();
					response.accountType = backendAcc.getAccountType();
					response.balance = backendAcc.getBalance();
					response.approved = backendAcc.getApproved();
					response.customerList = backendAcc.getCustomerList();
					ObjectMapper mapper = new ObjectMapper();

					try {
						return mapper.writeValueAsString(response);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block

						e.printStackTrace();
					}
				} else {

					response.fail = true;
					response.warning = "User doesn't own this account.";
					ObjectMapper mapper = new ObjectMapper();
					try {
						return mapper.writeValueAsString(response);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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

	@Path("/open/{user}/{pass}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

//	public String openAccount(@FormParam("accType") String accType, @FormParam("balance") double balance, @DefaultValue("0")@FormParam("joint") int joint, @PathParam("user") String user, @PathParam("pass") String pass) {
	public String openAccount(OpenAccount openAcc, @PathParam("user") String user, @PathParam("pass") String pass) {
		User backendUser = uServ.getUser(user);
		System.out.println("Jersey accType: " + openAcc.accType + ", balance: " + openAcc.balance + ", joint:"
				+ openAcc.joint + ", path params user: " + user + ", pass: " + pass);
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
		if (backendUser.getUserID() == openAcc.joint) {
			Response response = new Response();
			response.fail = true;
			response.warning = "Can't add yourself as a joint account holder.";
			ObjectMapper mapper = new ObjectMapper();
			try {
				return mapper.writeValueAsString(response);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}
		if (openAcc.joint != 0) {
			if (pass.equals(backendUser.getPassword())) {
				if (backendUser.getUserType() == 1) {

					User jointUser = uServ.getUser(openAcc.joint);
					if (jointUser == null || jointUser.getUserType() != 1) {
						Response response = new Response();
						response.fail = true;
						response.warning = "Could not find a customer with your joint user's id.";
						ObjectMapper mapper = new ObjectMapper();
						try {
							return mapper.writeValueAsString(response);
						} catch (JsonProcessingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						Account newAcc = new Account(1, openAcc.accType, openAcc.balance, "Pending");
						newAcc.addCustomer(backendUser.getUserID());
						newAcc.addCustomer(openAcc.joint);
						System.out.println(newAcc);
						if (accServ.createAccount(newAcc)) {
							Response response = new Response();
							Customer backendCustomer = cServ.getCustomer(backendUser.getUserID());
							response.fail = false;
							response.warning = "Successful";
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
						} else {
							System.out.println("server failed with joint " + openAcc.joint);
							Response response = new Response();
							response.fail = true;
							response.warning = "Server failed to open account.";
							ObjectMapper mapper = new ObjectMapper();
							try {
								return mapper.writeValueAsString(response);
							} catch (JsonProcessingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}

				} else {
					Response response = new Response();
					response.fail = true;
					response.warning = "Cannot open an account if you are not a customer.";
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
		} else {
			if (pass.equals(backendUser.getPassword())) {
				if (backendUser.getUserType() == 1) {

					Account newAcc = new Account(1, openAcc.accType, openAcc.balance, "Pending");
					newAcc.addCustomer(backendUser.getUserID());
					System.out.println(newAcc);

					if (accServ.createAccount(newAcc)) {
						Response response = new Response();
						Customer backendCustomer = cServ.getCustomer(backendUser.getUserID());
						response.fail = false;
						response.warning = "Successful";
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
					} else {
						System.out.println("failed without joint " + openAcc.joint);
						Response response = new Response();
						response.fail = true;
						response.warning = "Server failed to open account.";
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
					response.warning = "Cannot open an account if you are not a customer.";
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
		}

		return "";
	}

	@Path("/create")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String createCustomer(CreateCustomer createCus) {
		User backendUser = uServ.getUser(createCus.user);
		System.out.println("Jersey createCus: " + createCus.user + ", password: " + createCus.password + ", fName:"
				+ createCus.fName + ", lName:" + createCus.lName);
		if (backendUser != null) {
			Response response = new Response();
			response.fail = true;
			response.warning = "Username already exists.";
			ObjectMapper mapper = new ObjectMapper();
			try {
				return mapper.writeValueAsString(response);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if (cServ.createCustomer(createCus.user, createCus.password, createCus.fName, createCus.lName)) {
				backendUser = uServ.getUser(createCus.user);
				Response response = new Response();
				Customer backendCustomer = cServ.getCustomer(backendUser.getUserID());
				response.fail = false;
				response.warning = "Successful";
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

			} else {
				Response response = new Response();
				response.fail = true;
				response.warning = "Server was unable to create the user.";
				ObjectMapper mapper = new ObjectMapper();
				try {
					return mapper.writeValueAsString(response);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return "";
	}

	@Path("/withdraw")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String withdraw(WithdrawDepositAttempt withAttempt) {
		User backendUser = uServ.getUser(withAttempt.userName);
		System.out.println("Jersey withAttempt: " + withAttempt.userName + ", password: " + withAttempt.password
				+ ", amount:" + withAttempt.amount + ", accNumber:" + withAttempt.accNumber);
		if (backendUser == null) {
			Response response = new Response();
			response.fail = true;
			response.warning = "User for withdrawl attempt could not be found.";
			ObjectMapper mapper = new ObjectMapper();
			try {
				return mapper.writeValueAsString(response);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if (!backendUser.getPassword().equals(withAttempt.password)) {
				Response response = new Response();
				response.fail = true;
				response.warning = "User and password for withdrawl did not match.";
				ObjectMapper mapper = new ObjectMapper();
				try {
					return mapper.writeValueAsString(response);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Customer backendCustomer = cServ.getCustomer(backendUser.getUserID());
				if (backendCustomer.getAccountList().contains(withAttempt.accNumber)) {
					try {
						String result = tServ.withdraw(withAttempt.amount, withAttempt.accNumber);
						logger.trace("Withdrawl attempt was made. Result: " + result);

						Response response = new Response();
						response.fail = false;
						response.warning = "Successful";
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
					} catch (InvalidActionException e) {
						// TODO Auto-generated catch block
						Response response = new Response();
						response.fail = true;
						response.warning = e.getMessage();
						ObjectMapper mapper = new ObjectMapper();
						try {
							return mapper.writeValueAsString(response);
						} catch (JsonProcessingException e1) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					Response response = new Response();
					response.fail = true;
					response.warning = "Provided user does not own this account.";
					ObjectMapper mapper = new ObjectMapper();
					try {
						return mapper.writeValueAsString(response);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}

		return "";
	}

	@Path("/deposit")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String deposit(WithdrawDepositAttempt depAttempt) {
		User backendUser = uServ.getUser(depAttempt.userName);
		System.out.println("Jersey depAttempt: " + depAttempt.userName + ", password: " + depAttempt.password
				+ ", amount:" + depAttempt.amount + ", accNumber:" + depAttempt.accNumber);
		if (backendUser == null) {
			Response response = new Response();
			response.fail = true;
			response.warning = "User for deposit attempt could not be found.";
			ObjectMapper mapper = new ObjectMapper();
			try {
				return mapper.writeValueAsString(response);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if (!backendUser.getPassword().equals(depAttempt.password)) {
				Response response = new Response();
				response.fail = true;
				response.warning = "User and password for deposit did not match.";
				ObjectMapper mapper = new ObjectMapper();
				try {
					return mapper.writeValueAsString(response);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Customer backendCustomer = cServ.getCustomer(backendUser.getUserID());
				if (backendCustomer.getAccountList().contains(depAttempt.accNumber)) {
					try {
						String result = tServ.deposit(depAttempt.amount, depAttempt.accNumber);

						Response response = new Response();
						response.fail = false;
						response.warning = "Successful";
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
					} catch (InvalidActionException e) {
						// TODO Auto-generated catch block
						Response response = new Response();
						response.fail = true;
						response.warning = e.getMessage();
						ObjectMapper mapper = new ObjectMapper();
						try {
							return mapper.writeValueAsString(response);
						} catch (JsonProcessingException e1) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					Response response = new Response();
					response.fail = true;
					response.warning = "Provided user does not own this account.";
					ObjectMapper mapper = new ObjectMapper();
					try {
						return mapper.writeValueAsString(response);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}

		return "";
	}

	@Path("/transfer")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String transfer(TransferAttempt transAttempt) {
		User backendUser = uServ.getUser(transAttempt.userName);
		System.out.println("Jersey transAttempt: " + transAttempt.userName + ", password: " + transAttempt.password
				+ ", amount:" + transAttempt.amount + ", senderNumber:" + transAttempt.senderNumber
				+ ", receiverNumber:" + transAttempt.receiverNumber);
		if (backendUser == null) {
			Response response = new Response();
			response.fail = true;
			response.warning = "User for deposit attempt could not be found.";
			ObjectMapper mapper = new ObjectMapper();
			try {
				return mapper.writeValueAsString(response);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if (!backendUser.getPassword().equals(transAttempt.password)) {
				Response response = new Response();
				response.fail = true;
				response.warning = "User and password for deposit did not match.";
				ObjectMapper mapper = new ObjectMapper();
				try {
					return mapper.writeValueAsString(response);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Customer backendCustomer = cServ.getCustomer(backendUser.getUserID());
				if (backendCustomer.getAccountList().contains(transAttempt.senderNumber)) {
					try {
						String result = tServ.transfer(transAttempt.amount, transAttempt.senderNumber,
								transAttempt.receiverNumber);

						Response response = new Response();
						response.fail = false;
						response.warning = "Successful";
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
					} catch (InvalidActionException e) {
						// TODO Auto-generated catch block
						Response response = new Response();
						response.fail = true;
						response.warning = e.getMessage();
						ObjectMapper mapper = new ObjectMapper();
						try {
							return mapper.writeValueAsString(response);
						} catch (JsonProcessingException e1) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					Response response = new Response();
					response.fail = true;
					response.warning = "Provided user does not own this account.";
					ObjectMapper mapper = new ObjectMapper();
					try {
						return mapper.writeValueAsString(response);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}

		return "";
	}

	@Path("/decide")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String decide(DecisionAttempt decAttempt) {
		User backendUser = uServ.getUser(decAttempt.userName);
		System.out.println("Jersey decAttempt: " + decAttempt.userName + ", password: " + decAttempt.password
				+ ", decision:" + decAttempt.decision + ", accNumber:" + decAttempt.accNumber);
		if (backendUser == null) {
			Response response = new Response();
			response.fail = true;
			response.warning = "User for account application decision attempt could not be found.";
			ObjectMapper mapper = new ObjectMapper();
			try {
				return mapper.writeValueAsString(response);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if (!backendUser.getPassword().equals(decAttempt.password)) {
				Response response = new Response();
				response.fail = true;
				response.warning = "User and password for account application decision did not match.";
				ObjectMapper mapper = new ObjectMapper();
				try {
					return mapper.writeValueAsString(response);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {

				if (backendUser.getUserType() == 2) {
					try {
						Employee backendEmployee = eServ.getEmployee(backendUser.getUserID());
						String result = eServ.decideService(backendEmployee, decAttempt.accNumber, decAttempt.decision);

						Response response = new Response();

						response.fail = false;
						response.warning = "Successful";
						response.userID = backendEmployee.getUserID();
						response.userName = backendEmployee.getUserName();
						response.password = backendEmployee.getPassword();
						response.firstName = backendEmployee.getFirstName();
						response.lastName = backendEmployee.getLastName();
						response.userType = 2;
						response.employeeCustomerList = eServ.getCustomerList();
						ObjectMapper mapper = new ObjectMapper();

						try {
							return mapper.writeValueAsString(response);
						} catch (JsonProcessingException e) {
							// TODO Auto-generated catch block

							e.printStackTrace();
						}
					} catch (InvalidActionException e) {
						// TODO Auto-generated catch block
						Response response = new Response();
						response.fail = true;
						response.warning = e.getMessage();
						ObjectMapper mapper = new ObjectMapper();
						try {
							return mapper.writeValueAsString(response);
						} catch (JsonProcessingException e1) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					Response response = new Response();
					response.fail = true;
					response.warning = "Only an employee can decide on pending accounts.";
					ObjectMapper mapper = new ObjectMapper();
					try {
						return mapper.writeValueAsString(response);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}

		return "";
	}

	@Path("/infoUpdate")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String infoUpdate(InfoUpdateAttempt infoUpdateAttempt) {
		User backendUser = uServ.getUser(infoUpdateAttempt.userName);
		System.out.println(
				"Jersey infoUpdateAttempt: " + infoUpdateAttempt.userName + ", password: " + infoUpdateAttempt.password
						+ ", newAddress:" + infoUpdateAttempt.newAddress + ", newPhone:" + infoUpdateAttempt.newPhone);
		if (backendUser == null) {
			Response response = new Response();
			response.fail = true;
			response.warning = "User for information update attempt could not be found.";
			ObjectMapper mapper = new ObjectMapper();
			try {
				return mapper.writeValueAsString(response);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if (!backendUser.getPassword().equals(infoUpdateAttempt.password)) {
				Response response = new Response();
				response.fail = true;
				response.warning = "User and password for update did not match.";
				ObjectMapper mapper = new ObjectMapper();
				try {
					return mapper.writeValueAsString(response);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Customer backendCustomer = cServ.getCustomer(backendUser.getUserID());
				if (cServ.updateInfo(backendCustomer, infoUpdateAttempt.newPhone, infoUpdateAttempt.newAddress)) {
					backendCustomer = cServ.getCustomer(backendUser.getUserID());
					Response response = new Response();
					response.fail = false;
					response.warning = "Successful";
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
				} else {
					// TODO Auto-generated catch block
					Response response = new Response();
					response.fail = true;
					response.warning = "Server failed to update personal information";
					ObjectMapper mapper = new ObjectMapper();
					try {
						return mapper.writeValueAsString(response);
					} catch (JsonProcessingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		}

		return "";
	}
	
	@Path("/passwordUpdate")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String passwordUpdate(PasswordUpdateAttempt passwordUpdateAttempt) {
		User backendUser = uServ.getUser(passwordUpdateAttempt.userName);
		System.out.println(
				"Jersey passwordUpdateAttempt: " + passwordUpdateAttempt.userName + ", password: " + passwordUpdateAttempt.password
						+ ", newPassword:" + passwordUpdateAttempt.newPassword);
		if (backendUser == null) {
			Response response = new Response();
			response.fail = true;
			response.warning = "User for password update attempt could not be found.";
			ObjectMapper mapper = new ObjectMapper();
			try {
				return mapper.writeValueAsString(response);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if (!backendUser.getPassword().equals(passwordUpdateAttempt.password)) {
				Response response = new Response();
				response.fail = true;
				response.warning = "User and original for update did not match.";
				ObjectMapper mapper = new ObjectMapper();
				try {
					return mapper.writeValueAsString(response);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Customer backendCustomer = cServ.getCustomer(backendUser.getUserID());
				if (cServ.updatePasswordService(backendCustomer, passwordUpdateAttempt.newPassword)) {
					backendCustomer = cServ.getCustomer(backendUser.getUserID());
					Response response = new Response();
					response.fail = false;
					response.warning = "Successful";
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
				} else {
					// TODO Auto-generated catch block
					Response response = new Response();
					response.fail = true;
					response.warning = "Server failed to update password";
					ObjectMapper mapper = new ObjectMapper();
					try {
						return mapper.writeValueAsString(response);
					} catch (JsonProcessingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		}

		return "";
	}
}
