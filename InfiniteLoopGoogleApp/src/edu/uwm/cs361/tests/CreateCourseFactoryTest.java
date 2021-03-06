package edu.uwm.cs361.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.uwm.cs361.entities.Course;
import edu.uwm.cs361.entities.Teacher;
import edu.uwm.cs361.factories.CreateCourseFactory;
import edu.uwm.cs361.factories.PersistenceFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateCourseFactoryTest {
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	private PersistenceManager pm;

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() {
		helper.setUp();
		pm = PersistenceFactory.getPersistenceManager();

		List<Teacher> teachers = (List<Teacher>) pm.newQuery(Teacher.class).execute();
		for (Teacher teacher : teachers) {
			pm.deletePersistent(teacher);
		}
		
		List<Course> courses = (List<Course>) pm.newQuery(Course.class).execute();
		for (Course course : courses) {
			pm.deletePersistent(course);
		}
		
		pm.flush();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testErrorOnBlankClassname() {
		CreateCourseFactory course_fact = new CreateCourseFactory();
		Teacher teacher = new Teacher("username","password1","fname","lname", "email", "8478478478", new String[] {"teacher1","teacher2"});
		Set<String> meetingDays = new HashSet<String>(Arrays.asList(new String[] { "M", "T", "W" }));
		pm.makePersistent(teacher);
		Course c = course_fact.createCourse(pm, "", "2013-10-14", "2013-10-15", meetingDays, "10:30", "EMS145", "Pay now", "its a class", ""+teacher.getUser_id().getId());

		assertNull(c);
		assertTrue(course_fact.hasErrors());
		assertEquals(1, course_fact.getErrors().size());
		assertTrue(course_fact.getErrors().get(0).equals("Please enter a class name."));
	}

	@Test
	public void testErrorOnBlankStartDate() {
		CreateCourseFactory course_fact = new CreateCourseFactory();
		Set<String> meetingDays = new HashSet<String>(Arrays.asList(new String[] { "M", "T", "W" }));
		Teacher teacher = new Teacher("username","password1","fname","lname", "email", "8478478478", new String[] {"teacher1","teacher2"});
		pm.makePersistent(teacher);
		Course c = course_fact.createCourse(pm, "learning101", "", "2013-10-15", meetingDays, "10:30", "EMS145", "Pay now", "its a class", ""+teacher.getUser_id().getId());

		assertNull(c);
		assertTrue(course_fact.hasErrors());
		assertEquals(1, course_fact.getErrors().size());
		assertTrue(course_fact.getErrors().get(0).equals("Please enter a class start date."));
	}
	
	@Test
	public void testErrorOnBlankEndDate() {
		CreateCourseFactory course_fact = new CreateCourseFactory();
		Set<String> meetingDays = new HashSet<String>(Arrays.asList(new String[] { "M", "T", "W" }));
		Teacher teacher = new Teacher("username","password1","fname","lname", "email", "8478478478", new String[] {"teacher1","teacher2"});
		pm.makePersistent(teacher);
		Course c = course_fact.createCourse(pm, "learning101", "2013-10-14", "", meetingDays, "10:30", "EMS145", "Pay now", "its a class", ""+teacher.getUser_id().getId());

		assertNull(c);
		assertTrue(course_fact.hasErrors());
		assertEquals(1, course_fact.getErrors().size());
		assertTrue(course_fact.getErrors().get(0).equals("Please enter a class end date."));
	}
	
	@Test
	public void testErrorOnEndDateBeforeStartDate() {
		CreateCourseFactory course_fact = new CreateCourseFactory();
		Set<String> meetingDays = new HashSet<String>(Arrays.asList(new String[] { "M", "T", "W" }));
		Teacher teacher = new Teacher("username","password1","fname","lname", "email", "8478478478", new String[] {"teacher1","teacher2"});
		pm.makePersistent(teacher);
		Course c = course_fact.createCourse(pm, "learning101", "2013-10-14", "1991-10-14", meetingDays, "10:30", "EMS145", "Pay now", "its a class", ""+teacher.getUser_id().getId());

		assertNull(c);
		assertTrue(course_fact.hasErrors());
		assertEquals(1, course_fact.getErrors().size());
		assertTrue(course_fact.getErrors().get(0).equals("Start date must be before end date."));
	}
	
	@Test
	public void testErrorOnNonSelectedMeetingDays() {
		CreateCourseFactory course_fact = new CreateCourseFactory();
		Set<String> meetingDays = null;
		Teacher teacher = new Teacher("username","password1","fname","lname", "email", "8478478478", new String[] {"teacher1","teacher2"});
		pm.makePersistent(teacher);
		Course c = course_fact.createCourse(pm, "learning101", "2013-10-14", "2013-10-15", meetingDays, "10:30", "EMS145", "Pay now", "its a class", ""+teacher.getUser_id().getId());

		assertNull(c);
		assertTrue(course_fact.hasErrors());
		assertEquals(1, course_fact.getErrors().size());
		assertTrue(course_fact.getErrors().get(0).equals("Must select a meeting day."));
	}

	@Test
	public void testErrorOnBlankMeetingTime() {
		CreateCourseFactory course_fact = new CreateCourseFactory();
		Set<String> meetingDays = new HashSet<String>(Arrays.asList(new String[] { "M", "T", "W" }));
		Teacher teacher = new Teacher("username","password1","fname","lname", "email", "8478478478", new String[] {"teacher1","teacher2"});
		pm.makePersistent(teacher);
		Course c = course_fact.createCourse(pm, "learning101", "2013-10-14", "2013-10-15", meetingDays, "", "EMS145", "Pay now", "its a class", ""+teacher.getUser_id().getId());

		assertNull(c);
		assertTrue(course_fact.hasErrors());
		assertEquals(1, course_fact.getErrors().size());
		assertTrue(course_fact.getErrors().get(0).equals("Please enter a meeting time."));
	}
	
	@Test
	public void testErrorOnBlankPlace() {
		CreateCourseFactory course_fact = new CreateCourseFactory();
		Set<String> meetingDays = new HashSet<String>(Arrays.asList(new String[] { "M", "T", "W" }));
		Teacher teacher = new Teacher("username","password1","fname","lname", "email", "8478478478", new String[] {"teacher1","teacher2"});
		pm.makePersistent(teacher);
		
		Course c = course_fact.createCourse(pm, "learning101", "2013-10-14", "2013-10-15", meetingDays, "10:45", "", "Pay now", "its a class", ""+teacher.getUser_id().getId());

		assertNull(c);
		assertTrue(course_fact.hasErrors());
		assertEquals(1, course_fact.getErrors().size());
		assertTrue(course_fact.getErrors().get(0).equals("Please enter a class meeting place."));
	}
	
	@Test
	public void testErrorOnEmptyPaymentOptions() {
		CreateCourseFactory course_fact = new CreateCourseFactory();
		Set<String> meetingDays = new HashSet<String>(Arrays.asList(new String[] { "M", "T", "W" }));
		Teacher teacher = new Teacher("username","password1","fname","lname", "email", "8478478478", new String[] {"teacher1","teacher2"});
		pm.makePersistent(teacher);
		Course c = course_fact.createCourse(pm, "learning101", "2013-10-14", "2013-10-15", meetingDays, "10:45", "EMS203", "", "its a class", ""+teacher.getUser_id().getId());

		assertNull(c);
		assertTrue(course_fact.hasErrors());
		assertEquals(1, course_fact.getErrors().size());
		assertTrue(course_fact.getErrors().get(0).equals("Please enter a payment option."));
	}
	
	@Test
	public void testErrorOnBlankDescription() {
		CreateCourseFactory course_fact = new CreateCourseFactory();
		Set<String> meetingDays = new HashSet<String>(Arrays.asList(new String[] { "M", "T", "W" }));
		Teacher teacher = new Teacher("username","password1","fname","lname", "email", "8478478478", new String[] {"teacher1","teacher2"});
		pm.makePersistent(teacher);
		Course c = course_fact.createCourse(pm, "learning101", "2013-10-14", "2013-10-15", meetingDays, "10:45","EMS203", "Pay now", "", ""+teacher.getUser_id().getId());

		assertNull(c);
		assertTrue(course_fact.hasErrors());
		assertEquals(1, course_fact.getErrors().size());
		assertTrue(course_fact.getErrors().get(0).equals("Please enter a class description."));
	}
	
	@Test
	public void testSuccess() {
		CreateCourseFactory course_fact = new CreateCourseFactory();
		Set<String> meetingDays = new HashSet<String>(Arrays.asList(new String[] { "M", "T", "W" }));
		Teacher teacher = new Teacher("username","password1","fname","lname", "email", "8478478478", new String[] {"teacher1","teacher2"});
		pm.makePersistent(teacher);
		Course c = course_fact.createCourse(pm, "learning101", "2013-10-14", "2013-10-15", meetingDays, "10:45","EMS203", "Pay now","its a good class", ""+teacher.getUser_id().getId());

		assertFalse(course_fact.hasErrors());
		assertEquals(0, course_fact.getErrors().size());
		assertEquals(c.getName(), "learning101");
		assertEquals(c.getStartDateFormatted(), "10/14/2013");
		assertEquals(c.getEndDateFormatted(), "10/15/2013");
		assertEquals(c.getTime(), "10:45");
		assertEquals(c.getPlace(), "EMS203");
		assertEquals(c.getDescription(), "its a good class");
		assertEquals(c.getTeacher().getFirstName(), "fname");
		
		Iterator<String> iterator = c.getMeetingDays().iterator();
		Iterator<String> iterator2 = meetingDays.iterator();
		while(iterator.hasNext() && iterator2.hasNext()) {
			assertEquals(iterator.next(),iterator2.next());
		}

		assertEquals(c.getPaymentOption(),"Pay now");
		
		Set<Course> courses2 = teacher.getCourses();
		assertEquals(courses2.size(),1);
		assertEquals(courses2.iterator().next().getName(), "learning101");
	}
}
