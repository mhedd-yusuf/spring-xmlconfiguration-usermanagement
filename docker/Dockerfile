# Use official IBM WebSphere Liberty base image
# Options: websphere-liberty (commercial) or open-liberty (open source)
FROM icr.io/appcafe/open-liberty:full-java11-openj9-ubi

# Metadata
LABEL maintainer="@mhedd-yusuf"
LABEL description="Spring User Management Application on Liberty"

# Set environment variables
ENV LICENSE=accept \
    SERVER_NAME=userManagementServer \
    JAVA_HOME=/opt/java/openjdk \
    PATH=/opt/java/openjdk/bin:$PATH

# Create directories for mounted volumes
USER root
RUN mkdir -p /opt/ibm/wlp/usr/shared/lib && \
    mkdir -p /opt/ibm/wlp/usr/servers/${SERVER_NAME}/apps && \
    mkdir -p /opt/ibm/wlp/usr/servers/${SERVER_NAME}/logs && \
    chown -R 1001:0 /opt/ibm/wlp/usr/shared/lib && \
    chown -R 1001:0 /opt/ibm/wlp/usr/servers/${SERVER_NAME}

# Switch back to non-root user
USER 1001

# Expose ports
# 9080 - HTTP
# 9443 - HTTPS
# 7777 - Debug port (optional)
EXPOSE 9080 9443 7777

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:9080/user-management/api/users || exit 1

# The server.xml, apps, and libs will be mounted as volumes
# So we don't COPY them into the image