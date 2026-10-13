package com.example.capacity_connect.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${spring.datasource.url:#{null}}")
    private String springDatasourceUrl;

    @Value("${spring.datasource.username:#{null}}")
    private String springDatasourceUsername;

    @Value("${spring.datasource.password:#{null}}")
    private String springDatasourcePassword;

    @Value("${DATABASE_URL:#{null}}")
    private String renderDatabaseUrl;

    @Value("${DB_HOST:#{null}}")
    private String dbHost;

    @Value("${DB_PORT:5432}")
    private String dbPort;

    @Value("${DB_NAME:#{null}}")
    private String dbName;

    @Value("${DB_USERNAME:#{null}}")
    private String dbUsername;

    @Value("${DB_PASSWORD:#{null}}")
    private String dbPassword;

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        // 1. Check for Render standard DATABASE_URL (e.g. postgresql://user:pass@host:5432/dbname)
        if (renderDatabaseUrl != null && !renderDatabaseUrl.isBlank()) {
            log.info("Configuring DataSource from DATABASE_URL environment variable");
            try {
                if (renderDatabaseUrl.startsWith("jdbc:")) {
                    config.setJdbcUrl(renderDatabaseUrl);
                    if (springDatasourceUsername != null) config.setUsername(springDatasourceUsername);
                    if (springDatasourcePassword != null) config.setPassword(springDatasourcePassword);
                } else {
                    URI uri = new URI(renderDatabaseUrl);
                    String host = uri.getHost();
                    int port = uri.getPort() > 0 ? uri.getPort() : 5432;
                    String path = uri.getPath();
                    if (path.startsWith("/")) {
                        path = path.substring(1);
                    }

                    String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + path;
                    if (uri.getQuery() != null && !uri.getQuery().isBlank()) {
                        jdbcUrl += "?" + uri.getQuery();
                    }
                    config.setJdbcUrl(jdbcUrl);
                    config.setDriverClassName("org.postgresql.Driver");

                    if (uri.getUserInfo() != null) {
                        String[] userPass = uri.getUserInfo().split(":", 2);
                        config.setUsername(userPass[0]);
                        if (userPass.length > 1) {
                            config.setPassword(userPass[1]);
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to parse DATABASE_URL as URI, fallback to direct jdbc URL", e);
                config.setJdbcUrl(renderDatabaseUrl);
            }
        }
        // 2. Check for individual DB_* variables (Render Blueprint fromDatabase specification)
        else if (dbHost != null && !dbHost.isBlank() && dbName != null && !dbName.isBlank()) {
            log.info("Configuring PostgreSQL DataSource from DB_HOST, DB_NAME, etc.");
            String jdbcUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;
            config.setJdbcUrl(jdbcUrl);
            config.setDriverClassName("org.postgresql.Driver");
            if (dbUsername != null) config.setUsername(dbUsername);
            if (dbPassword != null) config.setPassword(dbPassword);
        }
        // 3. Fallback to spring.datasource.* properties (Local development / custom env vars)
        else {
            log.info("Configuring DataSource from spring.datasource.* properties");
            String defaultUrl = "jdbc:mysql://localhost:3306/capacity-connect?createDatabaseIfNotExist=true";
            String url = (springDatasourceUrl != null && !springDatasourceUrl.isBlank()) ? springDatasourceUrl : defaultUrl;
            config.setJdbcUrl(url);
            if (springDatasourceUsername != null) config.setUsername(springDatasourceUsername);
            if (springDatasourcePassword != null) config.setPassword(springDatasourcePassword);
        }

        // Production-friendly connection pool defaults for Render Free Tier (512MB RAM)
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(300000);
        config.setMaxLifetime(1800000);

        return new HikariDataSource(config);
    }
}
