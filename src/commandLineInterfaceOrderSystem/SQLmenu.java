package commandLineInterfaceOrderSystem;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SQLmenu {

	static List<String> dataTypes = Arrays.asList("Customers", "Items", "Orders");
	static List<String> queryTypes = Arrays.asList("Create", "Delete", "Update", "View");

	static Map<String, String> dataTypeMap = Map.of("1", "Customers", "2", "Items", "3", "Orders");

	static Map<String, String> queryTypeMap = Map.of("1", "Create", "2", "Delete", "3", "Update", "4", "View");
	
	public static Scanner userInput = new Scanner(System.in);

	public static void main(String[] args) {
		runMainMenu();
	}

	// -----METHODS-----

	// public static String programExit(String exit) { Idea saved for later // this is now further down in helpers
	// System.out.println("Program will be terminated")
	// }

	// ---Run Main Menu--------

	public static void runMainMenu() { //run main menu method, what users first see when running program
		String selectedDataType; //comment for build test
		String selectedQuery;

		System.out.println("Welcome to Lepick Pharmacy's customer ordering system."
				+ "\n_____________________________________________________\n");

		System.out.println("Please select which data type you wish to operate on:");
		int dataLoopCounter = 1;
		for (String data : dataTypes) {
			System.out.println(dataLoopCounter + ": " + data); //in scope from array list and map above
			dataLoopCounter++;
		}

		selectedDataType = prepareDataTypeAttempt(); // method below in menu helpers
		System.out.println("Selected data type: " + selectedDataType);

		System.out.println("\nPlease select which query you would like to use:");
		int queryLoopCounter = 1;
		for (String query : queryTypes) {
			System.out.println(queryLoopCounter + ": " + query);
			queryLoopCounter++;
		}

		selectedQuery = prepareQueryAttempt();
		System.out.println("Selected query type: " + selectedQuery);
		
		// switches between type selected eg, if customer data type selected, 
		// and delete query - run delete method from customer manager
		// same for all three data types

		switch (selectedDataType) {
		case "Customers": {
			CustomerManager customerManager = new CustomerManager();
																
			switch (selectedQuery) {
			case "Create": {
				customerManager.createCustomer();
				break;
			}
			case "Delete": {
				customerManager.deleteCustomer();
				break;
			}
			case "Update": {
				customerManager.runUpdateCustomer();
				break;
			}
			case "View": {
				customerManager.viewAllCustomers();
				break;
			}
			}

		}
		case "Items": {
			ItemManager itemManager = new ItemManager();
			switch (selectedQuery) {
			case "Create": {
				itemManager.createItem();
				break;
			}
			case "Delete": {
				itemManager.deleteItem();
				break;
			}
			case "Update": {
				itemManager.runUpdateItem();
				break;
			}
			case "View": {
				itemManager.viewAllItems();
				break;
			}
			}
		}
		case "Orders": {
			OrderManager orderManager = new OrderManager();
			switch (selectedQuery) {
			case "Create": {
				orderManager.createOrder();
				break;
			}
			case "Delete": {
				orderManager.deleteOrder();
				break;
			}
			case "Update": {
				orderManager.runUpdateOrder();
				break;
			}
			case "View": {
				orderManager.viewAllOrders();
				break;
			}
			}
		}

		}
	}

	// ---menu helpers----
	
	public static String prepareDataTypeAttempt( ) {
		return dataTypeAttempt();
	}

	public static String dataTypeAttempt( ) {
		String enteredData = userInput.nextLine(); // entered data is a temp variable within the scope of this method
		if (!dataTypes.contains(dataTypeMap.get(enteredData))) {
			System.out.println("You must select one of the data types");
			return prepareDataTypeAttempt();
		} else {
			return dataTypeMap.get(enteredData); // data selection entered correctly
		}
	}

	public static String prepareQueryAttempt( ) {
		return queryAttempt();
	}

	public static String queryAttempt( ) {
		String enteredQuery = userInput.nextLine(); // entered query is a temp variable within the scope of this method
		if (!queryTypes.contains(queryTypeMap.get(enteredQuery))) {
			System.out.println("You must select one of the query types");
			return prepareQueryAttempt();
		} else {
			return queryTypeMap.get(enteredQuery); // query selection entered correctly
		}
	}
	// ------Return to SQLMenu------------

	public static void prepareAnythingElseAttempt( ) {
		System.out.println("Would you like to do anything else? State Y / N");
		attemptAnythingElse();
	}

	public static void attemptAnythingElse( ) {
		String enteredAnswer = userInput.nextLine(); // entered answer is a temp variable within the scope of this
														// method
		if (enteredAnswer.equalsIgnoreCase("Y")) {
			SQLmenu.runMainMenu();
		} else if (enteredAnswer.equalsIgnoreCase("N")) {
			System.out.println("Exiting program");
			userInput.close();//scanner closes here before system exit, avoiding memory leak
			System.exit(0); //terminates program
			
		} else {
			System.out.println("You must state Y / N");
			prepareAnythingElseAttempt(); //re run method is neither y/n entered
		}
	}
}
