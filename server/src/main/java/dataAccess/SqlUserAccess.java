package dataAccess;


import java.sql.Connection;
import java.sql.SQLException;

public class SqlUserAccess implements UserAccess {

    public SqlUserAccess() {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            System.out.println("\n[Error connecting to database] " + e.getMessage());
        }
    }
    @Override
    public String getUserPass(String username) throws DataAccessException {
        String password = null;
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT password FROM UserData WHERE username=?")) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        password = rs.getString("password");

                        System.out.printf("\n[SELECT FOR GET USER PASS] Password: %s", password);
                    }
                }
                return password;
            }
        } catch(Exception e) {
            System.out.println("\n[GET USER PASS] SQL Access Error: " + e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public String getUserMail(String username) throws DataAccessException {
        String email = null;
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT email FROM UserData WHERE username=?")) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        email = rs.getString("email");

                        System.out.printf("\n[SELECT FOR GET USER MAIL] Email: %s", email);
                    }
                }
            return email;
            }
        } catch(Exception e) {
            System.out.println("\n[GET USER MAIL] SQL Access Error: " + e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clearAllUsers() throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE UserData")) {
                preparedStatement.executeUpdate();
            }
        } catch(SQLException e) {
            System.out.println("\n[CLEAR ALL USERS] SQL Access Error: " + e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void addUser(String username, String password, String email) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO UserData (username, password, email) VALUES(?, ?, ?)")) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, email);
                preparedStatement.executeUpdate();

            }
        } catch (Exception e) {
            System.out.println("\n[ADD USER] SQL Access Error: " + e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }
}
