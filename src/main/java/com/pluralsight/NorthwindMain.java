package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class NorthwindMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (args.length != 2) {
            System.exit(1);
        }

        String username = args[0];
        String password = args[1];

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            int option;

            while (true) {
                option = menu(scanner);

                switch (option) {
                    case 1 -> displayAllProducts(username, password);
                    case 2 -> displayAllCustomers(username, password);
                    case 0 -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }

                System.out.println();
            }

        } catch (Exception e) {
            System.out.println("An error has occurred: " + e);
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static int menu(Scanner scanner) {
        System.out.println("What do you want to do?");
        System.out.println(" 1) Display all products");
        System.out.println(" 2) Display all customers");
        System.out.println(" 0) Exit");
        System.out.print("Select an option: ");

        int option = -1;
        try {
            option = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Please enter a valid number.");
            scanner.nextLine();
        }
        return option;
    }

    private static void displayAllProducts(String username, String password) throws SQLException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet results = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/northwind",
                    username, password
            );

            String query = """
                    SELECT ProductID, ProductName, UnitPrice, UnitsInStock
                    FROM products
                    """;

            preparedStatement = connection.prepareStatement(query);
            results = preparedStatement.executeQuery();

            System.out.printf("%-4s %-40s %-8s %s%n",
                    "ID", "Name", "Price", "Stock");
            System.out.println("-".repeat(80));

            while (results.next()) {
                System.out.printf("%-4d| %-40s| %-8.2f| %d%n",
                        results.getInt("ProductID"),
                        results.getString("ProductName"),
                        results.getDouble("UnitPrice"),
                        results.getInt("UnitsInStock"));
            }

        } catch (Exception e) {
            System.out.println("Error displaying products: " + e);
            e.printStackTrace();
        } finally {
            if (results != null) {
                results.close();
            }
            if (preparedStatement != null){
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    private static void displayAllCustomers(String username, String password) throws SQLException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet results = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/northwind",
                    username, password
            );

            String query = """
                    SELECT ContactName, CompanyName, City, Country, Phone
                    FROM customers
                    ORDER BY Country
                    """;

            preparedStatement = connection.prepareStatement(query);
            results = preparedStatement.executeQuery();

            System.out.printf("%-30s %-35s %-15s %-15s %s%n",
                    "Contact Name", "Company", "City", "Country", "Phone");
            System.out.println("-".repeat(110));

            while (results.next()) {
                System.out.printf("%-30s %-35s %-15s %-15s %s%n",
                        results.getString("ContactName"),
                        results.getString("CompanyName"),
                        results.getString("City"),
                        results.getString("Country"),
                        results.getString("Phone"));
            }

        } catch (Exception e) {
            System.out.println("Error displaying customers: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (results != null) {
                results.close();
            }
            if (preparedStatement != null){
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
