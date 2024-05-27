create table car (
                     id int primary key unique,
                     brand text,
                     model text,
                     price int

);
create table person (
                        id int,
                        name text primary key,
                        licence boolean,
                        car_id int references car(id)
);

