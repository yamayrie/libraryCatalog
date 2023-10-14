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
@SuppressWarnings("serial")
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
		JTextField authorField = new JTextField(20);
		JTextField genreField = new JTextField(20);
		
		panel.add(new JLabel("TITLE"));
		panel.add(titleField);
		panel.add(new JLabel("AUTHOR"));
		panel.add(authorField);
		panel.add(new JLabel("GENRE"));
		panel.add(genreField);
		
		JButton addButton = new JButton("Add Book");
		JButton removeButton = new JButton("Remove Book");
		JButton displayButton = new JButton("Display Book Catalog");
		
		
		

		addButton.addActionListener(new ActionListener() {
			
			/**
			 * ActionListener for the add book button. Happens whenever the add book
			 * button is pressed.
			 * 
			 * @param e ActionEvent that occurred
			 * 
			*/
			@Override
			public void actionPerformed(ActionEvent e) {
				// get title, author, and genre from user input
				String title = titleField.getText();
				String author = authorField.getText();
				String genre = genreField.getText();
				if (!title.isEmpty() && !author.isEmpty() && !genre.isEmpty()) {
					libraryCatalog.addBook(title, author, genre);
					
					// clear field
					titleField.setText("");
					authorField.setText("");
					genreField.setText("");
					displayBookList();
				}
			}
		});
		
		removeButton.addActionListener(new ActionListener() {
			
			/**
			 * ActionListener for the remove book button. Happens whenever the remove book
			 * button is pressed.
			 * 
			 * @param e ActionEvent that occurred
			 * 
			*/
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
			
			/**
			 * ActionListener for the display book catalog button. Happens whenever the display
			 * book catalog button is pressed.
			 * 
			 * @param e ActionEvent that occurred
			 * 
			*/
			@Override
			public void actionPerformed(ActionEvent e) {
				displayBookList();
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);
		buttonPanel.add(displayButton);
		
		add(buttonPanel, BorderLayout.NORTH);
		add(new JScrollPane(bookJList), BorderLayout.CENTER);
		add(panel, BorderLayout.SOUTH);
	}
	
	/**
	 * Clears the bookListModel and fills it with books from the library catalog. It updates the
	 * displayed book list in the user interface.
	 * 
	*/
	private void displayBookList() {
		bookListModel.clear();
		for (Book book : libraryCatalog.getBookCatalog()) {
			bookListModel.addElement(book);
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
