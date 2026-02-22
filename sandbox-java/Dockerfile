FROM eclipse-temurin:17-jdk

WORKDIR /app

# Create non-root user (Debian syntax)
RUN groupadd -r appgroup && useradd -r -g appgroup appuser
USER appuser

CMD ["sh"]