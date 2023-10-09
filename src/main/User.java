package main;

import data_structures.DoublyLinkedList;
import interfaces.List;

public class User {
	
	private int id;
	private String name;
	private DoublyLinkedList<Book> checkedOutList;

	public User(int id, String name) {
		this.id = id;
		this.name = name;
		this.checkedOutList = new DoublyLinkedList<>();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Book> getCheckedOutList() {
		return checkedOutList;
	}

	public void setCheckedOutList(DoublyLinkedList<Book> checkedOutList) {
		this.checkedOutList = checkedOutList;
	}

	public void addBook(Book book) {
		checkedOutList.add(book);
	}
	
	public float libraryFees() {
		float dueFees = 0;
		for (Book book : getCheckedOutList()) {
			if (book.isCheckedOut() && book.calculateFees() > 0) {
				dueFees += book.calculateFees();
			}
		}
		return dueFees;
	}
	
}
