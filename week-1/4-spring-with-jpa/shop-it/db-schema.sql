-- db-schema.sql
-- Reference schema for the price_matrix table used by Shop-It (Module 4).
-- Spring Boot with JPA (ddl-auto=update) manages the 'orders' table automatically.

create table if not exists price_matrix (
    id serial primary key,
    product_id int not null unique,
    price decimal(10, 2) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

insert into price_matrix (product_id, price) values
(1, 19.99),
(2, 29.99),
(3, 39.99);
