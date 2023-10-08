package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Iterator;

import data_structures.ArrayList;
import data_structures.DoublyLinkedList;
import data_structures.SinglyLinkedList;
import interfaces.FilterFunction;
import interfaces.List;


/*
 * A DoublyLinkedList was used to store the books and users since we're going to be adding and removing. With a DoublyLinkedList, we can traverse the list forward and backward. This allows us to move efficiently, something that would've given us a bigger time complexity using an ArrayList. If we used an ArrayList, it would've also taken resizing the array whenever we had to insert more books or users. Using an ArrayList would've been an option considered if we didn't have to insert or remove and just had to retrieve data.
 * 
 * Between a SinglyLinkedList and a DoublyLinkedList, I decided to use a DoublyLinkedList. In both we wouldn't have to resize and the insertions and removals are both O(1). DoublyLinkedList just gives us the option of bidirectional traversal, making it the better option. If we had to iterate through the data in different orders, the DoublyLinkedList would make the implementation smoother and easier. It gives us more flexibility in what we do. Even if we will not need to traverse bidirectionally, it is a good option to have.
 * 
 * */



public class LibraryCatalog {
	
	private DoublyLinkedList<Book> bookCatalog; // stores the library's collection of books
	private DoublyLinkedList<User> users; // stores the information of the library's users
	private int nextID; // next ID available for new books in the library
	
	public LibraryCatalog() throws IOException {
		bookCatalog = getBooksFromFiles(); // initializes by reading the data from catalog.csv
		users = getUsersFromFiles(); // initializes by reading the data from user.csv
		nextID = calculateNextID(); // sets the next available ID
	}
	
	private DoublyLinkedList<Book> getBooksFromFiles() throws IOException {
		DoublyLinkedList<Book> books = new DoublyLinkedList<>(); // created a new DoublyLinkedList to store books
		
		try (BufferedReader reader = new BufferedReader(new FileReader("data/catalog.csv"))) { // opening to be able to read "data/catalog.csv" file
			String line; // stores each line read from the file
			reader.readLine(); // skip the first line which just has the format for saving the books
			
			while ((line = reader.readLine()) != null) { // iteration until the end
				String[] split = line.split(","); // splits the line and returns an array of strings around matches of commas
				if (split.length == 6) { // checks if the array has 6 elements which is the amount of data the books have
					
					// throws the data in their specific type
					int id = Integer.parseInt(split[0]);
					String title = split[1];
					String author = split[2];
					String genre = split[3];
					LocalDate lastCheckoutDate = LocalDate.parse(split[4]);
					boolean checkedOut = Boolean.parseBoolean(split[5]);
					
					// creates a new book with the split data and adds it to the books linked list
					Book book = new Book(id, title, author, genre, lastCheckoutDate, checkedOut);
					books.add(book);
				}
			}
		}
		return books; // returns the list with all the split objects
	}
	
	
	private DoublyLinkedList<User> getUsersFromFiles() throws IOException {
		DoublyLinkedList<User> users = new DoublyLinkedList<>(); // created a new DoublyLinkedList to store users
		
		try (BufferedReader reader = new BufferedReader(new FileReader("data/user.csv"))) { // opening to be able to read "data/user.csv" file
			String line; // stores each line read from the file
			reader.readLine(); // skip the first line which just has the format for saving the books
			
			while ((line = reader.readLine()) != null) { // iteration until the end
				String[] split = line.split(","); // splits the line and returns an array of strings around matches of commas
				if (split.length == 3) { // checks if the array has 3 elements which is the amount of data the users have
					int id = Integer.parseInt(split[0]); // parse id
					String name = split[1]; // get name
					String bookString = split[2].trim(); // remove dragging spaces from book string 
					
					User user = new User(id, name); // create new user object
					
					if (!bookString.isEmpty()) {
						String[] bookIds = bookString.replaceAll("[{}]", "").split(" ");
						for (String bookIdString : bookIds) { // iterate through ids and add books to user's book list
							int bookID = Integer.parseInt(bookIdString);
							Book book = findBookById(bookID);
							if (book != null) {
								user.addBook(book); // add book to user's book list
							}
						}
					}
					users.add(user); // adds user to linked list
				}
			}
		}
		
		return users; // returns the list with all the split data in their place
	}
	
	
	private Book findBookById(int bookID) {
		for (Book book : bookCatalog) { // iterates the catalog
			if (book.getId() == bookID) { // checks if book id matches target
				return book; // if target id found, return the book
			}
		}
		return null; // not found
	}

	public DoublyLinkedList<Book> getBookCatalog() {
		// allows us to access private fields outside off LibraryCatalog. we can access the reference to the DoublyLinkedList that stores the books
		return bookCatalog;
	}
	
	
	public DoublyLinkedList<User> getUsers() {
		// allows us to access private fields outside off LibraryCatalog. we can access the reference to the DoublyLinkedList that stores the users
		return users;
	}
	
	
	public void addBook(String title, String author, String genre) {
		
		LocalDate today = LocalDate.of(2023, 9, 15);
		Book newBook = new Book(nextID ,title, author, genre, today, false);
		nextID++; // increments to make sure that each new book will have a unique ID based on the size of the catalog
		bookCatalog.add(newBook);
	}
	
	
	public void removeBook(int id) {
		Iterator<Book> i = bookCatalog.iterator(); // creates iterator
		while (i.hasNext()) { // loop through elements in bookCatalog
			Book book = i.next(); // gets next element
			if (book.getId() == id) { // checks if book id is equal to target id
				i.remove(); // since found, removes the whole line from bookCatalog
				return; // ends loop
			}
		}
		System.out.println("Book not found in catalog"); // if id is not found
	}	
	
	
	public boolean checkOutBook(int id) {
		for (Book book : bookCatalog) { // iteration
			if (book.getId() == id) { // find the target id
				if (book.isCheckedOut()) { // checks if it's available to check out
					return false; 
				}
				else { // if found and it's not checked out
					LocalDate today = LocalDate.of(2023, 9, 15); 
					book.setCheckedOut(true); // change checkout status
					book.setLastCheckOut(today); // update checkout date
					return true;
				}
			}
		}
		return false; // book doesn't exist in catalog
	}
	
	
	public boolean returnBook(int id) {
		for (Book book : bookCatalog) { // iteration
			if (book.getId() == id) { // find target id
				if (!book.isCheckedOut()) { // if not checked out
					return false;
				}
				else {
					book.setCheckedOut(false); // if checked out
					return true;
				}
			}
		}
		
		return false; // id not found
	}
	
	
	public boolean getBookAvailability(int id) {
		for (Book book :bookCatalog) {
			if (book.getId() == id) { // if book id equals target
				return !book.isCheckedOut(); // true if book is not checked out
			}
		}
		return false; // book not found
	}
	
	
	public int bookCount(String title) {
		int count = 0; // initialize counter for matching books
		for (Book book : bookCatalog) { // iteration
			if (book.getTitle().equalsIgnoreCase(title)) { // checks if book title equals target title
				count++; // if true, increases counter
			}
		}
		return count; // number of books with matching titles
	}
	
	private int calculateNextID() {
		int biggestID = 0; // initialization of largest id
		
		for (Book book : bookCatalog) { // iteration
			if (book.getId() > biggestID) { // if book id is greater than the current one
				biggestID = book.getId(); // update the largest id
			}
		}
		return biggestID + 1; // after going through every book in the catalog, it returns the largest id plus one to make sure that when we add a new book to the catalog, it will have the next id after the last one
	}
	
	public int genreCount(String genre) {
		int count = 0;
		for (Book book : bookCatalog) {
			if (book.getGenre().equalsIgnoreCase(genre)) {
				count++;
			}
		}
		return count;
	}
	
	private double calculateTotalFees(User user) {
		double totalDue = 0.0;
		for (Book book : user.getCheckedOutList()) {
			if (book.isCheckedOut()) {
				totalDue += book.calculateFees();
			}
		}
		return totalDue;
	}
	
	public double calculateTotalLibraryFees() {
		double totalFees = 0.0;
		for (User user : users) {
			totalFees += calculateTotalFees(user);
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
		
		
		
		for (User user : users) {
			double totalFees = calculateTotalFees(user);
			if (totalFees > 0) {
				output += user.getName() + "\t\t\t\t\t$" + String.format("%.2f", totalFees) + "\n";
			}
		}

			
		output += "====================================================\n";
		
		double totalDue = calculateTotalLibraryFees();
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
	public List<Book> searchForBook(FilterFunction<Book> func) {
		List<Book> matchingBooks = new ArrayList<>();
		for (Book book : bookCatalog) {
			if (func.filter(book)) {
				matchingBooks.add(book);
			}
		}
		return matchingBooks;
	}
	
	public List<User> searchForUsers(FilterFunction<User> func) {
		List<User> matchingUsers = new ArrayList<>();
		for (User user : users) {
			if (func.filter(user)) {
				matchingUsers.add(user);
			}
		}
		return matchingUsers;
	}
	
}
