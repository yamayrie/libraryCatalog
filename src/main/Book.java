package main;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Book {
	
	private int id;
	private String title;
	private String author;
	private String genre;
	private LocalDate lastCheckoutDate;
	private boolean checkedOut;
	
	public Book(int id, String title, String author, String genre, LocalDate lastCheckoutDate, boolean checkedOut) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.lastCheckoutDate = lastCheckoutDate;
		this.checkedOut = checkedOut;
	}	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public LocalDate getLastCheckOut() {
		return lastCheckoutDate;
	}
	public void setLastCheckOut(LocalDate lastCheckOut) {
		this.lastCheckoutDate = lastCheckOut;
	}
	public boolean isCheckedOut() {
		return checkedOut;
	}
	public void setCheckedOut(boolean checkedOut) {
		this.checkedOut = checkedOut;
	}
	
	@Override
	public String toString() {
		/*
		 * This is supposed to follow the format
		 * 
		 * {TITLE} BY {AUTHOR}
		 * 
		 * Both the title and author are in uppercase.
		 */
		return title.toUpperCase() + " BY " + author.toUpperCase();
	}
	
	
	
	/**
	 * Calculates the late fee for a checked out book based on the number of days it's overdue.
	 * 
	 * @return How much money is owed in late fees, or 0 if not overdue
	 * 
	*/
	public float calculateFees() {
		
		LocalDate today = LocalDate.of(2023, 9, 15);
		long daysSince = ChronoUnit.DAYS.between(this.getLastCheckOut(), today);
		
		if (daysSince >= 31) {
			float baseFee = 10.0f;
			float additionalFees = 1.50f;
			float lateFee = baseFee + additionalFees * (daysSince - 31);
			return lateFee;
		}
		else {
			return 0.0f;
		}		
	}
}
