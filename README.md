# HearingsDemo API

A prototype Spring Boot 3.5.x REST API built for HMCTS-style hearing management.
This project uses a Read-Only CQRS architecture and strictly separates HTTP/Web layers from the Domain layers using DTOs.

## Environments overview

Due to corporate firewall constraints (Zscaler), this project supports two distinct development environments:
1. **GitHub Codespaces (Remote):** Connects to the real/shared Supabase database.
2. **Local Machine (Docker):** Uses a local PostgreSQL container seeded with safe, synthetic test data.

Spring Boot automatically switches between these environments using Environment Variable fallbacks in `application.properties`.

---

## 1. Local Development (Corporate Laptop)

### Prerequisites
Because Docker Desktop requires a paid license for enterprise use, we use **Colima** to run our Docker daemon.
* Install: `brew install colima docker docker-compose`

### Starting the Infrastructure
1. **Start the Docker Engine:**
   ```bash
   colima start
   ```
   (This spins up postgres:18.1 (matching production) on port 5432).

2. **Spin up the Postgres Container**
   ```bash
   docker-compose up -d
   ```
   (This starts the **local-postgres** container in "detatched" mode. It will pull the postgres:18.1 image and bind it to port 5432).
   
3. **Run the Application:**
   Run HearingsDemoApplication via your IDE. 

### Accessing the Local Database
To inspect the local database, you must run psql inside the container:
```bash
docker exec -it local-postgres psql -U local_user -d hearings_demo
```
**Useful commands:**
- \dt (list all tables)
- \d table_name (view structure of specific table)
- SELECT * FROM table_name (View table data (optional LIMIT number))
- \x (toggle expanded view)
- \q (exit database).


## 2. Remote Development (GitHub Codespaces)
When opening this repository in GitHub Codespaces, the .devcontainer automatically installs Java 17, the PostgreSQL client, and configures the IDE.
- **Database Connection:** Codespaces automatically injects GitHub Secrets (DB_URL, DB_USERNAME, DB_PASSWORD). Spring Boot detects these and connects to the remote Supabase instance, bypassing the local fallbacks.
- **Database Access:** Because the Devcontainer has the psql client installed, you can connect directly from the 
  Codespace terminal:
```bash
psql "$DB_URL"
```

### Troubleshooting
**Zscaler Certificate Errors (x509: certificate signed by unknown authority)**

If docker compose up fails to pull images locally, your corporate proxy is intercepting the connection. You must inject the Zscaler Root CA into the Colima VM:
1. Export your Zscaler certificate to your Mac home folder as ZscalerRootCA.crt.
2. Run colima ssh.
3. Convert and install it:
```bash
sudo openssl x509 -inform DER -in /Users/YOUR_NAME/ZscalerRootCA.crt -out /usr/local/share/ca-certificates/ZscalerRootCA.crt
sudo update-ca-certificates
sudo service docker restart
```
