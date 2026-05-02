# Архитектура БД Деятеля

Для проектирования использовалась dbdiagram.io.
- [LMS Деятель](https://dbdiagram.io/d/LMS-6251d74f2514c97903ffada1):


```
Table users {
  id int [pk]
  open_id uuid [unique]  // Для logto
  first_name varchar(100)
  last_name varchar(100)
  patronymic varchar(100)
  birth_date date

  Indexes {
    (open_id)  // поиск по email при логине
    (last_name, first_name, patronymic)  // поиск по ФИО
  }
}

Table grades {
  id int [pk]
  code varchar(8)
  grade_name varchar(50)
  grade_type varchar(20)
  supervisor_id int

  Indexes {
    (supervisor_id)  // связь с заведующим
    (grade_name)  // поиск по названию
  }
}
Ref: grades.supervisor_id > users.id

Table disciplines {
  id int [pk]
  discipline_name varchar(50)
  supervisor_id int
  description text
  semester_id int
  grade_id int

  Indexes {
    (supervisor_id)  // дисциплины преподавателя
    (semester_id)  // дисциплины семестра
    (discipline_name)  // поиск по названию
  }
}
Ref: disciplines.supervisor_id > users.id

Table lessons {
  id int [pk]
  discipline_id int
  ordered_number int
  main_theme varchar(200) [not null]
  description text [not null]
  teacher_file_link text [null]
  students_file_link text [null]
  type varchar(50)
  format varchar(50)
  recommend_room varchar(20)

  Indexes {
    (discipline_id, ordered_number)
  }
}
Ref: lessons.discipline_id > disciplines.id

Table groups {
  id int [pk]
  group_name varchar(15) [unique]
  headman_id int
  first_semester_id int
  course_level int
  grade_id int

  Indexes {
    (headman_id)  // староста группы
    (course_level)  // фильтр по курсу
    (group_name)  // поиск по имени группы
  }
}
Ref: groups.headman_id > users.id
Ref: groups.grade_id > grades.id

Table timetables {
  id int [pk]
  semester_id int
  discipline_id int
  teacher_id int
  day_of_week int
  week_parity int
  room varchar(10) [null]
  start_time time
  end_time time

  Indexes {
    (semester_id)  // расписание семестра
    (teacher_id)  // расписание преподавателя
    (discipline_id)  // расписание дисциплины
    (day_of_week, week_parity)  // поиск по дню и чётности
    (room)  // поиск по аудитории
  }
}
Ref: timetables.discipline_id > disciplines.id
Ref: timetables.teacher_id > users.id

Table timetables_groups {
  timetable_id int [pk]
  group_id int [pk]

  Indexes {
    (group_id)  // поиск всех занятий группы
  }
}
Ref: timetables_groups.group_id > groups.id
Ref: timetables_groups.timetable_id > timetables.id

Table teachers_abilities {
  teacher_id int [pk]
  discipline_id int [pk]

  Indexes {
    (discipline_id)  // преподаватели, ведущие дисциплину
  }
}
Ref: teachers_abilities.teacher_id > users.id
Ref: teachers_abilities.discipline_id > disciplines.id


Table marks {
  id int [pk]
  timetable_id int
  student_id int
  lesson_real_date date

  updated_at datetime

  attendance_status varchar(20)
  mark int

  Indexes {
    (timetable_id, student_id, lesson_real_date) [unique]
    (student_id)  // успеваемость студента за семестр
    (timetable_id, lesson_real_date)  // оценки за конкретное занятие
    (attendance_status)  // фильтр по посещаемости
  }

}
Ref: marks.timetable_id > timetables.id
Ref: marks.student_id > users.id


Table semesters {
  id int [pk]
  semester_name varchar(35)
  start date
  end date

  Indexes {
    (start, end)  // поиск семестра по дате
    (semester_name)  // поиск по названию
  }
}
Ref: groups.first_semester_id > semesters.id
Ref: timetables.semester_id > semesters.id

Table homeworks {
  id int [pk]
  lesson_id int
  semester_id int
  deadline datetime
  description text
  file_link text

  Indexes {
    (lesson_id)  // задания к уроку
    (semester_id)  // задания семестра
    (deadline)  // сортировка по дедлайну
  }
}
Ref: homeworks.lesson_id > lessons.id
Ref: homeworks.semester_id > semesters.id

Table attached_homeworks {
  id int [pk]
  homework_id int
  student_id int
  mark int
  attach_date datetime

  Indexes {
    (homework_id)  // все сдачи конкретного задания
    (student_id)  // все сдачи студента
    (attach_date)  // сортировка по дате сдачи
  }
}
Ref: attached_homeworks.student_id > users.id
Ref: attached_homeworks.homework_id > homeworks.id

Table comments {
  id int [pk]
  attached_homework_id int
  from_id int
  send_time datetime
  message text

  Indexes {
    (attached_homework_id)  // комментарии к сдаче
    (send_time)  // сортировка по времени
  }
}
Ref: comments.attached_homework_id > attached_homeworks.id
Ref: comments.from_id > users.id

Table enrollments {
  id int [pk]
  student_id int
  group_id int
  start_semester_id int
  end_semester_id int

  Indexes {
    (student_id)  // вся история студента
    (group_id)  // все студенты группы за всё время
    (start_semester_id)  // зачисления в семестре
    (end_semester_id)  // отчисления/выпуски
    (student_id, end_semester_id)  // поиск активной записи (end_semester_id IS NULL)
  }
}
Ref: enrollments.student_id > users.id
Ref: enrollments.group_id > groups.id
Ref: enrollments.start_semester_id > semesters.id
Ref: enrollments.end_semester_id > semesters.id
```