alter table student
    add constraint age_fix check (age > 15),
    ADD CONSTRAINT name_unique UNIQUE (name),
    alter column name set not null,
    alter column age set default 20;
alter table faculty
    add constraint name_and_color_unique unique (name, color);
