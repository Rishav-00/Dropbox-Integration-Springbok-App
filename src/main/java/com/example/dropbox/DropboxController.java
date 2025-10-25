package com.example.dropbox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DropboxController {

    @Autowired
    private DropboxService dropboxService;

    @GetMapping("/auth")
    public RedirectView authenticate() {
        String authUrl = dropboxService.getAuthorizationUrl();
        return new RedirectView(authUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<Map<String, Object>> callback(@RequestParam("code") String code) {
        Map<String, Object> response = new HashMap<>();
        try {
            dropboxService.exchangeCodeForToken(code);
            response.put("success", true);
            response.put("message", "Authentication successful! You can now fetch team information.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/team-info")
    public ResponseEntity<Map<String, Object>> getTeamInfo() {
        Map<String, Object> response = new HashMap<>();
        try {
            if (!dropboxService.isAuthenticated()) {
                response.put("success", false);
                response.put("error", "Not authenticated. Please authenticate first.");
                return ResponseEntity.status(401).body(response);
            }

            String teamInfo = dropboxService.getTeamInfo();
            response.put("success", true);
            response.put("team_info", new org.json.JSONObject(teamInfo).toMap());
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
