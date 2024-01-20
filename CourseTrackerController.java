package course.tracker.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import course.tracker.controller.model.CourseData;
import course.tracker.controller.model.CourseData.CourseInstructor;
import course.tracker.controller.model.CourseData.CourseStudent;
import course.tracker.service.CourseService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/course_tracker")
@Slf4j
public class CourseTrackerController {
	@Autowired
	private CourseService courseService;

	@PostMapping("/instructor")
	@ResponseStatus(code = HttpStatus.CREATED)
	public CourseInstructor createInstructor(@RequestBody CourseInstructor courseInstructor) {
		log.info("Creating Instructor with ID= {}", courseInstructor);
		return courseService.saveInstructor(courseInstructor);
	}// createInstructor

	@PutMapping("/{instructorId}/{courseId}/course")
	public CourseData updateCourse(@PathVariable Long instructorId, @PathVariable Long courseId,
			@RequestBody CourseData courseData) {
		courseData.setCourseId(courseId);
		log.info("Updating course number {}", courseData);
		return courseService.saveCourse(instructorId, courseData);

	}// updateCourse

	@PostMapping("/{courseId}/student")
	@ResponseStatus(code = HttpStatus.CREATED)
	public CourseStudent addStudentToCourse(@PathVariable Long courseId, @RequestBody CourseStudent courseStudent) {
		log.info("Adding student with ID number = {} to course number = {}", courseStudent, courseId);
		return courseService.saveStudent(courseId, courseStudent);
	}// addStudentToCourse

	@PostMapping("/{instructorId}/course")
	@ResponseStatus(code = HttpStatus.CREATED)
	public CourseData addCourseToInstructor(@PathVariable Long instructorId, @RequestBody CourseData courseData) {
		log.info("Adding course number = {} to instructor with ID = {}.", courseData, instructorId);
		return courseService.saveCourse(instructorId, courseData);
	}// addStudentToCourse

	@GetMapping
	public List<CourseData> retrieveAllCourses() {
		log.info("Retrieving all courses...");
		return courseService.retrieveAllCourses();
	}// retrieveAllCourses

	@GetMapping("/{instructorId}/{courseId}")
	public CourseData retrieveCourseById(@PathVariable Long instructorId, @PathVariable Long courseId) {
		log.info("Retrieving course with ID = {}", courseId);
		return courseService.retrieveCourseById(instructorId, courseId);
	}// retrieveCourseById

	@DeleteMapping("/{instructorId}/{courseId}")
	public Map<String, String> deleteCourseById(@PathVariable Long instructorId, @PathVariable Long courseId) {
		log.info("Deleting course with ID = {}", courseId);

		courseService.deleteCourseById(instructorId, courseId);

		return Map.of("message", "Course with ID = [" + courseId + "] was deleted.");
	}// deleteCourseById

	@DeleteMapping("/{instructorId}")
	public Map<String, String> deleteInstructorById(@PathVariable Long instructorId) {
		log.info("Deleteing instructor with ID = {}", instructorId);

		courseService.deleteInstructorById(instructorId);

		return Map.of("message", "Instructor with ID = [" + instructorId + "] was deleted.");
	}// deleteInstructorById

	@DeleteMapping("/{courseId}/student")
	public Map<String, String> deleteStudentById(@PathVariable Long courseId, @PathVariable Long studentId) {
		log.info("Deleteing student with ID = {} from course with ID = {}", studentId, courseId);

		courseService.deleteStudentById(courseId, studentId);

		return Map.of("message", "Student with ID = [" + studentId + "] was deleted from course number [" + courseId + "].");
	}//deleteStudentById

}// end of CourseTrackerController

// end
