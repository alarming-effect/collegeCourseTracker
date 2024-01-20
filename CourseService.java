package course.tracker.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import course.tracker.controller.model.CourseData;
import course.tracker.controller.model.CourseData.CourseInstructor;
import course.tracker.controller.model.CourseData.CourseStudent;
import course.tracker.dao.CourseDao;
import course.tracker.dao.InstructorDao;
import course.tracker.dao.StudentDao;
import course.tracker.entity.Course;
import course.tracker.entity.Instructor;
import course.tracker.entity.Student;

@Service
public class CourseService {
	@Autowired
	private CourseDao courseDao;
	@Autowired
	private StudentDao studentDao;
	@Autowired
	private InstructorDao instructorDao;

	@Transactional(readOnly = false)
	public CourseData saveCourse(Long instructorId, CourseData courseData) {
		Long courseId = courseData.getCourseId();
		Course course = findOrCreateCourse(instructorId, courseId);
		Instructor instructor = findInstructorById(instructorId);

		copyCourseFields(course, courseData);
		course.setInstructor(instructor);
		instructor.getCourses().add(course);

		return new CourseData(courseDao.save(course));
	}// saveCourse

	private void copyCourseFields(Course course, CourseData courseData) {
		course.setCourseId(courseData.getCourseId());
		course.setCourseName(course.getCourseName());
		course.setCourseHours(courseData.getCourseHours());
		course.setCourseLevel(courseData.getCourseLevel());
		course.setCourseCapacity(courseData.getCourseCapacity());
		course.setCourseLocation(courseData.getCourseLocation());
	}// copyCourseFields

	private Course findOrCreateCourse(Long instructorId, Long courseId) {
		if (Objects.isNull(courseId)) {
			return new Course();
		} else {
			return findCourseById(instructorId, courseId);
		}
	}// findOrCreateCourse

	private Course findCourseById(Long instructorId, Long courseId) {
		Course course = courseDao.findById(courseId)
				.orElseThrow(() -> new NoSuchElementException("Course with ID= [" + courseId + "] was not found."));
		if(course.getInstructor().getInstructorId() != instructorId ){
			throw new IllegalArgumentException("Course with ID= " + courseId + " is not assigned to instructor with ID=" + instructorId + " ." );
		}
		return course;
	}// findCourseById
	
	private Course searchCourseById(Long courseId) {
		Course course = courseDao.findById(courseId)
				.orElseThrow(() -> new NoSuchElementException("Course with ID= [" + courseId + "] was not found."));
	
		return course;
	}// findCourseById

	private void copyStudentFields(Student student, CourseStudent courseStudent) {
		student.setStudentId(courseStudent.getStudentId());
		student.setStudentFirstName(courseStudent.getStudentFirstName());
		student.setStudentLastName(courseStudent.getStudentLastName());
		student.setMajor(courseStudent.getMajor());
		student.setStudentEmail(courseStudent.getStudentEmail());
		student.setStudentPhone(courseStudent.getStudentPhone());
		student.setCompletedCredits(courseStudent.getCompletedCredits());
	}// copyStudentFields

	private void copyInstructorFields(Instructor instructor, CourseInstructor courseInstructor) {
		instructor.setInstructorId(courseInstructor.getInstructorId());
		instructor.setInstructorFirstName(courseInstructor.getInstructorFirstName());
		instructor.setInstructorLastName(courseInstructor.getInstructorLastName());
		instructor.setInstructorEmail(courseInstructor.getInstructorEmail());
		instructor.setInstructorPhone(courseInstructor.getInstructorPhone());
		instructor.setInstructorTitle(courseInstructor.getInstructorTitle());
	}// copyInstructorFields

	private Student findOrCreateStudent(Long courseId, Long studentId) {
		if (Objects.isNull(studentId)) {
			return new Student();
		}
		return findStudentById(courseId, studentId);
	}// findOrCreateStudent

	private Student findStudentById(Long courseId, Long studentId) {
		Student student = studentDao.findById(studentId)
				.orElseThrow(() -> new NoSuchElementException("Student with ID= [" + studentId + "] was not found."));
		boolean found = false;
		for (Course course : student.getCourses()) {
			if (course.getCourseId() == courseId) {
				found = true;
				break;
			}
		}
		if (!found) {
			throw new IllegalArgumentException("The student with ID = [" + studentId
					+ "] is not enrolled in the course with ID = [" + courseId + "].");
		}
		return student;
	}// findStudentById

	private Instructor findInstructorById(Long instructorId) {
		return instructorDao.findById(instructorId).orElseThrow(
				() -> new NoSuchElementException("Instructor with ID= [" + instructorId + "] was not found."));
	}// findInstructorById

	private Instructor findOrCreateInstructor(Long instructorId) {
		if (Objects.isNull(instructorId)) {
			return new Instructor();
		}
		return findInstructorById(instructorId);
	}// findOrCreateInstructor

	@Transactional(readOnly = false)
	public CourseInstructor saveInstructor(CourseInstructor courseInstructor) {
		Long instructorId = courseInstructor.getInstructorId();
		Instructor instructor = findOrCreateInstructor(instructorId);

		copyInstructorFields(instructor, courseInstructor);

		// instructor.getCourses().add(course);

		return new CourseInstructor(instructorDao.save(instructor));
	}// saveInstructor

	@Transactional(readOnly = false)
	public CourseStudent saveStudent(Long courseId, CourseStudent courseStudent) {
		Course course = searchCourseById(courseId);
		Long studentId = courseStudent.getStudentId();
		Student student = findOrCreateStudent(courseId, studentId);

		copyStudentFields(student, courseStudent);

		student.getCourses().add(course);
		course.getStudent().add(student);

		Student dbStudent = studentDao.save(student);
		return new CourseStudent(dbStudent);
	}// saveStudent

	@Transactional(readOnly = true)
	public List<CourseData> retrieveAllCourses() {
		List<Course> courses = courseDao.findAll();
		List<CourseData> result = new LinkedList<>();

		for (Course course : courses) {
			CourseData cd = new CourseData(course);

			result.add(cd);
		}
		return result;
	}// retrieveAllCourses

	@Transactional(readOnly = true)
	public CourseData retrieveCourseById(Long instructorId, Long courseId) {
		return new CourseData(findCourseById(instructorId, courseId));
	}// retrieveCourseById

	@Transactional(readOnly = false)
	public void deleteCourseById(Long instructorId, Long courseId) {
		Course course = findCourseById(instructorId, courseId);
		courseDao.delete(course);
	}// deleteCourseById

	@Transactional(readOnly = false)
	public void deleteInstructorById(Long instructorId) {
		Instructor instructor = findInstructorById(instructorId);
		instructorDao.delete(instructor);
	}//deleteInstructorById

	@Transactional(readOnly = false)
	public void deleteStudentById(Long courseId, Long studentId) {
		Student student = findStudentById(courseId, studentId);
		studentDao.delete(student);
	}//deleteStudentById

}// end
