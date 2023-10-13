package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


/**
 * This class represents a simple library catalog user interface.
 * It allows users to interact with the library catalog by adding, removing, and displaying books. 
 * 
*/
public class LibraryGUI extends JFrame {
	
	private LibraryCatalog libraryCatalog;
	private DefaultListModel<Book> bookListModel;
	private JList<Book> bookJList;
	
	public LibraryGUI() throws IOException {
		libraryCatalog = new LibraryCatalog();
		bookListModel = new DefaultListModel<>();
		bookJList = new JList<>(bookListModel);
		
		setTitle("LibraryGUI");
		setSize(500, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 2));
		
		JTextField titleField = new JTextField(20);
		titleField.setVisible(false);
		JTextField authorField = new JTextField(20);
		authorField.setVisible(false);
		JTextField genreField = new JTextField(20);
		genreField.setVisible(false);
		
		JLabel titleLabel = new JLabel("Title:");
		titleLabel.setVisible(false);
		JLabel authorLabel = new JLabel("Author:");
		authorLabel.setVisible(false);
		JLabel genreLabel = new JLabel("Genre:");
		genreLabel.setVisible(false);
		
		panel.add(titleLabel);
		panel.add(titleField);
		panel.add(authorLabel);
		panel.add(authorField);
		panel.add(genreLabel);
		panel.add(genreField);
		
		JButton addButton = new JButton("Add Book");
		JButton removeButton = new JButton("Remove Book");
		JButton displayButton = new JButton("Display Book Catalog");
		
		addButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				titleLabel.setVisible(true);
				titleField.setVisible(true);
				authorLabel.setVisible(true);
				authorField.setVisible(true);
				genreLabel.setVisible(true);
				genreField.setVisible(true);
				
				String title = titleField.getText();
				String author = authorField.getText();
				String genre = genreField.getText();
				if (!title.isEmpty() && !author.isEmpty() && !genre.isEmpty()) {
					libraryCatalog.addBook(title, author, genre);
					titleField.setText("");
					authorField.setText("");
					genreField.setText("");
					displayBookList();
				}
			}
		});
		
		removeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Book selectedBook = bookJList.getSelectedValue();
				if (selectedBook != null) {
					libraryCatalog.removeBook(selectedBook.getId());
					displayBookList();
				}
			}
		});
		
		displayButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayBookList();
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);
		buttonPanel.add(displayButton);
		
		add(panel, BorderLayout.SOUTH);
		add(new JScrollPane(bookJList), BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.NORTH);
	}
	
	private void displayBookList() {
		bookListModel.clear();
		for (Book book : libraryCatalog.getBookCatalog()) {
			String bookInfo = "(" + book.getId() + ") " + book.toString();
			bookListModel.addElement(new Book(book.getId(), bookInfo, "", "", null, false));
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					LibraryGUI libraryGUI = new LibraryGUI();
					libraryGUI.setVisible(true);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
