package com.example.telnet_final;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {
    private static String ip = "4.tcp.eu.ngrok.io";// this is the host ip that your data base exists on you can use 10.0.2.2 for local host                                                    found on your pc. use if config for windows to find the ip if the database exists on                                                    your pc
    private static String port = "16168";// the port sql server runs on
    private static String Classes = "org.postgresql.Driver";// the driver that is required for this connection use                                                                           "org.postgresql.Driver" for connecting to postgresql
    private static String database = "telnet";// the data base name
    private static String username = "postgres";// the user name
    private static String password = "admin";// the password
    private static String url = "jdbc:postgresql://" + ip + ":" + port + "/" + database; // the connection url string
    public Connection ConnectionDb() {
        Connection connection = null;

        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username, password);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return connection;
    }



}
