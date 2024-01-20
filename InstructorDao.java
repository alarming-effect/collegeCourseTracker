package course.tracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import course.tracker.entity.Instructor;

public interface InstructorDao extends JpaRepository<Instructor, Long> {

}
