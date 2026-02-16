# 1. Start from the official Java 17 image (Fast & Cached)
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