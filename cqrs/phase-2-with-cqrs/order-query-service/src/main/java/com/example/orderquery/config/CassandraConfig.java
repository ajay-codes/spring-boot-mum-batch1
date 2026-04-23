package com.example.orderquery.config;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
@Slf4j
public class CassandraConfig {

    @PostConstruct
    public void createKeyspace() {
        log.info("Creating Cassandra keyspace 'order_keyspace' if not exists...");
        try (CqlSession session = new CqlSessionBuilder()
                .addContactPoint(new InetSocketAddress("localhost", 9042))
                .withLocalDatacenter("datacenter1")
                .build()) {
            session.execute(
                    "CREATE KEYSPACE IF NOT EXISTS order_keyspace " +
                            "WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1}");
            log.info("Keyspace 'order_keyspace' is ready.");
        }
    }
}
