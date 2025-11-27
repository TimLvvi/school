--2.1 Описание структуры: у каждого человека есть машина. Причем несколько человек могут пользоваться одной машиной. У каждого человека есть имя, возраст и признак того, что у него есть права (или их нет). У каждой машины есть марка, модель и стоимость. Также не забудьте добавить таблицам первичные ключи и связать их.

--2.1
create table car (id serial primary key, brand text not null,model text not null,price integer not null);
create table person(id serial primary key, name text not null,age integer not null,license boolean not null,car_id integer references car(id));
