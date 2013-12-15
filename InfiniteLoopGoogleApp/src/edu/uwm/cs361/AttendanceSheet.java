package edu.uwm.cs361;

import java.io.IOException;
import java.util.*;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.cs361.entities.*;
import edu.uwm.cs361.factories.PersistenceFactory;
import edu.uwm.cs361.factories.StudentChargesFactory;

@SuppressWarnings("serial")
public class AttendanceSheet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException	{
		PersistenceManager pm = PersistenceFactory.getPersistenceManager();
		
		String username = null;

		Cookie[] cookies = req.getCookies();
		
		if (cookies != null) {
			for (Cookie c : cookies) {

		 if(c.getName().equals("Teachername")){
				username = c.getValue();
			}
		 if(c.getName().equals("Adminname")){
			Cookie admin = new Cookie("Adminname", null);
			admin.setMaxAge(0);
			resp.addCookie(admin);
			resp.sendRedirect("/login.jsp");
		}
			 if(c.getName().equals("Studentname")){
				Cookie student = new Cookie("Studentname", null);
				student.setMaxAge(0);
				resp.addCookie(student);
				resp.sendRedirect("/login.jsp");
		}
			 if(c.getName().isEmpty()){
					resp.sendRedirect("/login.jsp");
				}
				
			}
		}
				
		req.setAttribute("username", username);
		
		Teacher teacher = getTeacher(username);		
		
		req.setAttribute("courses", getCourses(teacher));
		req.setAttribute("students", getStudents(teacher));
		
		
		req.getParameter("course_id");
		Course course = (Course)pm.getObjectById(Course.class,Long.parseLong((String)req.getParameter("course_id")));
		req.setAttribute("course_select", course);
		
		Date date1 = course.getStartDate();
		Date date2 = course.getEndDate();
		
		int diffInDays = getDiffInDate(date1,date2);
		
		req.setAttribute("weeks", diffInDays);
		req.setAttribute("students", course.getStudents());
		req.getRequestDispatcher("AttendanceSheet.jsp").forward(req, resp);
		
	}
	
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException	{
		PersistenceManager pm = PersistenceFactory.getPersistenceManager();
		
		
		
		
		
		try{
			//List<Student> students = getStudents(pm);
			//Date currentDate = new Date();
			//String reason, s_amount;
			//StudentChargesFactory charge_fact = new StudentChargesFactory();
			
			String w = req.getParameter("work_please");
			
			System.out.println(w);
			
			int number_of_week = Integer.parseInt(w);
			
		
			
			StudentAttendance c;

				Course course = (Course) pm.getObjectById(Course.class,Long.parseLong(req.getParameter("course_id")));
				
				Set<Student> students = course.getStudents();
				Set<String> meetDays = course.getMeetingDays();
				
				
				
				
				//List <String> days = new ArrayList<String>();
				String name; 
				
				Map<String,Boolean> attendance_map;
				
				
				for (Student student : students){
				String [] s_attendance = req.getParameterValues(student.getUser_id()+"_attendance");
				
				List <String> days = new ArrayList<String>();
				
				
				//String [] student_attendance = req.getParameterValues("attendance");
				if(s_attendance != null){
				for(int i =0 ; i < s_attendance.length; i++){
					
					System.out.print(s_attendance[i]);
					
					if(s_attendance[i].equals("1")){
						days.add(s_attendance[i]);
						i++;
					}
					else{
						days.add(s_attendance[i]);
					}
				}
				}
				
				
				
				name = student.getFullName();
				System.out.println(name);
				c = new StudentAttendance(days, number_of_week, name, meetDays);
				pm.makePersistent(c);
				course.getAttendance().add(c);
				pm.flush();
			
				
				}
				
				req.getRequestDispatcher("Attendance.jsp").forward(req, resp);
				
		} finally {
			pm.close();
		}
		
		
		
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	private Teacher getTeacher(String username) {
		PersistenceManager pm = getPersistenceManager();
		List<Teacher> teachers = new ArrayList<Teacher>();
		Teacher teacher = null;
		try {
			teachers = (List<Teacher>) pm.newQuery(Teacher.class).execute();
			for (Teacher t : teachers) {
				if (t.getUsername().equals(username)) {
					teacher = t;
				
					teacher.getCourses(); 
				}
			}
		} finally {
			pm.close();
		}
		return teacher;
	}
	

	private Set<Course> getCourses(Teacher teacher) {
		PersistenceManager pm = getPersistenceManager();
		Set<Course> courses = new HashSet<Course>();
		try {
			if (teacher != null) {
				if (teacher.getCourses() != null) {
					courses = teacher.getCourses();
				}
			}
		} finally {
			pm.close();
		}
		return courses;
	}
	
	private Set<Student> getStudents(Teacher teacher) {
		PersistenceManager pm = getPersistenceManager();
		Set<Student> students = new HashSet<Student>();
		try {
			if (teacher != null) {
				if (teacher.getStudents() != null) {
					students = teacher.getStudents();
				}
			}
		} finally {
			pm.close();
		}
		return students;
	}
	
	private int getDiffInDate(Date date1, Date date2){
		
		final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

		int diffInDays = (int) ((date2.getTime() - date1.getTime())/ DAY_IN_MILLIS );
		return (int) Math.ceil((double)diffInDays/7);
	}
	private PersistenceManager getPersistenceManager() {
		return JDOHelper.getPersistenceManagerFactory("transactions-optional").getPersistenceManager();
	}
}