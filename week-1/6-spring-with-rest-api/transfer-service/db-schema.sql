


create table accounts (
    number text primary key,
    holder_name text not null,
    balance numeric(10, 2) not null
);

insert into accounts (number, holder_name, balance) values ('1', 'Alice', 100.00);
insert into accounts (number, holder_name, balance) values ('2', 'Bob', 50.00);

select * from accounts;