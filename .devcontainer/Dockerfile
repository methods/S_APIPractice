# 1. Start from the official Java 17 image (Fast & Cached)
# NOTE: We intentionally use the floating "1-17-bookworm" tag here to keep the devcontainer
# up to date with the latest 1.x Java 17 image on Bookworm. Reproducibility is less critical
# for this development-only environment than receiving automatic security and tooling updates.
FROM mcr.microsoft.com/devcontainers/java:1-17-bookworm

# 2. Install Postgres Client & Git LFS (Build time)
RUN rm -f /etc/apt/sources.list.d/yarn.list \
    && apt-get update \
    && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends \
        postgresql-client \
        git-lfs \
    && git lfs install \
    && apt-get clean -y && rm -rf /var/lib/apt/lists/*


# 3. Create the folder where Postgres looks for certificates
RUN mkdir -p /home/vscode/.postgresql

# 4. Copy the Supabase Root CA certificate
COPY supabase-ca.crt /home/vscode/.postgresql/root.crt

# 5. Ensure the 'vscode' user owns this folder so the app can read it
RUN chown -R vscode:vscode /home/vscode/.postgresql && \
    chmod 700 /home/vscode/.postgresql && \
    chmod 600 /home/vscode/.postgresql/root.crt