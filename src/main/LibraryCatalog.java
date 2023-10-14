package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import data_structures.ArrayList;
import data_structures.DoublyLinkedList;
import data_structures.SinglyLinkedList;
import interfaces.FilterFunction;
import interfaces.List;


/**
 * Class that represents a Library Catalog that manages a collection of books and users. Includes methods for
 * adding, removing, returning, counting books, among other things. Generates reports on the books found in the
 * library and the users fees, among other things.
 * 
*/
public class LibraryCatalog {
	
	/** Stores the library's collection of books.*/
	private DoublyLinkedList<Book> bookCatalog; 
	/** Stores the information of the library's users.*/
	private DoublyLinkedList<User> users;
	/** Next ID available for new books in the library.*/
	private int nextID;
	
	
	/**
	 * Constructs a new LibraryCatalog instance and initializes it by reading data from files.
	 * 
	 * @throws IOException if an error occurs while reading the data from the files.
	 * 
	*/
	public LibraryCatalog() throws IOException {
		bookCatalog = getBooksFromFiles(); 
		users = getUsersFromFiles(); 
		nextID = calculateNextID();
	}
	
	
	/**
	 * Reads book data from catalog.csv. Each line is expected to contain comma-separated values representing
	 * book attributes. It skips the first line which contains a header of the file's format for storing.
	 * 
	 * @return A doubly linked list that has book objects read from the file.
	 * @throws IOException if an error occurs while reading the data from the file.
	 * 
	*/
	private DoublyLinkedList<Book> getBooksFromFiles() throws IOException {
		DoublyLinkedList<Book> books = new DoublyLinkedList<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader("data/catalog.csv"))) {
			String line; // stores each line read from the file
			reader.readLine(); // skip the first line 
			
			while ((line = reader.readLine()) != null) {
				String[] split = line.split(",");
				if (split.length == 6) {
					
					// splits the line in their specific type
					int id = Integer.parseInt(split[0]);
					String title = split[1];
					String author = split[2];
					String genre = split[3];
					LocalDate lastCheckoutDate = LocalDate.parse(split[4]);
					boolean checkedOut = Boolean.parseBoolean(split[5]);
					
					// creates a new book with the split data and adds it to the books list
					Book book = new Book(id, title, author, genre, lastCheckoutDate, checkedOut);
					books.add(book);
				}
			}
		}
		return books;
	}
	
	/**
	 * Reads user data from user.csv and construct a list of User objects.
	 * 
	 * Each line is expected to contain comma-separated values representing user attributes. The file's format
	 * is ID,Full Name,{checked out books, if any} . The method iterates each line, creates User objects, and
	 * adds their information. It skips the first line which contains a header of the file's format for storing.
	 * 
	 * @return A doubly linked list that has user objects read from the file.
	 * @throws IOException if an error occurs while reading the data from the file.
	 * 
	*/
	private DoublyLinkedList<User> getUsersFromFiles() throws IOException {
		DoublyLinkedList<User> users = new DoublyLinkedList<>();
		DoublyLinkedList<Book> checkedOut;

		try (BufferedReader reader = new BufferedReader(new FileReader("data/user.csv"))) {
			String line; // stores each line read from the file
			reader.readLine(); // skips the first line
			
			while ((line = reader.readLine()) != null) {
				// splits the line in their specific type
				String[] split = line.split(",");
				int id = Integer.parseInt(split[0].trim());
				String name = split[1].trim();
				
				// if the line has checked out books
				if (split.length == 3) {
					checkedOut = new DoublyLinkedList<>();
					String[] bookIDs = split[2].replace("{", "").replace("}", "").split(" ");
					DoublyLinkedList<Book> bookCollection = getBookCatalog();

					for (String bookID : bookIDs) {
						int bookId = Integer.parseInt(bookID.trim());
						for (Book book : bookCollection) {
							if (book.getId() == bookId) {
								checkedOut.add(book);
								break;
							}
						}
					}
	            }
				else { // no checked out books by users
					checkedOut = new DoublyLinkedList<>();
				}
				// creates a new user with the split data and adds it to the users list
				User user = new User(id, name);
				user.setCheckedOutList(checkedOut);
				users.add(user);
			
			}
		}
		return users;
	}


	
	/** Allows us to access private fields outside of LibraryCatalog. We can access the reference to the DoublyLinkedList that stores the books. */
	public DoublyLinkedList<Book> getBookCatalog() {
		return bookCatalog;
	}
	
	/** Allows us to access private fields outside of LibraryCatalog. We can access the reference to the DoublyLinkedList that stores the users. */
	public DoublyLinkedList<User> getUsers() {
		return users;
	}
	
	
	/**
	 * Adds a new book to the library catalog
	 * 
	 * @param title Title of the new book.
	 * @param author Author of the new book.
	 * @param genre Genre of the new book.
	 * 
	*/
	public void addBook(String title, String author, String genre) {
		
		LocalDate today = LocalDate.of(2023, 9, 15);
		Book newBook = new Book(nextID ,title, author, genre, today, false);
		nextID++; // increments to make sure that each new book will have a unique ID based on the size of the catalog
		bookCatalog.add(newBook);
	}
	
	
	/**
	 * Removes a book from the library catalog based on its ID
	 * 
	 * @param id ID of the book to be removed.
	 * 
	*/
	public void removeBook(int id) {
		Book toRemove = null;
		for (Book book : bookCatalog) {
			if (book.getId() == id) {
				toRemove = book;
				break;
			}
		}
		if (toRemove != null) {
			bookCatalog.remove(toRemove);
		}
	}	
	
	
	/**
	 * Checks out a book from the library catalog based on its ID.
	 * 
	 * @param id ID of the book to be removed.
	 * @return {@code true} if successful checkout, {@code false} if already checked out or doesn't exist in
	 * catalog.
	 * 
	*/
	public boolean checkOutBook(int id) {
		for (Book book : bookCatalog) {
			if (book.getId() == id) {
				if (book.isCheckedOut()) {
					return false; 
				}
				else {
					LocalDate today = LocalDate.of(2023, 9, 15); 
					book.setCheckedOut(true);
					book.setLastCheckOut(today);
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	/**
	 * Returns a book from the library catalog based on its ID.
	 * 
	 * @param id ID of the book to be returned.
	 * @return {@code true} if successful return, {@code false} if book is not checked out or book doesn't
	 * exist in catalog.
	 * 
	*/
	public boolean returnBook(int id) {
		for (Book book : bookCatalog) {
			if (book.getId() == id) {
				if (!book.isCheckedOut()) {
					return false;
				}
				else {
					book.setCheckedOut(false);
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	/**
	 * Checks the availability of a book from the library catalog based on its ID.
	 * 
	 * @param id ID of the book to check availability.
	 * @return {@code true} if available, {@code false} if checked out or book is not found in the catalog.
	 * 
	*/
	public boolean getBookAvailability(int id) {
		for (Book book : bookCatalog) {
			if (book.getId() == id) {
				return !book.isCheckedOut();
			}
		}
		return false; // book doesn't exist
	}
	
	
	
	/**
	 * Counts the amount of books in the library catalog based on its title ignoring case.
	 * 
	 * @param title Title of the book to count instances of.
	 * @return The number of books with matching title.
	 * 
	*/
	public int bookCount(String title) {
		int count = 0; 
		for (Book book : bookCatalog) { 
			if (book.getTitle().equalsIgnoreCase(title)) {
				count++;
			}
		}
		return count; // number of books with matching titles
	}
	
	
	/**
	 * Calculates the next available ID for a new book in the catalog.
	 * It finds the largest ID currently in use and returns the next available one
	 * which is one greater than the largest ID found. 
	 * 
	 * @return The next available ID for a new book.
	 *
	*/
	private int calculateNextID() {
		int biggestID = 0; 
		
		for (Book book : bookCatalog) { 
			if (book.getId() > biggestID) {
				biggestID = book.getId();
			}
		}
		return biggestID + 1;
	}
	
	
	/**
	 * Counts the number of books in the catalog that belong to a specific genre.
	 * The method iterates through the book catalog and counts how many of them 
	 * match the parameter.
	 * 
	 * @param genre The genre to be counted
	 * @return The number of books in the catalog with the genre
	 * 
	*/
	public int genreCount(String genre) {
		int count = 0;
		for (Book book : bookCatalog) {
			if (book.getGenre().equalsIgnoreCase(genre)) {
				count++;
			}
		}
		return count;
	}
	
	
	
	/**
	 * Calculates the total library fees for a user based on their checked out books.
	 * 
	 * @param user User to calculate library fees.
	 * @return Total fees for the user.
	 * 
	*/
	public double calculateLibraryFees(User user) {
		double totalFees = 0.0;
			for (Book book : user.getCheckedOutList()) {
				if (book.isCheckedOut() == true) {
					if (book.calculateFees() > 0) {
					totalFees += book.calculateFees();
					}
				}
			}
		return totalFees;
	}
	
	
	public void generateReport() throws IOException {
		
		String output = "\t\t\t\tREPORT\n\n";
		output += "\t\tSUMMARY OF BOOKS\n";
		output += "GENRE\t\t\t\t\t\tAMOUNT\n";
		/*
		 * In this section you will print the amount of books per category.
		 * 
		 * Place in each parenthesis the specified count. 
		 * 
		 * Note this is NOT a fixed number, you have to calculate it because depending on the 
		 * input data we use the numbers will differ.
		 * 
		 * How you do the count is up to you. You can make a method, use the searchForBooks()
		 * function or just do the count right here.
		 */
		
		int adventure = genreCount("Adventure");
		int fiction = genreCount("Fiction");
		int classics = genreCount("Classics");
		int mystery = genreCount("Mystery");
		int sciFi = genreCount("Science Fiction");
		
		output += "Adventure\t\t\t\t\t" + adventure + "\n";
		output += "Fiction\t\t\t\t\t\t" +  fiction + "\n";
		output += "Classics\t\t\t\t\t" + classics + "\n";
		output += "Mystery\t\t\t\t\t\t" + mystery + "\n";
		output += "Science Fiction\t\t\t\t\t" + sciFi + "\n";
		output += "====================================================\n";
		
		int totalBooks = adventure + fiction + classics + mystery + sciFi;
		output += "\t\t\tTOTAL AMOUNT OF BOOKS\t" + totalBooks + "\n\n";
		
		/*
		 * This part prints the books that are currently checked out
		 */
		output += "\t\t\tBOOKS CURRENTLY CHECKED OUT\n\n";
		/*
		 * Here you will print each individual book that is checked out.
		 * 
		 * Remember that the book has a toString() method. 
		 * Notice if it was implemented correctly it should print the books in the 
		 * expected format.
		 * 
		 * PLACE CODE HERE
		 */
		
		
		for (Book book : bookCatalog) {
			if (book.isCheckedOut()) {
				output += book.toString() + "\n";
			}
		}
		
		
		output += "====================================================\n";
		
		int checkedOutCount = searchForBook(book -> book.isCheckedOut()).size();		
		
		output += "\t\t\tTOTAL AMOUNT OF BOOKS\t" + checkedOutCount + "\n\n";
		
		
		/*
		 * Here we will print the users the owe money.
		 */
		output += "\n\n\t\tUSERS THAT OWE BOOK FEES\n\n";
		/*
		 * Here you will print all the users that owe money.
		 * The amount will be calculating taking into account 
		 * all the books that have late fees.
		 * 
		 * For example if user Jane Doe has 3 books and 2 of them have late fees.
		 * Say book 1 has $10 in fees and book 2 has $78 in fees.
		 * 
		 * You would print: Jane Doe\t\t\t\t\t$88.00
		 * 
		 * Notice that we place 5 tabs between the name and fee and 
		 * the fee should have 2 decimal places.
		 * 
		 * PLACE CODE HERE!
		 */
		

		double totalDue = 0.0;
		for (User user : users) {
			double userFee = 0.0;
			for (Book book : user.getCheckedOutList()) {
				if (book.isCheckedOut()) {
					float fee = book.calculateFees();
					if (fee > 0) {
						userFee += fee;
					}
				}
			}
			if (userFee > 0) {
				output += user.getName() + "\t\t\t\t\t$" + String.format("%.2f", userFee) + "\n";
				totalDue += userFee;
			}
		}
		
		

			
		output += "====================================================\n";
		
		output += "\t\t\t\tTOTAL DUE\t$" + String.format("%.2f", totalDue) + "\n\n\n";
		output += "\n\n";
		System.out.println(output);// You can use this for testing to see if the report is as expected.
		
		/*
		 * Here we will write to the file.
		 * 
		 * The variable output has all the content we need to write to the report file.
		 * 
		 * PLACE CODE HERE!!
		 */
		
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("report.txt"))) {
			writer.write(output);
		}
		catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
	}
	
	/*
	 * BONUS Methods
	 * 
	 * You are not required to implement these, but they can be useful for
	 * other parts of the project.
	 */
	
	
	
	
	/**
	 * Method that receives a lambda function and searches for books.
	 * 
	 * @param func The lambda function received
	 * @return List of books that follow the condition given by the lambda function.
	*/
	public List<Book> searchForBook(FilterFunction<Book> func) {
		DoublyLinkedList<Book> matchingBooks = new DoublyLinkedList<>();
		for (Book book : bookCatalog) {
			if (func.filter(book)) {
				matchingBooks.add(book);
			}
		}
		return matchingBooks;
	}
	
	
	/**
	 * Method that receives a lambda function and searches for users.
	 * 
	 * @param func The lambda function received
	 * @return List of users that follow the condition given by the lambda function.
	*/
	public List<User> searchForUsers(FilterFunction<User> func) {
		DoublyLinkedList<User> matchingUsers = new DoublyLinkedList<>();
		for (User user : users) {
			if (func.filter(user)) {
				matchingUsers.add(user);
			}
		}
		return matchingUsers;
	}
	
}
