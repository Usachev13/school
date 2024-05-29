-- liquibase formatted sql

-- changeset dusachev:1
Create index student_name_index on student(name);
create index faculty_name_and_color_index on faculty(name, color);