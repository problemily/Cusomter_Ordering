package commandLineInterfaceOrderSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderManager {

	static List<String> itemNames = new ArrayList<>(); // for selecting items to add to order
	static HashMap<String, String> itemMap = new HashMap<>(); 

	static List<String> selectedItems = new ArrayList<>(); //storing items selected

	static List<String> orderIDs = new ArrayList<>(); // for selecting orderIDs to delte from orders
	static HashMap<String, String> orderIdMap = new HashMap<>(); // 

	static List<String> selectedIDs = new ArrayList<>(); // storing selected ids

	static List<String> orderPropertyTypes = Arrays.asList("Add an item", "Delete an item");
	static Map<String, String> orderPropertyTypeMap = Map.of("1", "Add an item", "2", "Delete an item"); // for selecting what property 
																										// you would like to update within an order (Add or Delete)


	// ---------------------METHODS--------------------------

	static void cleanUp() {  //empties all array lists for next query selected
		itemNames = new ArrayList<>();
		itemMap = new HashMap<>(); 
		selectedItems = new ArrayList<>();
		orderIDs = new ArrayList<>();
		orderIdMap = new HashMap<>(); 
		selectedIDs = new ArrayList<>();
	}
	
	// ---------------customer details method-----------------------------

	public static String prepareCustomerDetailsAttempt() {
		System.out.println("Please enter the customer's email attached to this order: "); 
		return attemptCustomerDetails();
	}

	public static String attemptCustomerDetails() {
		String enteredDetails = SQLmenu.userInput.nextLine();
		if (!enteredDetails.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) { //error handling
			System.out.println("Email must contain '@' symbol and a domain (example .com)"); 
			return prepareCustomerDetailsAttempt();
		} else {
			return enteredDetails;
		}
	}
	// -----------------delete customer details from orders---------------

	public static String prepareDeleteCustomerDetailsAttempt() {
		System.out.println("Please enter the customer's email for the order you wish to delete ");
		return attemptDeleteCustomerDetails();
	}

	public static String attemptDeleteCustomerDetails() {
		String enteredDetails = SQLmenu.userInput.nextLine();
		if (!enteredDetails.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) { //error handling
			System.out.println("Email must contain '@' symbol and a domain (example .com)");
			return prepareDeleteCustomerDetailsAttempt();
		} else {
			return enteredDetails;
		}
	}
	// -----------------------order details method-------------------------

	public static void prepareOrderDetailsAttempt() {

		try {

			Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root", "emsql24");

			PreparedStatement pstat = c.prepareStatement("SELECT itemName FROM ITEMS_VIEW"); //shows list of items to user, to be added to order

			ResultSet allItems = pstat.executeQuery();

			System.out.println("\nList of item names  " + "\n___________________________________");
			while (allItems.next()) {
				String itemName = allItems.getString("itemName");
				itemNames.add(itemName);

				String itemKey = String.valueOf(itemMap.size() + 1); //incrementing list
				itemMap.put(itemKey, itemName);
				System.out.println(itemKey + ": " + itemName); //prints selectable items
			}
		} catch (Exception e) {
			System.out.println("Error connecting to Database");
			e.printStackTrace();
		}
		beginOrderDetailsAttempt(); //calls method were above items can be selected
	}

	static void beginOrderDetailsAttempt() { 
		attemptOrderDetails();
	}

	public static void attemptOrderDetails() {
		System.out.println("\nSelect an item from the list above to add to the customer order");
		String enteredItem = SQLmenu.userInput.nextLine();
//		System.out.println("test" + enteredItem); log tests
//		System.out.println(itemNames.contains(enteredItem)); log tests
		if (itemNames.contains(itemMap.get(enteredItem))) {
			System.out.println("Items added to order");
			selectedItems.add(itemMap.get(enteredItem));
			addAnotherItem();
		} else {
			System.out.println("That order does not exist, please try again.");
			beginOrderDetailsAttempt();
		}
	}

	public static void addAnotherItem() { //method for adding multiple items to order
		System.out.println("Would you like to add another item? Y/N");
		String enteredAnswer = SQLmenu.userInput.nextLine();
		if (enteredAnswer.equalsIgnoreCase("Y") || enteredAnswer.equalsIgnoreCase("N")) { 	// a little wierd, but needed it to only attempt OrderDetails method
																							// if yes as selected, and nothing to happen if No, and run add another
																							// item method if anything other than Y/N was entered.
			if (enteredAnswer.equalsIgnoreCase("Y")) {
				attemptOrderDetails(); //returns to above method
			}
		} else {
			System.out.println("You must state Y / N"); //error handling 
			addAnotherItem();
		}
	}

	// delete order

	public static void prepareDeleteOrderDetailsAttempt(String customerID) { // should this be orderID and customer ID? - no it should not!
																																													
		try {

			Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root", "emsql24");

			PreparedStatement getOrderId = c
					.prepareStatement("SELECT ordersID, customerID, itemNames FROM Order_view WHERE customerID = ?"); //deletes an order from DB
			getOrderId.setNString(1, customerID); // AD new
			ResultSet orderResult = getOrderId.executeQuery();
			while (orderResult.next()) {
				String orderId = orderResult.getString("ordersID");
				orderIDs.add(orderId);
				String itemNames = orderResult.getString("itemNames");
				String iDKey = String.valueOf(orderIdMap.size() + 1);
				orderIdMap.put(iDKey, orderId); // this shows all orders attached to entered customer
				System.out.println(iDKey + ": " + "Order ID - " + orderId + " | Item Names - " + itemNames);
			}
		} catch (Exception e) {
			System.out.println("Error connecting to Database"); //error handling 
			e.printStackTrace();
		}
		deleteOrderDetailsAttempt();
	}

	static void beginOrderDeletionAttempt() {
		attemptOrderDetails();
	}

	public static void deleteOrderDetailsAttempt() {
		System.out.println("\nSelect an order ID from the list above to delete from customer orders");
		String enteredID = SQLmenu.userInput.nextLine();
		if (orderIDs.contains(orderIdMap.get(enteredID))) {
			System.out.println("Order added to deletion request");
			selectedIDs.add(orderIdMap.get(enteredID)); // this allows user to select what order to delete
			addAnotherOrder();
		} else {
			System.out.println("That order does not exist, please try again."); //error handling
			beginOrderDeletionAttempt();
			
		}
			orderIDs.removeAll(selectedIDs);
	}

	public static void addAnotherOrder() {
		System.out.println("Would you like to add another order? Y/N");
		String enteredAnswer = SQLmenu.userInput.nextLine();
		if (enteredAnswer.equalsIgnoreCase("Y") || enteredAnswer.equalsIgnoreCase("N")) { //if no do nothing, if yes delete method
			if (enteredAnswer.equalsIgnoreCase("Y")) {
				deleteOrderDetailsAttempt();
			}
		} else {
			System.out.println("You must state Y / N"); //error handling
			addAnotherItem();
		}
	}
	
	// ----------------------------MAIN----------------------------------

	// --------------------------------CREATE------------------------------------------
	public void createOrder() {
		String email;
		{
			email = prepareCustomerDetailsAttempt();
			prepareOrderDetailsAttempt();

			try {
				Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root",
						"emsql24");

				PreparedStatement pstat = c
						.prepareStatement("INSERT INTO orders(customerID, totalPrice, ItemNames) values(?,?,?)");

				String formattedItems = "";

				for (String item : selectedItems) {
					if (formattedItems.length() > 0) {
						formattedItems = formattedItems + ", " + item;
					} else {
						formattedItems = item;
					}
				}

				PreparedStatement getCustomerId = c.prepareStatement("SELECT customerID FROM customer WHERE email = ?"); //gets id from entered email
				getCustomerId.setNString(1, email);
				ResultSet customerResult = getCustomerId.executeQuery();  //listed data table of executed query
				customerResult.next(); // for each instance in list
				String customerId = customerResult.getString("customerID"); //data to string
				// System.out.println(customerId); log test

				double totalPrice = 0;
				for (String item : selectedItems) {
					PreparedStatement getItemPrice = c
							.prepareStatement("SELECT itemPrice FROM items WHERE ItemName = ?");
					// System.out.println(item);
					getItemPrice.setNString(1, item);
					ResultSet itemPriceResult = getItemPrice.executeQuery();  //listed data table of executed query
					itemPriceResult.next(); // for each instance in list
					String itemPrice = itemPriceResult.getString("itemPrice");
					// System.out.println(itemPrice);
					totalPrice += Double.parseDouble(itemPrice); //data to string from double
				}

				pstat.setNString(1, customerId);
				pstat.setNString(2, String.format("%.2f", totalPrice)); //formats price to 2 decimals
				pstat.setNString(3, formattedItems);
				pstat.executeUpdate();
				cleanUp();
				System.out.println("Order created!");
			} catch (Exception e) {
				System.out.println("Error connecting to Database"); //error handling
				e.printStackTrace();
			}

			SQLmenu.prepareAnythingElseAttempt();// calls to return to SQL menu method
		}
	}

	// -------------------------DELETE-----------------------------------
	public void deleteOrder() {
		String email;
		{
			email = prepareDeleteCustomerDetailsAttempt();

			try {

				Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root",
						"emsql24");

				PreparedStatement getCustomerId = c.prepareStatement("SELECT customerID FROM customer WHERE email = ?"); //gets customer ID from entered email

				getCustomerId.setNString(1, email);
				ResultSet customerResult = getCustomerId.executeQuery();
				customerResult.next();
				String customerId = customerResult.getString("customerID");

				prepareDeleteOrderDetailsAttempt(customerId); //calls to method that list orders with ids to select

				for (String id : selectedIDs) { // selected id with id map
					PreparedStatement pstat = c.prepareStatement("DELETE FROM orders WHERE ordersID = ?");
					pstat.setNString(1, id);
					pstat.executeUpdate(); // updates database
						// clears list for next query
					System.out.println("Order: " + id + " deleted successfully\n");
				}
				 // was using selectedIDs.clear() but this resulted in java.util.ConcurrentModificationException error
				//did some research, found this error was to do with itterating over arrays, and subsquently researched
				//correct syndax to use and created a method called cleanUP to overwrite with empty arrays after each query
				cleanUp();
			} catch (Exception e) {
				System.out.println("Error connecting to Database");
				e.printStackTrace();
			}
			SQLmenu.prepareAnythingElseAttempt(); // calls to return to SQL menu method
		}
	}
	// -------------------------UPDATE-----------------------------------

	public void runUpdateOrder() {
		String orderPropertyType;
		String selectedCustomerEmail;
		String selectedCustomerId;
		

		System.out.println("Please provide the email of the customer whose order you wish to update");
		selectedCustomerEmail = getCustomerEmail(); //below in update helpers
		selectedCustomerId = getCustomerIdFromEmail(selectedCustomerEmail);
		

		System.out.println("What would you like to do?");
		int typeLoopCounter = 1;
		for (String orderPropTypes : orderPropertyTypes) { //same idea as customer/item prop types
			System.out.println(typeLoopCounter + ": " + orderPropTypes);
			typeLoopCounter++;
		}

		orderPropertyType = propertyTypeAttempt();
		System.out.println("Selected property type: " + orderPropertyType);
		if (orderPropertyType.equals("Add an item")) { //checks user input and directs to correct method
			prepareItemChangeToOrderDetailsAttempt(selectedCustomerId);
			addItemToOrder(); //calls to update methods add to order
			} else { 
			prepareItemChangeToOrderDetailsAttempt(selectedCustomerId);
			deleteItemFromOrder(); //calls to update methods delete from order
		}
		cleanUp();
		SQLmenu.prepareAnythingElseAttempt();
	}	
		//-----------------Add to order------------------------------
		
		static void addItemToOrder() {


			try {

				Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root", "emsql24");

				PreparedStatement orderItemNames = c.prepareStatement("UPDATE orders SET itemNames = CONCAT(itemNames, ?) WHERE ordersID = ?"); // Update table, set = column, ? = userinput
				

				String formattedItems = "";

				for (String item : selectedItems) {
						formattedItems = formattedItems + ", " + item; //formats items into comma seperated list within database
				}
																																																						
				orderItemNames.setNString(1, formattedItems);
				orderItemNames.setInt(2, Integer.parseInt(selectedIDs.getFirst()));
				orderItemNames.executeUpdate();

				PreparedStatement NewPrice = c.prepareStatement("UPDATE orders SET totalPrice = ? WHERE ordersID = ?");

				double newItemsTotalPrice = 0;
				for (String item : selectedItems) {
					PreparedStatement getItemPrice = c.prepareStatement("SELECT itemPrice FROM items WHERE ItemName = ?");
					// System.out.println(item);
					getItemPrice.setNString(1, item);
					ResultSet itemPriceResult = getItemPrice.executeQuery();
					itemPriceResult.next();
					String itemPrice = itemPriceResult.getString("itemPrice");
					newItemsTotalPrice += Double.parseDouble(itemPrice); // this method takes items selected to be added to order and totals the price
				}
				
				double currentPriceTotal = 0;
				PreparedStatement getCurrentOrderTotal = c.prepareStatement("SELECT totalPrice FROM orders WHERE ordersID = ?");
				// System.out.println(itemPrice);
				getCurrentOrderTotal.setInt(1, Integer.parseInt(selectedIDs.getFirst()));
				ResultSet currentOrderPrice = getCurrentOrderTotal.executeQuery();
				currentOrderPrice.next();
				String currentTotal = currentOrderPrice.getString("totalPrice");
				currentPriceTotal +=  Double.parseDouble(currentTotal); // this method calls to database to get current price total
				
				double newOrderTotal = currentPriceTotal + newItemsTotalPrice; // new total adds the new items and current total together
				
				NewPrice.setNString(1, String.format("%.2f", newOrderTotal)); // formats as 2 decimals
				NewPrice.setInt(2, Integer.parseInt(selectedIDs.getFirst()));
				NewPrice.executeUpdate();
				System.out.println("Successful!" + "Items updated in customer's order." + "\nNew Price = " + String.format("%.2f", newOrderTotal) + 
						 "\n Selected items: " + formattedItems);
				
			} catch (Exception e) {
				System.out.println("Error connecting to Database");
				e.printStackTrace();
			}
				
	}
	//----------------------Delete from order---------------------------
		
		static void deleteItemFromOrder() {


			try {

				Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root", "emsql24");
				

				PreparedStatement getCurrentOrderItemNames = c.prepareStatement ("Select itemNames FROM orders WHERE ordersID = ?");
				
				getCurrentOrderItemNames.setInt(1, Integer.parseInt(selectedIDs.getFirst()));
				ResultSet currentOrderDetails = getCurrentOrderItemNames.executeQuery();
				currentOrderDetails.next();
				String currentItems = currentOrderDetails.getString("itemNames");
				
				String[] currentItemsList = currentItems.split(", ");
				ArrayList<String> currentOrderItems = new ArrayList<>(Arrays.asList(currentItemsList));
				
			
				for(String item : selectedItems) {
					currentOrderItems.remove(item); // for each item selected, remove individually from list
				}
			
	
				PreparedStatement orderItemNames = c.prepareStatement("UPDATE orders SET itemNames = REPLACE(itemNames, ?, ?) WHERE ordersID = ?"); // Update table, set = column, ? = 
				

				String formattedItems = "";

				for (String item : currentOrderItems) {
					if (formattedItems.length() > 0) {
						formattedItems = formattedItems + ", " + item;
					} else {
						formattedItems = item;
					}
				}
																																																						
				orderItemNames.setNString(1, currentItems);
				orderItemNames.setNString(2, formattedItems);
				orderItemNames.setInt(3, Integer.parseInt(selectedIDs.getFirst()));
				orderItemNames.executeUpdate();
				
				PreparedStatement NewPrice = c.prepareStatement("UPDATE orders SET totalPrice = ? WHERE ordersID = ?");

				double newItemsTotalPrice = 0;
				for (String item : selectedItems) {
					PreparedStatement getItemPrice = c.prepareStatement("SELECT itemPrice FROM items WHERE ItemName = ?");
					// System.out.println(item);
					getItemPrice.setNString(1, item);
					ResultSet itemPriceResult = getItemPrice.executeQuery();
					
					itemPriceResult.next();
					String itemPrice = itemPriceResult.getString("itemPrice");
					newItemsTotalPrice += Double.parseDouble(itemPrice);
				}
				
				double currentPriceTotal = 0;
				PreparedStatement getCurrentOrderTotal = c.prepareStatement("SELECT totalPrice FROM orders WHERE ordersID = ?");
				// System.out.println(itemPrice);
				getCurrentOrderTotal.setInt(1, Integer.parseInt(selectedIDs.getFirst()));
				ResultSet currentOrderPrice = getCurrentOrderTotal.executeQuery();
				currentOrderPrice.next();
				String currentTotal = currentOrderPrice.getString("totalPrice");
				currentPriceTotal +=  Double.parseDouble(currentTotal);
				
				double newOrderTotal = currentPriceTotal - newItemsTotalPrice;
				
				NewPrice.setNString(1, String.format("%.2f", newOrderTotal));
				NewPrice.setInt(2, Integer.parseInt(selectedIDs.getFirst()));
				NewPrice.executeUpdate();
				System.out.println("Successful!" + "Items updated in customer's order." + "\nNew Price = " + String.format("%.2f", newOrderTotal)
						+ "\n Selected items: " + formattedItems);

			} catch (Exception e) {
				System.out.println("Error connecting to Database");
				e.printStackTrace();
			}
			
	}

	//---------------- MAIN update helpers-------------------
		
	//Property Types

	public static String updatePropertyTypeAttempt() {
		return propertyTypeAttempt();
	}

	public static String propertyTypeAttempt() {
		String enteredType = SQLmenu.userInput.nextLine(); // entered type is a temp variable within the scope of this method
		if (!orderPropertyTypes.contains(orderPropertyTypeMap.get(enteredType))) {
			System.out.println("You must select one of the property types");
			return updatePropertyTypeAttempt();
		} else {
			return orderPropertyTypeMap.get(enteredType); // data selection entered correctly

		}
	}

	//Get customer email 
	
	static String prepareGetCustomerEmail() {
		return getCustomerEmail();
	}

	public static String getCustomerEmail() {
		Boolean doesEmailExist = false;
		String enteredEmail = SQLmenu.userInput.nextLine(); // entered email is a temp variable within the scope of this method
		if (!enteredEmail.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) { // email regex
			System.out.println("Email must contain '@' symbol and a domain (example .com)");
			return prepareGetCustomerEmail();
		} else {

			try { // use db to check if a customer with entered email exists

				Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root", // connecting to database																							
						"emsql24");

//				PreparedStatement pstat = c.prepareStatement("CREATE VIEW [Customer Email s] AS SELECT email FROM customer, SELECT*FROM customer [Customer email s]"); *initial syntax attempt
				
				PreparedStatement pstat = c.prepareStatement("SELECT username FROM Customer_View WHERE email = ?"); // used created view from within SQL databasse
																													

				pstat.setNString(1, enteredEmail);
				// System.out.println(doesEmailExist); log tests
				ResultSet customerUsernameQuery = pstat.executeQuery(); //again checking if there is username with that email attached
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
	
	//Get Customer ID
	
	static public String getCustomerIdFromEmail(String email) { //gets the customer id associated with the enetred email
		Boolean doesCustomerExist = false;
		String customerId = "";
		try { // use db to check if a customer with entered email exists

			Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root", // connecting to database																							
					"emsql24");

			PreparedStatement pstat = c.prepareStatement("SELECT customerID FROM Customer_View WHERE email = ?"); // used created view from within SQL databasse
																										
			pstat.setNString(1, email);
			
			ResultSet customerIdQuery = pstat.executeQuery(); //again checking if there is username with that email attached
			if (customerIdQuery.next()) {
				doesCustomerExist = true;
				customerId = String.valueOf(customerIdQuery.getInt("customerID"));
			}

		} catch (Exception e) {
			System.out.println("Error connecting to Database");
			e.printStackTrace();
		}
	

	if (doesCustomerExist) // customer exists
	{
		return customerId; // email entered correctly
	} else {
		System.out.println("Unable to find cusotmer ID associated with entered email");
		System.out.println("Program terminating");
		System.exit(0); //this error should never appear, but in the case it does the program terminates for user to restart
		return "";
	}
}

	//Item Changes methods (ADD and DELETE)
	
		public static void prepareItemChangeToOrderDetailsAttempt(String customerID) { 
			
			try {

				Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root", "emsql24");

				PreparedStatement getOrderId = c
						.prepareStatement("SELECT ordersID, customerID, itemNames FROM Order_view WHERE customerID = ?"); 
				getOrderId.setNString(1, customerID); // AD new
				ResultSet orderResult = getOrderId.executeQuery();
				while (orderResult.next()) {
					String orderId = orderResult.getString("ordersID");
					orderIDs.add(orderId);
					String itemNames = orderResult.getString("itemNames");
					String iDKey = String.valueOf(orderIdMap.size() + 1);
					orderIdMap.put(iDKey, orderId);
					System.out.println(iDKey + ": " + "Order ID - " + orderId + " | Item Names - " + itemNames); // selectable list for user
				}
			} catch (Exception e) {
				System.out.println("Error connecting to Database"); //error handling
				e.printStackTrace();
			}
			selectOrderDetailsAttempt();
		}

		static void beginItemChangeToOrderDetailsAttempt() {
			updatePrepareOrderDetailsAttempt();
		}
		
		public static void updatePrepareOrderDetailsAttempt() {

			try {

				Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root", "emsql24");

				PreparedStatement pstat = c.prepareStatement("SELECT itemName FROM ITEMS_VIEW");

				ResultSet allItems = pstat.executeQuery(); //all times in list

				System.out.println("\nList of item names  " + "\n___________________________________");
				while (allItems.next()) {
					String itemName = allItems.getString("itemName");
					itemNames.add(itemName);

					String itemKey = String.valueOf(itemMap.size() + 1); //incrementing list
					itemMap.put(itemKey, itemName);
					System.out.println(itemKey + ": " + itemName); //prints selectable items 
				}
			} catch (Exception e) {
				System.out.println("Error connecting to Database");
				e.printStackTrace();
			}
			updateAttemptOrderDetails(); //calls method were above items can be selected
		}

		public static void updateAttemptOrderDetails() {
			System.out.println("\nSelect an item from the list above");
			String enteredItem = SQLmenu.userInput.nextLine();
//			System.out.println("test" + enteredItem); log tests
//			System.out.println(itemNames.contains(enteredItem)); log tests
			if (itemNames.contains(itemMap.get(enteredItem))) {
				//System.out.println("Items added to order");
				selectedItems.add(itemMap.get(enteredItem));
				updateAddAnotherItem();
			} else {
				System.out.println("That item does not exist, please try again.");
				updateAttemptOrderDetails();
			}
		}

		public static void selectOrderDetailsAttempt() {
			System.out.println("\nSelect an order ID from the list above operate on"); //selects order ID from user input email
			String enteredID = SQLmenu.userInput.nextLine();
			if (orderIDs.contains(orderIdMap.get(enteredID))) {
				System.out.println("Order ID selected");
				selectedIDs.add(orderIdMap.get(enteredID)); //selects order ID from user input email
				beginItemChangeToOrderDetailsAttempt();
			} else {
				System.out.println("That order does not exist, please try again."); //error handling
				selectOrderDetailsAttempt();
				orderIDs.clear();
			}
		}
		public static void updateAddAnotherItem() { //method for adding multiple items to order
			System.out.println("Would you like to select another item? Y/N");
			String enteredAnswer = SQLmenu.userInput.nextLine();
			if (enteredAnswer.equalsIgnoreCase("Y") || enteredAnswer.equalsIgnoreCase("N")) {
				if (enteredAnswer.equalsIgnoreCase("Y")) {
				updateAttemptOrderDetails();	// if yes as selected, and nothing to happen if No, and run add another // item method if anything other than Y/N was entered.
			
				}
			
			} else {
				System.out.println("You must state Y / N"); //error handling
				updateAddAnotherItem();
			}
		}


	// -------------------------VIEW---------------------------------------
	
	public void viewAllOrders() {

		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root", "emsql24"); 	//simple view, prints customer username, email and ID, 
																															//formatted as list

			PreparedStatement pstat = c.prepareStatement("SELECT ordersID, customerID, itemNames FROM ORDER_VIEW");

			ResultSet allItems = pstat.executeQuery();
			System.out.println("\nQuery processed");
			System.out.println(
					"\nList of item names, descriptions and prices:" + "\n___________________________________");
			while (allItems.next()) {
				String ordersId = allItems.getString("ordersID");
				String customerId = allItems.getString("customerID");
				String itemNames = allItems.getString("itemNames");
				System.out.println("\nOrder ID: " + ordersId + "\n" + "Customer ID: " + customerId + "\n"
						+ "Order Details: " + itemNames);
			}
			System.out.println("\n___________________________________" + "\n\tEnd of List");

		} catch (Exception e) {
			System.out.println("Error connecting to Database");
			e.printStackTrace();
		}

		System.out.println();
		SQLmenu.prepareAnythingElseAttempt(); // calls to return to SQL menu method
	}
}