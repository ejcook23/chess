package dataAccess;


import java.sql.Connection;
import java.sql.SQLException;

public class SQLUserAccess implements UserAccess {

    public SQLUserAccess() {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String getUserPass(String username) throws DataAccessException {
        return null;
    }

    @Override
    public String getUserMail(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clearAllUsers() throws DataAccessException {

    }

    @Override
    public void addUser(String username, String password, String email) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO pet (name, type) VALUES(?, ?)")) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, type);

                preparedStatement.executeUpdate();

            }
        } catch(SQLException e) {
            System.out.println("SQL Access Error: " + e.getMessage());
        }
    }
}
