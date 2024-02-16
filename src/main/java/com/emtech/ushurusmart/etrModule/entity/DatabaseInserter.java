package com.emtech.ushurusmart.etrModule.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class DatabaseInserter {
    public static void main(String[] args) {
        // parsed JSON data
        etr myetr = new etr ();

        // Database connection details
        double invoiceNumber;
        double amount;
        LocalDateTime date;
        String buyerPin;

        // SQL query to insert data
        String query = "INSERT INTO EtrInvoice (invoiceNumber,amount,date,buyerPin) VALUES (?, ?, ?, ?)";

        try (Connection connect = DriverManager.getConnection(buyerPin);
             PreparedStatement pstmt = connect.prepareStatement(query)) {
            
            // Setting parameters
            pstmt.setDouble(1, myetr.getInvoiceNumber());
            pstmt.setDouble(2, myetr.getAmount());
            pstmt.setDate(parameterIndex:3, myetr.getDate());
            pstmt.setString(4, myetr.getBuyerPin());

            // Execute the insert
            pstmt.executeUpdate();
            System.out.println("Insertion successful");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
