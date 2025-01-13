package commandLineInterfaceOrderSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemManager {
	static List<String> itemPropertyTypes = Arrays.asList("Item name", "Item description", "Item price");
	static Map<String, String> itemPropertyTypeMap = Map.of("1", "Item name", "2", "Item description", "3",
			"Item price");

	// --------------------METHODS----------------------

	// ----------------Item Name Method-----------------

	public static String prepareItemNameAttempt() {
		System.out.println("Please enter the name of the item to be added to inventory:");
		return attemptItemName();
	}

	public static String attemptItemName() {
		String enteredItemName = SQLmenu.userInput.nextLine(); // entered item name is a temp variable within the scope of this
														// method
		if (enteredItemName.length() < 4) {
			System.out.println("Item name needs to be at least 4 characters");
			return prepareItemNameAttempt();
		} else {
			return enteredItemName; // user name entered correctly
		}
	}
//update item method

	public static String updateItemNameAttempt() {
		System.out.println("Please enter the new name of the item to be added to inventory:"); //update version of method for approprite print out
		return attemptItemName();
	}

	public static String updateItemName() {
		String enteredItemName = SQLmenu.userInput.nextLine(); // entered item name is a temp variable within the scope of this method
														
		if (enteredItemName.length() < 4) {
			System.out.println("Item name needs to be at least 4 characters");
			return updateItemNameAttempt();
		} else {
			System.out.println("Item name updated successfully");
			return enteredItemName; // item name entered correctly
		}
	}
//delete item method

	public static String deleteItemNameAttempt() {
		System.out.println("Please enter the name of the item to be deleted from inventory: "); //delete version of method for approprite print out
		return deleteItemName();
	}

	public static String deleteItemName() {
		String enteredItemName = SQLmenu.userInput.nextLine(); // entered item name is a temp variable within the scope of this
														// method
		if (enteredItemName.length() < 4) {
			System.out.println("Item name needs to be at least 4 characters");
			return deleteItemNameAttempt();
		} else {
			System.out.println("Item deleted successfully");
			return enteredItemName; // item name entered correctly
		}
	}

	// ------------------Item Description Method------------------

	public static String prepareItemDescriptionAttempt() {
		System.out.println("Please enter a short description of the item: ");
		return attemptItemDescription();
	}

	public static String attemptItemDescription() {
		String enteredDescription = SQLmenu.userInput.nextLine(); // entered description is a temp variable within the scope of this method
									
		if (enteredDescription.length() < 45) {
			System.out.println("Description needs to be at least 45 characters (one sentence)");
			return prepareItemDescriptionAttempt();
		} else {
			return enteredDescription; // description entered correctly
		}
	}

// update description 

	public static String updateItemDescriptionAttempt() {
		System.out.println("Please enter new item description:");
		return updateItemDescription();
	}

	public static String updateItemDescription() {
		String enteredItemDescription = SQLmenu.userInput.nextLine(); // entered description is a temp variable within the scope of this method
															
		if (enteredItemDescription.length() < 45) {
			System.out.println("Description needs to be at least 45 characters (one sentence)");
			return updateItemDescriptionAttempt();
		} else {
			System.out.println("Description updated successfully");
			return enteredItemDescription; // description entered correctly
		}
	}

	// -------------------Item price method--------------------

	public static String prepareItemPriceAttempt() {
		System.out.println("Please enter item price: ");
		return attemptItemPrice();
	}

	public static String attemptItemPrice() {
		String enteredItemPrice = SQLmenu.userInput.nextLine(); // entered email is a temp variable within the scope of this method											
		if (!enteredItemPrice.matches("\\d{1,2}+\\.\\d{2}")) {
			System.out.println("Item price must be in pounds and pence, e.g. 4.99, 5.00");
			return prepareItemPriceAttempt();
		} else {
			return enteredItemPrice; // email entered correctly, succeeds
		}
	}

//update price

	public static String updateItemPriceAttempt() {
		System.out.println("Please enter new item price:");
		return updateItemPrice();
	}

	public static String updateItemPrice() {
		String enteredItemPrice = SQLmenu.userInput.nextLine(); // entered password is a temp variable within the scope of this method
		if (!enteredItemPrice.matches("\\d{1,2}+\\.\\d{2}")) {
			System.out.println("Item price must be in pounds and pence, e.g. 4.99, 5.00");
			return updateItemPriceAttempt();
		} else {
			System.out.println("Item price updated successfully");
			return enteredItemPrice; // password entered correctly
		}
	}

	// ----------------------------MAIN----------------------------------

	// -------------------------CREATE-----------------------------------

	public void createItem() {

		String itemName;
		String itemDescription;
		String itemPrice;

		{

			itemName = prepareItemNameAttempt(); //calls relevant methods
			itemDescription = prepareItemDescriptionAttempt();
			itemPrice = prepareItemPriceAttempt();

			try {
				Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root",
						"emsql24");

				PreparedStatement pstat = c
						.prepareStatement("INSERT INTO items (itemName, itemDescription, itemPrice ) values(?,?,?)");

				pstat.setNString(1, itemName);
				pstat.setNString(2, itemDescription);
				pstat.setNString(3, itemPrice);

				pstat.executeUpdate();
				System.out.println("\nNew Item added: " + "\nItem name : " + itemName + "\nItem description : "
						+ itemDescription + "\nItem price : " + itemPrice + "\n");
			} catch (Exception e) {
				System.out.println("Error connecting to Database");
				e.printStackTrace();
			}

			SQLmenu.prepareAnythingElseAttempt(); // calls to return to SQL menu method
		}
	}

	// -------------------------DELETE-----------------------------------
	//----------------All work similarly to Customer Manager-------------

	public void deleteItem() {

		String itemName;

		{

			itemName = deleteItemNameAttempt();

			try {
				Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root",
						"emsql24");

				PreparedStatement pstat = c.prepareStatement("DELETE FROM items WHERE itemName = ?"); // deletes row with this item 

				pstat.setNString(1, itemName);
				pstat.executeUpdate();
				

			} catch (Exception e) {
				System.out.println("Error connecting to Database");
				e.printStackTrace();
			}

			System.out.println("Item with name: " + itemName + " deleted successfully\n");
			SQLmenu.prepareAnythingElseAttempt(); // calls to return to SQL menu method
		}
	}

	// ---------------------------UPDATE-----------------------------------

	public void runUpdateItem() {
		String itemPropertyType;
		String selectedItem;

		System.out.println("Please provide the name of the item you wish to update");
		selectedItem = getItemName();

		System.out.println("What would you like to update?");
		int typeLoopCounter = 1;
		for (String propTypes : itemPropertyTypes) {
			System.out.println(typeLoopCounter + ": " + propTypes);
			typeLoopCounter++;
		}

		itemPropertyType = itemPropertyTypeAttempt();
		System.out.println("Selected property type: " + itemPropertyType); 	//same as update customer property type but for items
																			//code reused and refactored for this purpose
		if (itemPropertyType.equals("Item name")) {
			updateItemItemName(selectedItem);
		} else if (itemPropertyType.equals("Item description")) {			// If item name, description, price - run appropriate method
			updateItemItemDescription(selectedItem);
		} else {
			updateItemItemPrice(selectedItem);
		}
		SQLmenu.prepareAnythingElseAttempt();
	}

	static String prepareGetItemName() {
		return getItemName();
	}

	static String getItemName() {
		Boolean doesItemExist = false;
		String enteredItem = SQLmenu.userInput.nextLine(); // entered Item is a temp variable within the scope of this method
		if (enteredItem.length() < 4) {
			System.out.println("Item name needs to be at least 4 characters");
			return prepareGetItemName();
		} else {

			try { // use db to check if an item with entered name exists

				Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root",
						"emsql24");
				PreparedStatement pstat = c.prepareStatement("SELECT idItems FROM items WHERE itemName = ?");

				pstat.setNString(1, enteredItem);
				ResultSet itemIdQuery = pstat.executeQuery();
				// System.out.println(doesItemExist); log tests
				if (itemIdQuery.next()) {
					doesItemExist = true;
					// System.out.println(doesItemExist); log tests
				}
			} catch (Exception e) {
				System.out.println("Error connecting to Database");
				e.printStackTrace();
			}
		}

		if (doesItemExist) // item exists
		{
			return enteredItem; // item entered correctly
		} else {
			System.out.println("No item exists with the name provided. Please try again."); //error handling
			return prepareGetItemName();
		}

	}

	public static String updateItemPropertyTypeAttempt() {
		return itemPropertyTypeAttempt();
	}

	public static String itemPropertyTypeAttempt() {
		String enteredType = SQLmenu.userInput.nextLine(); // entered type is a temp variable within the scope of this method
		if (!itemPropertyTypes.contains(itemPropertyTypeMap.get(enteredType))) {
			System.out.println("You must select one of the property types");
			return updateItemPropertyTypeAttempt(); //error handling
		} else {
			return itemPropertyTypeMap.get(enteredType); // data selection entered correctly

		}
	}

	void updateItemItemName(String itemNameAttempt) { //similar to update customer username
		String ItemName;

		ItemName = updateItemNameAttempt();

		try {

			Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root", "emsql24");

			PreparedStatement pstat = c.prepareStatement("UPDATE items SET ItemName = ? WHERE ItemName = ?"); //updates item name to new input

			pstat.setNString(1, ItemName);
			pstat.setNString(2, itemNameAttempt);
			pstat.executeUpdate();
			System.out.println("Successful!" + "\nItem: " + ItemName + " updated\n");
		} catch (Exception e) {
			System.out.println("Error connecting to Database"); //error handling
			e.printStackTrace();
		}
	}

	void updateItemItemDescription(String itemName) {
		String itemDescription;

		itemDescription = updateItemDescriptionAttempt();
		try {

			Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root", "emsql24");

			PreparedStatement pstat = c.prepareStatement("UPDATE items SET itemDescription = ? WHERE ItemName = ?"); //updates item description to new input based on item entered

			pstat.setNString(1, itemDescription);
			pstat.setNString(2, itemName);
			pstat.executeUpdate();
			System.out.println("Successful!" + "\nItem description " + itemDescription + " updated");

		} catch (Exception e) {
			System.out.println("Error connecting to Database"); //error handling
			e.printStackTrace();
		}

	}

	void updateItemItemPrice(String itemName) {
		String itemPrice;

		itemPrice = updateItemPriceAttempt();
		try {

			Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root", "emsql24");

			PreparedStatement pstat = c.prepareStatement("UPDATE items SET itemPrice = ? WHERE ItemName = ?"); //updates item price based on item name entered 

			pstat.setNString(1, itemPrice);
			pstat.setNString(2, itemName);
			pstat.executeUpdate();
			System.out.println("Successful!" + "\nItem price: " + itemPrice + " updated");

		} catch (Exception e) {
			System.out.println("Error connecting to Database");
			e.printStackTrace();
		}

	}
	// ---------------------------------VIEW-----------------------------------------------

	public void viewAllItems() {

		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerportal", "root", "emsql24"); 	//simple view, prints customer username, email and ID, 
																															//formatted as list

			PreparedStatement pstat = c.prepareStatement("SELECT itemName, itemDescription, itemPrice FROM ITEMS_VIEW");

			ResultSet allItems = pstat.executeQuery();
			System.out.println("\nQuery processed");
			System.out.println(
					"\nList of item names, descriptions and prices:" + "\n___________________________________");
			while (allItems.next()) {
				String itemName = allItems.getString("itemName");
				String description = allItems.getString("itemDescription");
				String price = allItems.getString("itemPrice");
				System.out.println("\nItem name: " + itemName + "\n" + "Item Description: " + description + "\n"
						+ "Item Price: " + price);
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
