package H_01;

	import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Scanner;

	public class HAMS_1 {
	    private static final String URL = "jdbc:mysql://localhost:3306/HAMS";
	    private static final String USER = "root";
	    private static final String PASSWORD = "root";
	    private static Connection con;
	    private static Scanner sc = new Scanner(System.in);

	    public static void main(String[] args) {
	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            con = DriverManager.getConnection(URL, USER, PASSWORD);
	            System.out.println("==============================================");
	            System.out.println(" HOSPITAL APPOINTMENT MANAGEMENT SYSTEM");
	            System.out.println("==============================================");
	            int choice;
	            do {
	                System.out.println("\n------------- MENU -------------");
	                System.out.println("1. Register New Patient");
	                System.out.println("2. View All Patients");
	                System.out.println("3. Add Doctor");
	                System.out.println("4. View All Doctors");
	                System.out.println("5. Book Appointment");
	                System.out.println("6. View All Appointments");
	                System.out.println("7. Search Appointments by Patient");
	                System.out.println("8. Search Appointments by Doctor");
	                System.out.println("9. Update Appointment");
	                System.out.println("10. Cancel Appointment");
	                System.out.println("11. View Patient Details");
	                System.out.println("12. Exit");
	                System.out.print("Enter your choice: ");
	                choice = readInt();
	                switch (choice) {
	                    case 1: registerPatient(); break;
	                    case 2: viewAllPatients(); break;
	                    case 3: addDoctor(); break;
	                    case 4: viewAllDoctors(); break;
	                    case 5: bookAppointment(); break;
	                    case 6: viewAllAppointments(); break;
	                    case 7: searchAppointmentsByPatient(); break;
	                    case 8: searchAppointmentsByDoctor(); break;
	                    case 9: updateAppointment(); break;
	                    case 10: cancelAppointment(); break;
	                    case 11: viewPatientDetails(); break;
	                    case 12: System.out.println("Thank you for using the system."); break;
	                    default: System.out.println("Invalid choice.");
	                }
	            } while (choice != 12);
	            con.close();
	        } catch (Exception e) {
	            System.out.println("Error: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }

	    private static void registerPatient() {
	        String sql = "INSERT INTO Patients(name, age, gender, phone) VALUES (?, ?, ?, ?)";
	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            System.out.print("Enter patient name: "); String name = sc.nextLine();
	            System.out.print("Enter age: "); int age = readInt();
	            System.out.print("Enter gender: "); String gender = sc.nextLine();
	            System.out.print("Enter phone: "); String phone = sc.nextLine();
	            ps.setString(1, name); ps.setInt(2, age); ps.setString(3, gender); ps.setString(4, phone);
	            ps.executeUpdate(); System.out.println("Patient registered successfully.");
	        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
	    }

	    private static void viewAllPatients() {
	        String sql = "SELECT patient_ID, name, age, gender, phone FROM Patients";
	        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
	            System.out.printf("%-12s %-20s %-5s %-10s %-15s%n", "Patient ID", "Name", "Age", "Gender", "Phone");
	            while (rs.next()) {
	                System.out.printf("%-12d %-20s %-5d %-10s %-15s%n", rs.getInt("patient_ID"), rs.getString("name"), rs.getInt("age"), rs.getString("gender"), rs.getString("phone"));
	            }
	        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
	    }

	    private static void addDoctor() {
	        String sql = "INSERT INTO Doctors(name, specialization, phone) VALUES (?, ?, ?)";
	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            System.out.print("Enter doctor name: "); String name = sc.nextLine();
	            System.out.print("Enter specialization: "); String specialization = sc.nextLine();
	            System.out.print("Enter phone: "); String phone = sc.nextLine();
	            ps.setString(1, name); ps.setString(2, specialization); ps.setString(3, phone);
	            ps.executeUpdate(); System.out.println("Doctor added successfully.");
	        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
	    }

	    private static void viewAllDoctors() {
	        String sql = "SELECT doctor_ID, name, specialization, phone FROM Doctors";
	        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
	            System.out.printf("%-12s %-20s %-20s %-15s%n", "Doctor ID", "Name", "Specialization", "Phone");
	            while (rs.next()) {
	                System.out.printf("%-12d %-20s %-20s %-15s%n", rs.getInt("doctor_ID"), rs.getString("name"), rs.getString("specialization"), rs.getString("phone"));
	            }
	        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
	    }

	    private static void bookAppointment() {
	        try {
	            System.out.print("Enter patient ID: "); int patientId = readInt();
	            System.out.print("Enter doctor ID: "); int doctorId = readInt();
	            System.out.print("Enter appointment date (YYYY-MM-DD): "); String date = sc.nextLine();
	            System.out.print("Enter appointment time (HH:MM:SS): "); String time = sc.nextLine();
	            System.out.print("Enter reason: "); String reason = sc.nextLine();
	            String sql = "INSERT INTO Appointments(patient_ID, doctor_ID, appointment_Date, time, reason, status) VALUES (?, ?, ?, ?, ?, 'Booked')";
	            try (PreparedStatement ps = con.prepareStatement(sql)) {
	                ps.setInt(1, patientId); ps.setInt(2, doctorId); ps.setDate(3, Date.valueOf(date)); ps.setTime(4, Time.valueOf(time)); ps.setString(5, reason);
	                ps.executeUpdate(); System.out.println("Appointment booked successfully.");
	            }
	        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
	    }

	    private static void viewAllAppointments() {
	        String sql = "SELECT a.appointment_ID, p.name as patient_name, d.name as doctor_name, a.appointment_Date, a.time, a.reason, a.status FROM Appointments a JOIN Patients p ON a.patient_ID=p.patient_ID JOIN Doctors d ON a.doctor_ID=d.doctor_ID";
	        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
	            while (rs.next()) {
	                System.out.println(rs.getInt("appointment_ID") + " | Patient: " + rs.getString("patient_name") + " | Doctor: " + rs.getString("doctor_name") + " | Date: " + rs.getDate("appointment_Date") + " " + rs.getTime("time") + " | Status: " + rs.getString("status"));
	            }
	        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
	    }

	    private static void searchAppointmentsByPatient() {
	        try {
	            System.out.print("Enter patient ID: "); int pid = readInt();
	            String sql = "SELECT * FROM Appointments WHERE patient_ID=?";
	            try (PreparedStatement ps = con.prepareStatement(sql)) {
	                ps.setInt(1, pid); ResultSet rs = ps.executeQuery();
	                while (rs.next()) {
						System.out.println("Appt ID: " + rs.getInt("appointment_ID") + " Date: " + rs.getDate("appointment_Date") + " Status: " + rs.getString("status"));
					}
	            }
	        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
	    }

	    private static void searchAppointmentsByDoctor() {
	        try {
	            System.out.print("Enter doctor ID: "); int did = readInt();
	            String sql = "SELECT * FROM Appointments WHERE doctor_ID=?";
	            try (PreparedStatement ps = con.prepareStatement(sql)) {
	                ps.setInt(1, did); ResultSet rs = ps.executeQuery();
	                while (rs.next()) {
						System.out.println("Appt ID: " + rs.getInt("appointment_ID") + " Patient: " + rs.getInt("patient_ID") + " Date: " + rs.getDate("appointment_Date"));
					}
	            }
	        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
	    }

	    private static void updateAppointment() {
	        try {
	            System.out.print("Enter appointment ID: "); int aid = readInt();
	            System.out.print("Enter new date (YYYY-MM-DD): "); String date = sc.nextLine();
	            System.out.print("Enter new time (HH:MM:SS): "); String time = sc.nextLine();
	            System.out.print("Enter new reason: "); String reason = sc.nextLine();
	            System.out.print("Enter new status (Booked/Confirmed/Completed/Cancelled): "); String status = sc.nextLine();
	            String sql = "UPDATE Appointments SET appointment_Date=?, time=?, reason=?, status=? WHERE appointment_ID=?";
	            try (PreparedStatement ps = con.prepareStatement(sql)) {
	                ps.setDate(1, Date.valueOf(date)); ps.setTime(2, Time.valueOf(time)); ps.setString(3, reason); ps.setString(4, status); ps.setInt(5, aid);
	                System.out.println(ps.executeUpdate() > 0 ? "Updated successfully." : "Not found.");
	            }
	        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
	    }

	    private static void cancelAppointment() {
	        try {
	            System.out.print("Enter appointment ID: "); int aid = readInt();
	            String sql = "UPDATE Appointments SET status='Cancelled' WHERE appointment_ID=?";
	            try (PreparedStatement ps = con.prepareStatement(sql)) {
	                ps.setInt(1, aid); System.out.println(ps.executeUpdate() > 0 ? "Cancelled successfully." : "Not found.");
	            }
	        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
	    }

	    private static void viewPatientDetails() {
	        try {
	            System.out.print("Enter patient ID: "); int pid = readInt();
	            String sql = "SELECT * FROM Patients WHERE patient_ID=?";
	            try (PreparedStatement ps = con.prepareStatement(sql)) {
	                ps.setInt(1, pid); ResultSet rs = ps.executeQuery();
	                if (rs.next()) {
						System.out.println("ID: " + rs.getInt("patient_ID") + " Name: " + rs.getString("name") + " Age: " + rs.getInt("age") + " Phone: " + rs.getString("phone"));
					} else {
						System.out.println("Patient not found.");
					}
	            }
	        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
	    }

	    private static int readInt() {
	        while (true) {
	            try { return Integer.parseInt(sc.nextLine()); }
	            catch (NumberFormatException e) { System.out.print("Enter valid number: "); }
	        }
	    }
	}