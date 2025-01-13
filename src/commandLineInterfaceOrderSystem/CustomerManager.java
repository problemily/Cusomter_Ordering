package commandLineInterfaceOrderSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CustomerManager {

	static List<String> customerPropertyTypes = Arrays.asList("Username", "Password");
	static Map<String, String> customerPropertyTypeMap = Map.of("1", "Username", "2", "Password");

	// --------------------METHODS----------------------
	// ----------------User name method-----------------

	public static String prepareUsernameAttempt() { 	// need to go back through and pass this scanner from main menu 
																		// currrently initialising everywhere, not efficient, and calling	
																		// from main menu means scanner can be closed before system exit.
		System.out.println("Please enter customer username:");
		return attemptUsername(); //returns method attempt user.name
	}

	public static String attemptUsername() {
		String enteredUsername = SQLmenu.userInput.nextLine(); // entered user name is a temporary variable within the scope of this method
														
														
		if (enteredUsername.length() < 9) {
			System.out.println("Username needs to be at least 9 characters");
			return prepareUsernameAttempt();
		} else if (!enteredUsername.matches("[A-Z|a-z|1-9]*[1-9]+[A-Z|a-z|1-9]*")) { // regex for user name needing to
																						// contain a number
			System.out.println("Username must contain a number");
			return prepareUsernameAttempt();
		} else {
			return enteredUsername; // user name entered correctly
		}
	}

	public static String updateUsernameAttempt() {
		System.out.println("Please enter new customer username:");
		return attemptUsername();
	}

	public static String updateUsername() {
		String enteredUsername = SQLmenu.userInput.nextLine(); // entered username is a temp variable within the scope of this
														// method
		if (enteredUsername.length() < 9) {
			System.out.println("Username needs to be at least 9 characters");
			return updateUsernameAttempt();
		} else if (!enteredUsername.matches("[A-Z|a-z|1-9]*[1-9]+[A-Z|a-z|1-9]*")) {
			System.out.println("Username must contain a number");
			return updateUsernameAttempt();
		} else {
			System.out.println("Username updated successfully");
			return enteredUsername; // user name entered correctly
		}
	}

	// ------------------Password Method------------------

	public static String preparePasswordAttempt() {
		System.out.println("Please enter customer password:");
		return attemptPassword();
	}

	public static String attemptPassword() {
		String enteredPassword = SQLmenu.userInput.nextLine(); // entered password is a temp variable within the scope of this
														// method
		if (enteredPassword.length() < 8) {
			System.out.println("Password needs to be at least 8 characters");
			return preparePasswordAttempt();
		} else if (!enteredPassword.matches("[A-Z|a-z|1-9]*[1-9]+[A-Z|a-z|1-9]*")) {
			System.out.println("Password must contain a number");
			return preparePasswordAttempt();
		} else {
			return enteredPassword; // password entered correctly
		}
	}

	public static String updatePasswordAttempt() { // similar method as create, but different wording and end result
		System.out.println("Please enter new customer password:");
		return updatePassword();
	}

	public static String updatePassword() {
		String enteredPassword = SQLmenu.userInput.nextLine(); // entered password is a temp variable within the scope of this
														// method
		if (enteredPassword.length() < 8) {
			System.out.println("Password needs to be at least 8 characters");
			return updatePasswordAttempt();
		} else if (!enteredPassword.matches("[A-Z|a-z|1-9]*[1-9]+[A-Z|a-z|1-9]*")) {
			System.out.println("Password must contain a number");
			return updatePasswordAttempt();
		} else {
			System.out.println("Password updated successfully");
			return enteredPassword; // password entered correctly
		}
	}

	// -------------------Email Method--------------------

	public static String prepareEmailAttempt() { // no update here as email is a unique field
		System.out.println("Please enter customer email:");
		return attemptEmail();
	}

	public static String attemptEmail() {
		String enteredEmail = SQLmenu.userInput.nextLine(); // entered email is a temp variable within the scope of this method
		if (!enteredEmail.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
			System.out.println("Email must contain '@' symbol and a domain (example .com)");
			return prepareEmailAttempt();
		} else {
			return enteredEmail; // email entered correctly
		}
	}

	// ----------------------------MAIN----------------------------------

	// -------------------------CREATE-----------------------------------

	public void createCustomer() {

		String password;
		String username;
		String email;

		{

			username = prepareUsernameAttempt(); //calls to relevant methods
			password = preparePasswordAttempt();
			email = prepareEmailAttempt();

			try {
				Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root",
						"emsql24");
				PreparedStatement pstat = c
						.prepareStatement("insert into customer(username, password, email ) values(?,?,?)"); // SQL insert into table, column, ? = user input value

				pstat.setNString(1, username); // results from create user name method
				pstat.setNString(2, password);// results from create password method
				pstat.setNString(3, email);// results from create email method

				pstat.executeUpdate();
				System.out.println("\nNew customer added: " + "\nUsername: " + username + "\nPassword: " + password
						+ "\nEmail: " + email + "\n");
			} catch (Exception e) {
				System.out.println("Error connecting to Database");
				e.printStackTrace();
			}

			SQLmenu.prepareAnythingElseAttempt(); // calls to return to SQL menu method
		}
	}

	// -------------------------DELETE-----------------------------------

	public void deleteCustomer() {

		String email;

		{

			email = prepareEmailAttempt();

			try {
				Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root",
						"emsql24");

				PreparedStatement pstat = c.prepareStatement("DELETE FROM customer WHERE email = ?"); // deletes row with this customer

				pstat.setNString(1, email); // result from email method
				pstat.executeUpdate();
				System.out.println("Customer with email: " + email + " deleted successfully\n");

			} catch (Exception e) {
				System.out.println("You cannot delete a customer if there is an order is attached"); // error message for deleting a customer with 1 or
																										// more orders within the data base
																					
				System.out.println("Error connecting to Database");
				e.printStackTrace();
			}
			SQLmenu.prepareAnythingElseAttempt(); // calls to return to SQL menu method
		}
	}

	// ---------------------------UPDATE-----------------------------------

	public void runUpdateCustomer() {
		String customerPropertyType;
		String selectedCustomer;

		System.out.println("Please provide the email of the customer you wish to update");
		selectedCustomer = getCustomerEmail();

		System.out.println("What would you like to update?");
		int typeLoopCounter = 1;
		for (String propTypes : customerPropertyTypes) { // customer prop types hashmap in scope above
			System.out.println(typeLoopCounter + ": " + propTypes);
			typeLoopCounter++;
		}

		customerPropertyType = propertyTypeAttempt();
		System.out.println("Selected property type: " + customerPropertyType);
		if (customerPropertyType.equals("Username")) {
			updateCustomerUsername(selectedCustomer); 	// If username, run update username method, else password 
		} else {										// no wierd if since it is selection key.
			updateCustomerPassword(selectedCustomer);
		}
		SQLmenu.prepareAnythingElseAttempt();
	}

	static String prepareGetCustomerEmail() {
		return getCustomerEmail();
	}

	static String getCustomerEmail() {
		Boolean doesEmailExist = false;
		String enteredEmail = SQLmenu.userInput.nextLine(); // entered email is a temp variable within the scope of this method
		if (!enteredEmail.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) { // email regex
			System.out.println("Email must contain '@' symbol and a domain (example .com)");
			return prepareGetCustomerEmail();
		} else {

			try { // use db to check if a customer with entered email exists

				Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root", // connecting to database																						
						"emsql24");

//				PreparedStatement pstat = c.prepareStatement("CREATE VIEW [Customer Email s] AS SELECT email FROM customer, SELECT*FROM customer [Customer email s]"); initial syntax attempt
				PreparedStatement pstat = c.prepareStatement("SELECT username FROM Customer_View WHERE email = ?"); // used reated view from within SQL databasse
																													

				pstat.setNString(1, enteredEmail);
				// System.out.println(doesEmailExist); log tests
				ResultSet customerUsernameQuery = pstat.executeQuery(); //checking if there is a customer who has a username with entered email
				if (customerUsernameQuery.next()) { 
					doesEmailExist = true;
				}
				// System.out.println(doesEmailExist); log tests

			} catch (Exception e) {
				System.out.println("Error connecting to Database");
				e.printStackTrace();
			}
		}

		if (doesEmailExist) // customer exists
		{
			return enteredEmail; // email entered correctly
		} else {
			System.out.println("No user exists with the email provided. Please try again.");
			return prepareGetCustomerEmail();
		}

	}

	public static String updatePropertyTypeAttempt() {
		return propertyTypeAttempt();
	}

	public static String propertyTypeAttempt() {
		String enteredType = SQLmenu.userInput.nextLine(); // entered type is a temp variable within the scope of this method
		if (!customerPropertyTypes.contains(customerPropertyTypeMap.get(enteredType))) {
			System.out.println("You must select one of the property types");
			return updatePropertyTypeAttempt();
		} else {
			return customerPropertyTypeMap.get(enteredType); // data selection entered correctly

		}
	}

	void updateCustomerUsername(String customerEmail) {
		String username;

		username = updateUsernameAttempt();

		try {

			Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root", "emsql24");

			PreparedStatement pstat = c.prepareStatement("UPDATE customer SET username = ? WHERE email = ?"); // Update table, set = column, ? = 

			pstat.setNString(1, username);
			pstat.setNString(2, customerEmail);
			pstat.executeUpdate();
			System.out.println("Successful!" + "\nCustomer: " + username + " updated\n");

		} catch (Exception e) {
			System.out.println("Error connecting to Database");
			e.printStackTrace();
		}
	}

	void updateCustomerPassword(String customerEmail) {
		String password;

		password = updatePasswordAttempt();
		try {

			Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root", "emsql24");

			PreparedStatement pstat = c.prepareStatement("UPDATE customer SET password = ? WHERE email = ?");

			pstat.setNString(1, password);
			pstat.setNString(2, customerEmail);
			pstat.executeUpdate();
			System.out.println("Successful!" + "\nCustomer" + password + "updated");

		} catch (Exception e) {
			System.out.println("Error connecting to Database");
			e.printStackTrace();
		}

	}
	// ---------------------------------VIEW-----------------------------------------------

	public void viewAllCustomers() {

		{
			try {
				Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root",
						"emsql24");

				PreparedStatement pstat = c.prepareStatement("SELECT username, email, customerID FROM CUSTOMER_VIEW"); 	//simple view, prints customer username, email and ID, 
																														//formatted as list

				ResultSet allCustomers = pstat.executeQuery(); //listed data table of executed query
				System.out.println("\nQuery processed");
				System.out
						.println("\nList of customer usernames and emails:" + "\n___________________________________");
				while (allCustomers.next()) { // for each instance in list
					String username = allCustomers.getString("username");
					String email = allCustomers.getString("email");
					String customerID = allCustomers.getString("customerID"); //named results to string
					System.out.println(
							"\nCustomer ID: " + customerID + "\n" + "Email: " + email + "\n" + "Username: " + username); //added strings to print message
				}
				System.out.println("End of List\n");

				// String viewAllCustomers = allCustomers.toString(); *previous attempts
				// System.out.println(viewAllCustomers);

			} catch (Exception e) {
				System.out.println("Error connecting to Database");
				e.printStackTrace();
			}

			System.out.println();
			SQLmenu.prepareAnythingElseAttempt(); // calls to return to SQL menu method
		}
	}

}

//PREVIOUS LOOP ATTEMPTS

//--username--

//System.out.println("Please enter customer user name:");
////next() gets what the user has typed but does not carry on down the file when called - unlike nextLine()
//if(.next().length() > 0) { // this line checks to ensure the length of what the user has entered is greater than 0 (basically ensuring it isn't nothing)
//String username = .nextLine(); // as we definitely have input, set it to username and carry on
//}	  

//--password--

//System.out.println("Please enter customer password:");
//while (!.hasNext("[A-Z|a-z|1-9]*[A-Z]+[A-Z|a-z|1-9]*") || !(.nextLine().length() > 8)) {
//System.out.println("Password must be over 8 characters and contain an upper case letter");
//} 
//password = .nextLine();

//--email--

//System.out.println("Please enter customer email: ");
//String customerEmail = .nextLine();

//------------Possible Method for returning to SQLmenu-----------------------

//public static void sqlMenu() {
//String anythingElse;
//anythingElse = prepareAnythingElseAttempt();
//System.out.println("\n" + anythingElse);
