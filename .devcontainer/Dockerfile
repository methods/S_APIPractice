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