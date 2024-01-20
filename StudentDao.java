package course.tracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import course.tracker.entity.Student;

public interface StudentDao extends JpaRepository<Student, Long> {

}
