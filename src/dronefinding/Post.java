/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dronefinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Dell
 */
public class Post {
    private static JSONObject jsonResponse;
    private String teamName = "tryingtofindmistakes";
    private String seed,Extractedseed;
    private final String endpoint = "https://dw.gnet.it/"; 

          public static String sendPostRequest(String endpoint, String payload) {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method to POST
            connection.setRequestMethod("POST");

            // Set request property for content type
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Write payload to the output stream of the connection
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(payload.getBytes());
                outputStream.flush();
            }

            // Get response code from the server
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // If the response is successful, read response data
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    // Parse JSON response using org.json.JSONObject
                    jsonResponse = new JSONObject(response.toString());
                    System.out.println("POST sent successfully");
                }
            } else {
                System.out.println("POST request failed with HTTP error code: " + responseCode);
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                    String errorLine;
                    StringBuilder errorResponse = new StringBuilder();
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorResponse.append(errorLine);
                    }
                    System.out.println("Error response: " + errorResponse.toString());
                }
            }
        } catch (MalformedURLException e) {
            System.err.println("Malformed URL: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        }

        return response.toString();
    }
          
    public String init() {
            String Endpoint = endpoint + "init" ;
            String Payload=   "{\"team\": \"" + teamName + "\", \"seed\": " + seed + "}" ;
            String initResponse = sendPostRequest(Endpoint, Payload);
            Extractedseed = extractSeedFromResponse(initResponse);
            return initResponse;
    }
    
    public String look(){
           String Endpoint = endpoint + "look" ;
            String Payload=  "{\"team\": \"" + teamName + "\", \"seed\": \"" + Extractedseed + "\"}" ;
            String lookResponse = sendPostRequest(Endpoint, Payload);
            return lookResponse;
            
    }
    
    public static String extractSeedFromResponse(String response) {
        return jsonResponse.getString("seed");
    }
    
    public String move(int direction) {
         String Endpoint = endpoint + "move" ;
         String Payload = "{\"team\": \"" + teamName + "\", \"seed\": \"" + Extractedseed + "\", \"move\": " + direction + "}" ;
         String moveResponse = sendPostRequest(Endpoint , Payload);
         
         return moveResponse;
    }
    
    
public String load() {
         String Endpoint = endpoint + "load" ;
    
    String payload = "{\"team\": \"" + teamName + "\", \"seed\": \"" + Extractedseed + "\"}";
    String loadResponse = sendPostRequest(Endpoint, payload);
    
    // Print the load response to the terminal
    System.out.println("Load Response: " + loadResponse);
    
    // Check if the load response indicates success
    return loadResponse;
}
     public int extractEnergyFromResponse(String response) {
        int energy = jsonResponse.getInt("Energy");
        return energy;
    }

    public static int extractPositionXFromResponse(String response) {
    try {
        JSONObject jsonResponse = new JSONObject(response);
        return jsonResponse.getInt("posx");
    } catch (JSONException e) {
        // Handle JSON parsing exception
        e.printStackTrace();
        return -1; // Return a default value or throw an exception as needed
    }
}

    public static int extractPositionYFromResponse(String response) {
       try {
        JSONObject jsonResponse = new JSONObject(response);
        return jsonResponse.getInt("posy");
    } catch (JSONException e) {
        // Handle JSON parsing exception
        e.printStackTrace();
        return -1; // Return a default value or throw an exception as needed
    }
    }

    public int[] extractNeighborsFromResponse(String response) {
        JSONArray numbersArray = jsonResponse.getJSONArray("neighbors");
        int[] numbers = new int[numbersArray.length()];
        
        for (int i = 0; i < numbersArray.length(); i++) {
            numbers[i] = numbersArray.getInt(i);
        }
        
        return numbers;
    }

    public int extractedHeightFromResponse(String response) {
        int rows = jsonResponse.getInt("height");
        return rows;
    }

    public int extractedWidthFromResponse(String response) {
        int cols = jsonResponse.getInt("width");
        return cols;
    }
}



