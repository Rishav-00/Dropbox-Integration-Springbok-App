package com.example.dropbox;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class DropboxService {
	
	@Value("${APP_KEY}")
	private String APP_KEY;

	@Value("${APP_SECRET}")
	private String APP_SECRET;


    private static final String REDIRECT_URI = "http://localhost:8081/callback";
    private static final String AUTH_URL = "https://www.dropbox.com/oauth2/authorize";
    private static final String TOKEN_URL = "https://api.dropboxapi.com/oauth2/token";
    private static final String TEAM_INFO_URL = "https://api.dropboxapi.com/2/team/get_info";
    private static final String SCOPES = "team_info.read";

    private String accessToken;
    
    public String getAuthorizationUrl() {
        try {
            return String.format(
                "%s?client_id=%s&response_type=code&redirect_uri=%s&token_access_type=offline&scope=%s",
                AUTH_URL,
                APP_KEY,
                URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8),
                URLEncoder.encode(SCOPES, StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            throw new RuntimeException("Error generating authorization URL", e);
        }
    }

    public void exchangeCodeForToken(String authCode) throws Exception {
        URL url = new URL(TOKEN_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        String credentials = APP_KEY + ":" + APP_SECRET;
        String encodedCredentials = java.util.Base64.getEncoder()
            .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        conn.setRequestProperty("Authorization", "Basic " + encodedCredentials);

        String requestBody = String.format(
            "code=%s&grant_type=authorization_code&redirect_uri=%s",
            URLEncoder.encode(authCode, StandardCharsets.UTF_8),
            URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8)
        );

        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            String response = readResponse(conn.getInputStream());
            JSONObject json = new JSONObject(response);
            accessToken = json.getString("access_token");
        } else {
            String error = readResponse(conn.getErrorStream());
            throw new Exception("Token exchange failed: " + error);
        }
    }

    public String getTeamInfo() throws Exception {
        if (accessToken == null) {
            throw new IllegalStateException("Not authenticated");
        }

        URL url = new URL(TEAM_INFO_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setDoOutput(true);

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            return readResponse(conn.getInputStream());
        } else {
            String error = readResponse(conn.getErrorStream());
            throw new Exception("API call failed: " + error);
        }
    }

    public boolean isAuthenticated() {
        return accessToken != null;
    }

    public void clearTokens() {
        accessToken = null;
    }

    private String readResponse(InputStream inputStream) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }
}