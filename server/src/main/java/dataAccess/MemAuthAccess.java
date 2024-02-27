package dataAccess;

public class MemAuthAccess implements AuthAccess{

    public void clearTokens() throws DataAccessException {
        authTokens.clear();
    }
}
