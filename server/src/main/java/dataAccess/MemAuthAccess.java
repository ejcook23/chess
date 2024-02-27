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

   // @Override
//    public AuthData getToken(String username) throws DataAccessException {
//        if(authTokens.get(username) == null) {
//            throw new DataAccessException("Attempted to GET the TOKEN of a nonexistent user.");
//        }
//        else {
//            return new AuthData(authTokens.get(username),username);
//        }
//    }
    @Override
    public String createAuth(String username) {
        String newAuthToken = UUID.randomUUID().toString();
        authTokens.put(newAuthToken,username);
        return newAuthToken;
    }

}
