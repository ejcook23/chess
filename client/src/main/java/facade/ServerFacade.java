package facade;

import com.google.gson.Gson;
import model.*;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;


public class ServerFacade {

    static int port;

    public ServerFacade(int serverPort) {
        port = serverPort;
    }

    public static void wipeDB() throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:" + port + "/db");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        System.out.println("\n [SERVER FACADE] WIPING DATABASE... \n");

        // Make the request
        http.connect();

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            UserAndAuthResponse response = new Gson().fromJson(inputStreamReader, UserAndAuthResponse.class);
            System.out.println(response);
        }

    }

    // REGISTER USER
    public static UserAndAuthResponse register(String username, String password, String email) throws Exception {
        // Specify the desired endpoint
        URI uri = new URI("http://localhost:" + port + "/user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Content-Type", "application/json");

        // Write out the body
        UserData body = new UserData(username,password,email);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            UserAndAuthResponse response = new Gson().fromJson(inputStreamReader, UserAndAuthResponse.class);
            System.out.println(response);
            return response;
        }

    }













}

