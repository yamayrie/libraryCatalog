# Project 1 - The Library

## Introduction
Project for the Data Structures class. Developed a system to manage a library's catalog.

## Features

- **Book Management** - Helps add, remove, check-out, return books, among other things.

- **Inventory Control** - Maintains the library's collection of books updated.

- **Late Fee Tracking** - Helps track overdue books and calculate late fees.

- **Report Generation** - Generates a report with valuable data. Gives a rundown of everything in the library, including how much is due and the amount that each user owes.

- **User Interface** - Simplifies book catalog management. Allows users to add, remove and display books within the catalog.

## Class Structure

A DoublyLinkedList was used to store the books and users since we're going to be adding and removing. With a DoublyLinkedList, we can traverse the list forward and backward. This allows us to move efficiently, something that would've given us a bigger time complexity using an ArrayList. If we used an ArrayList, it would've also taken resizing the array whenever we had to insert more books or users. Using an ArrayList would've been an option considered if we didn't have to insert or remove and just had to retrieve data.

Between a SinglyLinkedList and a DoublyLinkedList, I decided to use a DoublyLinkedList. In both we wouldn't have to resize and the insertions and removals are both O(1). DoublyLinkedList just gives us the option of bidirectional traversal, making it a better option. If we had to iterate through the data in different orders, the DoublyLinkedList would make the implementation smoother and easier. It gives us more flexibility in what we do. Even if we will not need to traverse bidirectionally, it is a good option to have.

### Student info
[Yamayrie Lebron Borrero](yamayrie.lebron@upr.edu)
