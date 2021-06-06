package bankingApp;

import org.junit.jupiter.api.*;
import com.revature.users.*;
import com.revature.accounts.*;
import com.revature.exceptions.InvalidActionException;
import com.revature.services.AccountService;
//import com.revature.services.AdminService;
import com.revature.services.CustomerService;
import com.revature.services.EmployeeService;

import com.revature.services.UserService;
import com.revature.services.tServices;

public class testBankingApp {

	@Test
	public void testGetAccount() {

		AccountService accServ = new AccountService();
		Account a = accServ.getAccount(8);
		Assertions.assertEquals(true, a instanceof Account);

	}

	@Test
	public void testWithdrawAndDepositPending() {
		tServices tServ = new tServices();
		AccountService accServ = new AccountService();
		try {
			tServ.withdraw(500, 11);
			Assertions.assertEquals(3500, accServ.getAccount(11).getBalance());

		} catch (InvalidActionException e) {
			Assertions.assertEquals(3500, accServ.getAccount(11).getBalance());
		}
		try {
			tServ.deposit(500, 11);
			Assertions.assertEquals(3500, accServ.getAccount(11).getBalance());

		} catch (InvalidActionException e) {
			Assertions.assertEquals(3500, accServ.getAccount(11).getBalance());
		}

	}

	@Test
	public void testWithdrawAndDeposit() {
		tServices tServ = new tServices();
		AccountService accServ = new AccountService();
		try {
			tServ.withdraw(500, 1);
			Assertions.assertEquals(1350, accServ.getAccount(1).getBalance());

		} catch (InvalidActionException e) {

		}
		try {
			tServ.deposit(500, 1);
			Assertions.assertEquals(1850, accServ.getAccount(1).getBalance());

		} catch (InvalidActionException e) {

		}

	}

	@Test
	public void testTransfer() {
		tServices tServ = new tServices();
		AccountService accServ = new AccountService();
		try {
			tServ.transfer(100, 1, 8);
			Assertions.assertEquals(1750, accServ.getAccount(1).getBalance());
			Assertions.assertEquals(1170, accServ.getAccount(8).getBalance());
		} catch (InvalidActionException e) {

		}
		try {
			tServ.transfer(100, 8, 1);
			Assertions.assertEquals(1850, accServ.getAccount(1).getBalance());
			Assertions.assertEquals(1070, accServ.getAccount(8).getBalance());
		} catch (InvalidActionException e) {

		}
	}

	@Test
	public void testGetUser() {
		UserService uServ = new UserService();
		Assertions.assertEquals(true, uServ.getUser("merinolu") instanceof User);
	}

	@Test
	public void testGetCustomer() {
		CustomerService cServ = new CustomerService();
		Assertions.assertEquals("merinolu", cServ.getCustomer(1).getUserName());
	}

	@Test
	public void testSetApproved() {
		AccountService accServ = new AccountService();
		accServ.setApproved(10, "Approved");
		Assertions.assertEquals("Approved", accServ.getAccount(10).getApproved());
		accServ.setApproved(10, "Cancelled");
		Assertions.assertEquals("Cancelled", accServ.getAccount(10).getApproved());
	}

	@Test
	public void testApproveAccount() {
		EmployeeService eServ = new EmployeeService();
		AccountService accServ = new AccountService();
		Employee e = new Employee(1, "morejonpa", "pass", "Pablo", "Morejon", 2);
		eServ.approveAccount(e, 11);
		Assertions.assertEquals("Approved", accServ.getAccount(11).getApproved());
		accServ.setApproved(11, "Pending");
		Assertions.assertEquals("Pending", accServ.getAccount(11).getApproved());

	}
	// testing employeeDao getEmployee
//	EmployeeService eServ = new EmployeeService();
//	System.out.println(eServ.getEmployee(2));

	@Test
	public void testGetEmployee() {
		EmployeeService eServ = new EmployeeService();
		Assertions.assertEquals(true, eServ.getEmployee(2) instanceof Employee);
	}

//	@Test
//	public void testAdminUpdatePassword() {
//		AdminService admServ = new AdminService();
//		UserService uServ = new UserService();
//		admServ.updatePassword(uServ.getUser("merinolu"), "pass");
//		Assertions.assertEquals("pass", uServ.getUser("merinolu").getPassword());
//		admServ.updatePassword(uServ.getUser("merinolu"), "password");
//		Assertions.assertEquals("password", uServ.getUser("merinolu").getPassword());
//	}

//	@Test
//	public void testAdminUpdateName() {
//		AdminService admServ = new AdminService();
//		UserService uServ = new UserService();
//		admServ.updateName(uServ.getUser("merinolu"), "Luis Pablo", "Merino Morejon");
//		Assertions.assertEquals("Luis Pablo", uServ.getUser("merinolu").getFirstName());
//		admServ.updateName(uServ.getUser("merinolu"), "Luis", "Merino");
//		Assertions.assertEquals("Luis", uServ.getUser("merinolu").getFirstName());
//	}

	@Test
	public void testGetUserByID() {
		UserService uServ = new UserService();
		Assertions.assertEquals(true, uServ.getUser(1) instanceof User);
	}

//	@Test
//	public void testGetAdmin() {
//		AdminService admServ = new AdminService();
//		Assertions.assertEquals(true, admServ.getAdmin(12) instanceof Admin);
//	}

	// Tests that became obsolete after the implementation of database usage
//	@Test
//	public void testAddAccount() {
//		UserList ul = UserList.getInstance();
//		Customer c = new Customer("user", "pass", "Luis", "Merino");
//		Account a = new Account(ul, 19, "checking", c);
//		Assertions.assertEquals(a, c.getAccount(0));
//	}
//	@Test
//	public void testCustomerList() {
//		UserList ul = UserList.getInstance();
//		Customer d = new Customer("user2", "pass", "Luis", "Merino");
//		Customer f = new Customer("user3", "pass", "Luis", "Merino");
//		Account b = new Account(ul, 27.00, "saving", d, f);
//		
//		
//		Assertions.assertEquals(d, b.getCustomer(0));
//		Assertions.assertEquals(f, b.getCustomer(1));
//	}
//	@Test
//	public void testUserList() {
//		UserList myList = UserList.getInstance();
//		UserList myOtherList = UserList.getInstance();
//		Assertions.assertEquals(myList, myOtherList);
//		Assertions.assertEquals(myList.getCusList(), myOtherList.getCusList());
//	}
//	
//	@Test
//	public void testWithdrawPending() {
//		UserList ul = UserList.getInstance();
//		Customer u = new Customer("user4", "pass", "Luis", "Merino");
//		Account c = new Account(ul, 20.00, "checking",  u);
//		System.out.println(tServices.withdraw(19, c));
//		Assertions.assertEquals(20, c.getBalance());
//		Assertions.assertEquals("Pending", c.getApproved());
//	}
//	
//	@Test
//	public void testWithdraw() {
//		UserList ul = UserList.getInstance();
//		Customer u = new Customer("user4", "pass", "Luis", "Merino");
//		Account c = new Account(ul, 20.00, "checking",  u);
//		c.setApproved("Approved");
//		System.out.println(tServices.withdraw(19, c));
//		System.out.println(tServices.withdraw(2, c));
//		System.out.println(tServices.withdraw(0, c));
//		Assertions.assertEquals(1, c.getBalance());
//		
//	}
//	@Test
//	public void testDepositPending() {
//		UserList ul = UserList.getInstance();
//		Customer u = new Customer("user4", "pass", "Luis", "Merino");
//		Account c = new Account(ul, 20.00, "checking",  u);
//		System.out.println(tServices.deposit(19, c));
//		Assertions.assertEquals(20, c.getBalance());
//		Assertions.assertEquals("Pending", c.getApproved());
//	}
//	
//	@Test
//	public void testdeposit() {
//		UserList ul = UserList.getInstance();
//		Customer u = new Customer("user4", "pass", "Luis", "Merino");
//		Account c = new Account(ul, 20.00, "checking",  u);
//		c.setApproved("Approved");
//		System.out.println(tServices.deposit(19, c));
//		System.out.println(tServices.deposit(2, c));
//		System.out.println(tServices.deposit(0, c));
//		Assertions.assertEquals(41, c.getBalance());
//		
//	}
//	@Test
//	public void testTransferPending() {
//		UserList ul = UserList.getInstance();
//		Customer c = new Customer("user4", "pass", "Luis", "Merino");
//		Account a = new Account(ul, 20.00, "checking",  c);
//		Account b = new Account(ul, 20.00, "saving",  c);
//		System.out.println(tServices.transfer(10, a, b));
//		Assertions.assertEquals(20, a.getBalance());
//		Assertions.assertEquals(20, b.getBalance());
//		
//	}
//	@Test
//	public void testTransferCancelled() {
//		UserList ul = UserList.getInstance();
//		Customer c = new Customer("user4", "pass", "Luis", "Merino");
//		Account a = new Account(ul, 20.00, "checking",  c);
//		Account b = new Account(ul, 20.00, "saving",  c);
//		a.setApproved("Cancelled");
//		System.out.println(tServices.transfer(10, a, b));
//		Assertions.assertEquals(20, a.getBalance());
//		Assertions.assertEquals(20, b.getBalance());
//		
//	}
//	@Test
//	public void testTransferSame() {
//		UserList ul = UserList.getInstance();
//		Customer c = new Customer("user4", "pass", "Luis", "Merino");
//		Account a = new Account(ul, 20.00, "checking",  c);
//		Account b = new Account(ul, 20.00, "saving",  c);
//		System.out.println(tServices.transfer(10, a, a));
//		Assertions.assertEquals(20, a.getBalance());
//		Assertions.assertEquals(20, b.getBalance());
//		
//	}
//	@Test
//	public void testTransfer() {
//		UserList ul = UserList.getInstance();
//		Customer c = new Customer("user4", "pass", "Luis", "Merino");
//		Account a = new Account(ul, 20.00, "checking",  c);
//		Account b = new Account(ul, 20.00, "saving",  c);
//		a.setApproved("Approved");
//		b.setApproved("Approved");
//		System.out.println(tServices.transfer(10, a, b));
//		Assertions.assertEquals(10, a.getBalance());
//		Assertions.assertEquals(30, b.getBalance());
//		
//	}
//	
//
//	
//	
//	@Test
//	public void testListAccounts() {
//		UserList ul = UserList.getInstance();
//		Customer c = new Customer("user4", "pass", "Pablo", "Merino");
//		new Account(ul, 20.00, "checking",  c);
//		new Account(ul, 20.00, "saving",  c);
//		CustomerService.listAccounts(c);
//		
//	}
//	@Test
//	public void testFindCustomer() {
//		UserList ul = UserList.getInstance();
//		Customer c = new Customer("user100", "pass", "Pablo", "Merino");
//		ul.addCustomer(c);
//		
//		Assertions.assertEquals(c, UserService.findCustomer(ul, "user100"));	
//		
//	}
//	@Test
//	public void testFindCustomerNull() {
//		UserList ul = UserList.getInstance();
//		Assertions.assertEquals(null, UserService.findCustomer(ul, "impossible"));
//	}
//	@Test
//	public void testFindEmployee() {
//		UserList ul = UserList.getInstance();
//		Employee e = new Employee("emp100", "pass", "pablo", "morejon");
//		ul.addEmployee(e);
//		
//		Assertions.assertEquals(e, UserService.findEmployee(ul, "emp100"));
//	}
//	@Test
//	public void testFindEmployeeNull() {
//		UserList ul = UserList.getInstance();
//		
//		Assertions.assertEquals(null, UserService.findEmployee(ul, "impossible2"));
//	}
//	@Test
//	public void testFindAccount() {
//		UserList ul = UserList.getInstance();
//		Customer c = new Customer("user4", "pass", "Pablo", "Merino");
//		ul.addCustomer(c);
//		Account a = new Account(ul, 20.00, "checking",  c);
//		Assertions.assertEquals(a, UserListService.findAccount(ul, a.getAccountNumber()));
//	}
//	@Test
//	public void testFindAccountNull() {
//		UserList ul = UserList.getInstance();
//		Customer c = new Customer("user4", "pass", "Pablo", "Merino");
//		ul.addCustomer(c);
//		new Account(ul, 20.00, "checking",  c);
//		Assertions.assertEquals(null, UserListService.findAccount(ul, 123));
//	}
//	
//	
//	@Test
//	public void testPendingAccounts() {
//		UserList ul = UserList.getInstance();
//		Customer cus = new Customer("user1000", "pass", "Pablo", "piddy");
//		new Account(ul, 20.00, "checking",  cus);
//		
//		Assertions.assertEquals(1, EmployeeService.pendingAccounts(cus));
//	}

}
