-- liquibase formatted sql

-- changeset tim:1
CREATE INDEX students_name_index ON students(name);

-- changeset tim:2
CREATE INDEX faculties_name_index ON faculties(name);
CREATE INDEX faculties_color_index ON faculties(color);