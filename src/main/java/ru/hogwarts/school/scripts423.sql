select student.name, student.age,faculty.name
from student
INNER JOIN faculty ON student.faculty_id = faculty.id;

select student.name, student.age, avatar.file_path
from student
inner join avatar on student.id = avatar.student_id

