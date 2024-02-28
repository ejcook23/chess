package dataAccess;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemAuthAccess implements AuthAccess{
    Map<String, String> authTokens = new HashMap<String, String>();

    @Override
    public void clearTokens() {
        authTokens.clear();
    }

    @Override
    public boolean tokenExists(String token) {
        return authTokens.get(token) != null;
    }

    @Override
    public String createAuth(String username) {
        String newAuthToken = UUID.randomUUID().toString();
        authTokens.put(newAuthToken,username);
        System.out.println(authTokens);
        return newAuthToken;
    }

    @Override
    public void delToken(String token) {
        authTokens.remove(token);
    }

}
