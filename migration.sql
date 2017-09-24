create table goods(
    id INT primary key auto_increment,
    name varchar(30),
    price MEDIUMINT
);

insert into goods(name, price) values ('apple', 100);
insert into goods(name, price) values ('orange', 1000);
insert into goods(name, price) values ('big,apple', 1000);
insert into goods(name, price) values ('small"orange"', 1000);

