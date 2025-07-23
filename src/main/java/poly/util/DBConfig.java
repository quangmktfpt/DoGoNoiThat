package poly.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConfig {
    public static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Storedogo2";
    public static final String USER = "sa";
    public static final String PASS = "123";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }
} 