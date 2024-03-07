package dataAccess;

public class SQLAuthAccess implements AuthAccess{
    @Override
    public void clearTokens() {

    }

    @Override
    public boolean tokenExists(String token) {
        return false;
    }

    @Override
    public String createAuth(String username) {
        return null;
    }

    @Override
    public void delToken(String token) {

    }

    @Override
    public String getUserFromToken(String token) {
        return null;
    }
}
