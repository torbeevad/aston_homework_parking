package ru.aston.db;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class H2DBInitializer implements DBInitializer {

    public void initialize() {
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("init_db.sql");
             Scanner scanner = new Scanner(in, StandardCharsets.UTF_8)) {
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isBlank() && !line.trim().startsWith("--")) {
                    sb.append(line).append("\n");
                }
            }
            String sqlCommands = sb.toString();
            executeSql(sqlCommands);
        } catch (Exception e) {
            System.out.println("Error initializing the database " + e.getMessage());
        }
    }
    private void executeSql(String sql) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        Statement statement = connection.createStatement();
        statement.execute(sql);
        statement.close();
        connection.close();
    }
}