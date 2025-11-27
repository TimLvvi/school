--3.1 Составить первый JOIN-запрос, чтобы получить информацию обо всех студентах (достаточно получить только имя и возраст студента) школы Хогвартс вместе с названиями факультетов.
--3.2 Составить второй JOIN-запрос, чтобы получить только тех студентов, у которых есть аватарки.

--3.1
select students.name, students.age, faculties.name from students join faculties on faculty_id = faculties.id;

--3.2
select students.name, students.age, faculties.name, avatars.file_path, avatars.file_size from students join faculties on students.faculty_id = faculties.id join avatars on students.id = avatars.student_id;