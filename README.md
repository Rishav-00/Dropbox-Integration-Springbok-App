# 📦 Dropbox Business API Integration (Spring Boot)

This project demonstrates a simple Spring Boot-based integration with the **Dropbox Business API** using **OAuth 2.0** authentication.

---

## 🧭 Overview

- ✅ Authenticate with Dropbox using **OAuth 2.0**  
- 🔄 Exchange authorization codes for access tokens  
- 📡 Fetch Dropbox team information (`/2/team/get_info`)  
- 🌐 Handle authentication and API calls purely through **REST endpoints**  
- 🔒 Securely manage secrets using **environment variables**

---

## 🧱 Project Structure

| File | Description |
|------|--------------|
| `DropboxBusinessApiApplication.java` | Entry point that starts the Spring Boot application |
| `DropboxController.java` | Defines REST endpoints for authentication and API calls |
| `DropboxService.java` | Handles Dropbox OAuth flow and API requests |

---

## 🔗 API Endpoints

| Endpoint | Method | Description |
|-----------|--------|-------------|
| `/auth` | GET | Redirects to Dropbox OAuth authorization page |
| `/callback` | GET | Handles Dropbox redirect and exchanges code for tokens |
| `/team-info` | GET | Fetches Dropbox team information using access token |

---

## ⚙️ Steps to Run the Application

### 1️⃣ Start the Spring Boot App
- Default Port: **8081**  
- Make sure your environment variables for `APP_KEY` and `APP_SECRET` are set.

**Windows (PowerShell):**
```powershell
$env:DROPBOX_APP_KEY="your_app_key"
$env:DROPBOX_APP_SECRET="your_app_secret"
````

**macOS/Linux (Terminal):**

```bash
export DROPBOX_APP_KEY="your_app_key"
export DROPBOX_APP_SECRET="your_app_secret"
```

Run the app:

```bash
mvn spring-boot:run
```

---

### 2️⃣ Authorize the App

Open your browser and visit:

```
http://localhost:8081/auth
```

* You’ll be redirected to Dropbox’s login/consent page.
* Log in and allow access.

---

### 3️⃣ Handle the Callback

After approval, Dropbox redirects to:

```
http://localhost:8081/callback?code=XYZ123...
```

* This triggers the **access token exchange**.
* The app stores the access token in memory for subsequent API calls.

---

### 4️⃣ Fetch Dropbox Data

Now, call the following endpoint:

```
http://localhost:8081/team-info
```

You’ll receive Dropbox **team information** as JSON.

---

## 📤 Example Responses

### ✅ 1. URL: `/auth`

**Success Response:**

```json
{
  "success": true,
  "message": "Redirecting to Dropbox authorization page..."
}
```

---

### ✅ 2. URL: `/team-info`

**Success Response:**

```json
{
  "team_info": {
    "num_provisioned_users": 3,
    "name": "API Testing Team",
    "policies": {
      "office_addin": {
        ".tag": "enabled"
      },
      "emm_state": {
        ".tag": "disabled"
      },
      "top_level_content_policy": {
        ".tag": "everyone"
      },
      "sharing": {
        "shared_link_create_policy": {
          ".tag": "default_public"
        },
        "default_link_expiration_days_policy": {
          ".tag": "none"
        },
        "shared_link_default_permissions_policy": {
          ".tag": "default"
        },
        "shared_folder_member_policy": {
          ".tag": "anyone"
        },
        "enforce_link_password_policy": {
          ".tag": "optional"
        },
        "group_creation_policy": {
          ".tag": "admins_and_members"
        },
        "shared_folder_join_policy": {
          ".tag": "from_anyone"
        },
        "shared_folder_link_restriction_policy": {
          ".tag": "anyone"
        }
      },
      "suggest_members_policy": {
        ".tag": "enabled"
      }
    },
    "num_used_licenses": 3,
    "team_id": "dbtid:AACKorDZ0aIehqp0rG1Gw3R_KCVHB_NX2L4",
    "num_licensed_users": 5
  },
  "success": true
}
```

**Failure Response (if not authenticated):**

```json
{
  "success": false,
  "error": "Not authenticated. Please authenticate first."
}
```

---

## 🧠 Notes

* The app uses **environment variables** for secure management of credentials.
* All responses are **pure JSON**.
* For production use, tokens should be securely persisted instead of stored in memory.

---

## 🧑‍💻 Author

**Rishav Dutta**
Integration Developer
💻 Skilled in Java, MuleSoft, and Spring Boot
🚀 Focused on simplifying enterprise integrations

