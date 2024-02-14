
package com.example.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.example.university.model.Course;
import com.example.university.model.Student;
import com.example.university.repository.CourseJpaRepository;
import com.example.university.repository.ProfessorJpaRepository;
import com.example.university.repository.StudentJpaRepository;
import com.example.university.repository.StudentRepository;

@Service
public class StudentJpaService implements StudentRepository {
	@Autowired
	CourseJpaRepository courseJpaRepository;

	@Autowired
	StudentJpaRepository studentJpaRepository;

	@Autowired
	ProfessorJpaRepository professorJpaRepository;

	@Override
	public ArrayList<Student> getStudents() {
		List<Student> studentList = studentJpaRepository.findAll();
		ArrayList<Student> students = new ArrayList<>(studentList);
		return students;
	}

	@Override
	public Student getStudentById(int studentId) {
		try {
			Student student = studentJpaRepository.findById(studentId).get();
			return student;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public Student addStudent(Student student) {
		List<Integer> courseIds = new ArrayList<>();
		for (Course course : student.getCourses()) {
			courseIds.add(course.getCourseId());
		}

		List<Course> courses = courseJpaRepository.findAllById(courseIds);

		student.setCourses(courses);

		for (Course course : courses) {
			course.getStudents().add(student);
		}

		Student savedStudent = studentJpaRepository.save(student);

		courseJpaRepository.saveAll(courses);

		return savedStudent;
	}

	@Override
	public Student updateStudent(int studentId, Student student) {
		try {
			Student newStudent = studentJpaRepository.findById(studentId).get();

			if (student.getStudentName() != null) {
				newStudent.setStudentName(student.getStudentName());
			}
			if (student.getEmail() != null) {
				newStudent.setEmail(student.getEmail());
			}
			if (student.getCourses() != null) {
				List<Course> courses = newStudent.getCourses();
				for (Course course : courses) {
					course.getStudents().remove(newStudent);
				}
				courseJpaRepository.saveAll(courses);

				List<Integer> newCourseIds = new ArrayList<>();
				for (Course course : student.getCourses()) {
					newCourseIds.add(course.getCourseId());
				}

				List<Course> newCourses = courseJpaRepository.findAllById(newCourseIds);

				for (Course course : newCourses) {
					course.getStudents().add(newStudent);
				}
				courseJpaRepository.saveAll(newCourses);
				newStudent.setCourses(newCourses);
			}
			return studentJpaRepository.save(newStudent);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public void deleteStudent(int studentId) {
		try{
			Student student = studentJpaRepository.findById(studentId).get();

			List<Course> courses = student.getCourses();
			for(Course course : courses){
				course.getStudents().remove(student);
			}
			courseJpaRepository.saveAll(courses);

		    studentJpaRepository.deleteById(studentId);
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND);

	}

	@Override
	public List<Course> getStudentCourses(int studentId) {
		try {
			Student student = studentJpaRepository.findById(studentId).get();
			return student.getCourses();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

}