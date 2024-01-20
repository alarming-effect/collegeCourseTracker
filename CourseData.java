package course.tracker.controller.model;

import java.util.HashSet;
import java.util.Set;

import course.tracker.entity.Course;
import course.tracker.entity.Instructor;
import course.tracker.entity.Student;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CourseData {
	private Long courseId;
	private String courseName;
	private int courseHours;
	private String courseLocation;
	private int courseCapacity;
	private int courseLevel;
	private Set<CourseStudent> students = new HashSet<>();
	
	public CourseData(Course course) {
		courseId = course.getCourseId();
		courseName = course.getCourseName();
		courseHours = course.getCourseHours();
		courseLocation = course.getCourseLocation();
		courseCapacity = course.getCourseCapacity();
		courseLevel = course.getCourseLevel();
		
		for (Student student : course.getStudent()) {
			students.add(new CourseStudent(student));
		}
	}//CourseData

@Data
@NoArgsConstructor
public static class CourseInstructor {
	private Long instructorId;
	private String instructorFirstName;
	private String instructorLastName;
	private String instructorEmail;
	private String instructorPhone;
	private String instructorTitle;

	private Set<CourseData> courses = new HashSet<>();
	
	public CourseInstructor(Instructor instructor) {
		instructorId = instructor.getInstructorId();
		instructorFirstName = instructor.getInstructorFirstName();
		instructorLastName = instructor.getInstructorLastName();
		instructorEmail = instructor.getInstructorEmail();
		instructorPhone = instructor.getInstructorPhone();
		instructorTitle = instructor.getInstructorTitle();
		
		for (Course course : instructor.getCourses()) {
			courses.add(new CourseData(course));}
	}
}//CourseInstructor

@Data
@NoArgsConstructor
public static class CourseStudent {
	private Long studentId;
	private String studentLastName;
	private String studentFirstName;
	private String studentEmail;
	private String studentPhone;
	private String major;
	private int completedCredits;
	
	public CourseStudent(Student student) {
		studentId= student.getStudentId();
		studentLastName = student.getStudentLastName();
		studentFirstName = student.getStudentFirstName();
		studentEmail = student.getStudentEmail();
		studentPhone = student.getStudentPhone();
		major = student.getMajor();
		completedCredits = student.getCompletedCredits();
	}
}//CourseStudent
}//end
		
