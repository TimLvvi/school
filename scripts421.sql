 --1.1 Возраст студента не может быть меньше 16 лет.
 --1.2 Имена студентов должны быть уникальными и не равны нулю.
 --1.3 Пара “значение названия” - “цвет факультета” должна быть уникальной.
 --1.4 При создании студента без возраста ему автоматически должно присваиваться 20 лет.

    --1.1
    alter table students add constraint age_constraint check (age>=16);

    --1.2
    alter table students alter column name set not null;
    alter table students add constraint name_unique unique (name);

    --1.3
    alter table faculties add constraint faculties_constraint unique (name, color);

    --1.4
    alter table students  alter column age set default 20;





