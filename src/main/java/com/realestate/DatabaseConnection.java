package com.realestate;

import java.sql.*;

public class DatabaseConnection {

    private static Connection connection = null;

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                String url = "jdbc:mysql://localhost:3306/realestatedb";
                String user = "root";
                String password = "";

                connection = DriverManager.getConnection(url, user, password);
                initDB(connection);
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found.");
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void initDB(Connection con) {
        try {
            Statement st = con.createStatement();

            // Enable foreign key checks (default in MySQL)
            st.execute("SET FOREIGN_KEY_CHECKS=1;");

            // Create user table
            String createMemberTable = "CREATE TABLE IF NOT EXISTS member ("
                    + "uname VARCHAR(45) NOT NULL, "
                    + "password VARCHAR(45) NOT NULL, "
                    + "email VARCHAR(45) NOT NULL, "
                    + "phone VARCHAR(45) NOT NULL, "
                    + "PRIMARY KEY (uname)"
                    + ");";
            st.executeUpdate(createMemberTable);

            String createPropertyTable = "CREATE TABLE IF NOT EXISTS property ("
                    + "ID serial NOT NULL, "
                    + "uname VARCHAR(45) NOT NULL, "
                    + "propertytype VARCHAR(45) NOT NULL, "
                    + "location VARCHAR(45) NOT NULL, "
                    + "budget VARCHAR(45) NOT NULL, "
                    + "description VARCHAR(225) NOT NULL, "
                    + "link VARCHAR(225) NOT NULL, "
                    + "status BOOLEAN DEFAULT false NOT NULL, "
                    + "PRIMARY KEY (ID)"
                    + ");";
            st.executeUpdate(createPropertyTable);


            String createPropertyDealTable = "CREATE TABLE IF NOT EXISTS propertydeals ("
                    + "ID serial NOT NULL, "
                    + "buyer VARCHAR(45) NOT NULL, "
                    + "PRIMARY KEY (ID)"
                    + ");" ;
            st.executeUpdate(createPropertyDealTable);
            } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String insert(Member member) {
        Connection con = null;
        try {
            con = getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql = "insert into member values(?,?,?,?)";
        String result="Success";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, member.getUname());
            ps.setString(2, member.getPassword());
            ps.setString(3, member.getEmail());
            ps.setNString(4, member.getPhone());
            ps.executeUpdate();
        } catch (SQLException e) {
            result="Failure";
            e.printStackTrace();
        }
        return result;
    }
}