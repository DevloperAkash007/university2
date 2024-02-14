/*
 *
 * You can use the following import statements
 * 
 * import java.util.ArrayList;
 * 
 */
package com.example.university.repository;

import com.example.university.model.Professor;
import com.example.university.model.Course;
import java.util.ArrayList;
import java.util.List;

public interface ProfessorRepository {
    ArrayList<Professor> getProfessors();

    Professor getProfessorById(int professorId);

    Professor addProfessor(Professor professor);

    Professor updateProfessor(int professorId, Professor professor);

    void deleteProfessor(int professorId);

    ArrayList<Course> getProfessorCourses(int professorId);

}