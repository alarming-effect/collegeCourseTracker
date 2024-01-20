package course.tracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import course.tracker.entity.Course;

public interface CourseDao extends JpaRepository<Course, Long> {

}
