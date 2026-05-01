package com.example.orderquery.repository;

import com.example.orderquery.entity.OrderReadModel;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface OrderReadModelRepository extends CassandraRepository<OrderReadModel, Long> {
}
