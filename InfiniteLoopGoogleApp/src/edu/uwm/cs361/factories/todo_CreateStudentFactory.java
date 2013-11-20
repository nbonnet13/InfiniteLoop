package edu.uwm.cs361.factories;

import java.util.*;

import edu.uwm.cs361.entities.*;

public class todo_CreateStudentFactory {
	private List<String> errors = new ArrayList<String>();
	
	public Student createStudent(String username, String password, String password_repeat,
			String firstname, String lastname, String email,
			String phonenumber, Set<Course> student_courses) {
		if (username.isEmpty()) {
			errors.add("Username is required.");
		}
		if (password.isEmpty()) {
			errors.add("Password is required.");
		} else if (!password.equals(password_repeat)) {
			errors.add("Passwords do not match.");
		}
		if (phonenumber.isEmpty()) {
			errors.add("Phone Number is required.");
		}
		if (email.isEmpty()) {
			errors.add("Email is required.");
		}
		
		if(hasErrors()) {
			return null;
		} else {
			return new Student(username,password,firstname,lastname,email,student_courses);
		}
	}
	
	public List<String> getErrors() {
		return new ArrayList<String>(errors);
	}
	
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
}