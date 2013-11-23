package edu.uwm.cs361;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;


import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import edu.uwm.cs361.entities.Charge;
import edu.uwm.cs361.entities.Student;

public class EmailService {
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat ("MM/dd/yyyy");
	
	public static void SendDeadlineEmails() {
		PersistenceManager pm = getPersistenceManager();
		try {
			List<DeadlineEmailObject> studentsWithDeadlineToday = populateEmailList(pm);
			sendEmails(studentsWithDeadlineToday);
		} finally {
			pm.close();
		}
	}

	private static List<DeadlineEmailObject> populateEmailList(PersistenceManager pm) {
		List<Student> students =  getStudents(pm);
		List<DeadlineEmailObject> studentsWithDeadlineToday = new ArrayList<DeadlineEmailObject>();
		String today = dateFormatter.format(System.currentTimeMillis());
		
		for(Student student : students) {
			Set<Charge> charges = student.getCharges();
			if(charges != null) {
				for(Charge charge : charges) {
					String deadline = dateFormatter.format(charge.getDeadline());
					if(deadline.equals(today)) {
						studentsWithDeadlineToday.add(new DeadlineEmailObject(student, charge));
					}
				}
			}
		}
		return studentsWithDeadlineToday;
	}

	@SuppressWarnings("unchecked")
	private static List<Student> getStudents(PersistenceManager pm) {
		return (List<Student>) pm.newQuery(Student.class).execute();
	}
	
	private static void sendEmails(List<DeadlineEmailObject> studentsWithDeadlineToday) {
		for(DeadlineEmailObject emailObj : studentsWithDeadlineToday) {
			sendEmail(emailObj);
			System.out.println("Student: " + emailObj.getStudent().getFullName() + "\nAmount: " + emailObj.getCharge().getAmount() + 
					"\nDeadline: " + dateFormatter.format(emailObj.getCharge().getDeadline()));
		}
	}

	private static void sendEmail(DeadlineEmailObject emailObj) {
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("njbuwm@gmail.com", "Admin"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailObj.getStudent().getEmail(), emailObj.getStudent().getFullName()));
		    msg.setSubject("Deadline Reached");
			msg.setText(buildMessage(emailObj));
			Transport.send(msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}

	private static String buildMessage(DeadlineEmailObject emailObj) {
		String email = "";
		email += "Dear " + emailObj.getStudent().getFullName() + " ,\n";
		email += "You owe us money. This much: $" + emailObj.getCharge().getAmount() + ".\n";
		email += "For this reason: " + emailObj.getCharge().getReason() + ".\n";
		email += "The deadline is today and I advise you to pay it or you will be deported to Idontpaymybills Island forever.\n";
		email += "Thank you,\nAutomated Emailer";
		return email;
	}

	private static PersistenceManager getPersistenceManager() {
		return JDOHelper.getPersistenceManagerFactory("transactions-optional").getPersistenceManager();
	}
}
