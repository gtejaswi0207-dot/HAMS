package H_01;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class HAMS_AWT extends Frame implements ActionListener {
    Connection con;
    TextArea out;
    Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12;

    public HAMS_AWT() {
        super("HAMS - Hospital Management System");
        setSize(950, 600);
        setLayout(new BorderLayout(10, 10));

        Label title = new Label("HOSPITAL APPOINTMENT MANAGEMENT SYSTEM (AWT)", Label.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBackground(new Color(0, 102, 102));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        Panel left = new Panel(new GridLayout(12, 1, 5, 5));
        b1 = new Button("1. Register Patient");
        b2 = new Button("2. View Patients");
        b3 = new Button("3. Add Doctor");
        b4 = new Button("4. View Doctors");
        b5 = new Button("5. Book Appointment");
        b6 = new Button("6. View Appointments");
        b7 = new Button("7. Search by Patient");
        b8 = new Button("8. Search by Doctor");
        b9 = new Button("9. Update Appointment");
        b10 = new Button("10. Cancel Appointment");
        b11 = new Button("11. Patient Details");
        b12 = new Button("12. EXIT");

        b1.addActionListener(this); b2.addActionListener(this); b3.addActionListener(this);
        b4.addActionListener(this); b5.addActionListener(this); b6.addActionListener(this);
        b7.addActionListener(this); b8.addActionListener(this); b9.addActionListener(this);
        b10.addActionListener(this); b11.addActionListener(this); b12.addActionListener(this);

        left.add(b1); left.add(b2); left.add(b3); left.add(b4); left.add(b5); left.add(b6);
        left.add(b7); left.add(b8); left.add(b9); left.add(b10); left.add(b11); left.add(b12);
        add(left, BorderLayout.WEST);

        out = new TextArea("Welcome to HAMS AWT\nDB Connecting...\n", 0, 0, TextArea.SCROLLBARS_BOTH);
        out.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(out, BorderLayout.CENTER);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/HAMS?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", "root", "root");
            out.append("DB Connected Successfully!\n");
        } catch (Exception e) {
            out.append("DB Error: " + e.getMessage() + "\n");
        }

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setVisible(true);
    }

    private String ask(String title, String labelText) {
        final Dialog d = new Dialog(this, title, true);
        d.setLayout(new FlowLayout());
        d.setSize(400, 130);
        d.add(new Label(labelText));
        final TextField tf = new TextField(20);
        d.add(tf);
        Button ok = new Button("OK");
        d.add(ok);
        final String[] value = new String[1];
        value[0] = "";
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                value[0] = tf.getText();
                d.setVisible(false);
                d.dispose();
            }
        });
        d.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                d.dispose();
            }
        });
        d.setLocation(400, 300);
        d.setVisible(true);
        return value[0];
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            if (ae.getSource() == b1) {
                String name = ask("Register Patient", "Name:");
                String age = ask("Register Patient", "Age:");
                String gender = ask("Register Patient", "Gender M/F:");
                String phone = ask("Register Patient", "Phone:");
                if(name.equals("")) return;
                PreparedStatement ps = con.prepareStatement("INSERT INTO Patients(name,age,gender,phone) VALUES(?,?,?,?)");
                ps.setString(1, name); ps.setInt(2, Integer.parseInt(age)); ps.setString(3, gender); ps.setString(4, phone);
                ps.executeUpdate(); out.append("Patient Added: " + name + "\n");
            } else if (ae.getSource() == b2) {
                Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM Patients");
                StringBuilder sb = new StringBuilder("--- ALL PATIENTS ---\nID | Name | Age | Gender | Phone\n-----------------------------------\n");
                while (rs.next()) { sb.append(rs.getInt(1)).append(" | ").append(rs.getString(2)).append(" | ").append(rs.getInt(3)).append(" | ").append(rs.getString(4)).append(" | ").append(rs.getString(5)).append("\n"); }
                out.setText(sb.toString());
            } else if (ae.getSource() == b3) {
                String name = ask("Add Doctor", "Doctor Name:");
                String spec = ask("Add Doctor", "Specialization (Cardiology/Neurology/Ortho/Pediatrics):");
                String phone = ask("Add Doctor", "Phone:");
                if(name.equals("")) return;
                PreparedStatement ps = con.prepareStatement("INSERT INTO Doctors(name,specialization,phone) VALUES(?,?,?)");
                ps.setString(1, name); ps.setString(2, spec); ps.setString(3, phone); ps.executeUpdate(); out.append("Doctor Added: " + name + " - " + spec + "\n");
            } else if (ae.getSource() == b4) {
                Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM Doctors");
                StringBuilder sb = new StringBuilder("--- ALL DOCTORS ---\nID | Name | Specialization | Phone\n---------------------------------------------\n");
                while (rs.next()) { sb.append(rs.getInt(1)).append(" | ").append(rs.getString(2)).append(" | ").append(rs.getString(3)).append(" | ").append(rs.getString(4)).append("\n"); }
                out.setText(sb.toString());
            } else if (ae.getSource() == b5) {
                String pid = ask("Book Appointment", "Patient ID:"); String did = ask("Book Appointment", "Doctor ID:");
                String date = ask("Book Appointment", "Date YYYY-MM-DD:"); String time = ask("Book Appointment", "Time HH:MM:SS:");
                String reason = ask("Book Appointment", "Reason:"); if(pid.equals("")) return;
                PreparedStatement ps = con.prepareStatement("INSERT INTO Appointments(patient_ID, doctor_ID, appointment_Date, `time`, reason, status) VALUES(?,?,?,?,?,'Booked')");
                ps.setInt(1, Integer.parseInt(pid)); ps.setInt(2, Integer.parseInt(did)); ps.setDate(3, Date.valueOf(date)); ps.setTime(4, Time.valueOf(time)); ps.setString(5, reason); ps.executeUpdate(); out.append("Appointment Booked!\n");
            } else if (ae.getSource() == b6) {
                Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT a.appointment_ID, p.name, d.name, a.appointment_Date, a.`time`, a.status FROM Appointments a JOIN Patients p ON a.patient_ID=p.patient_ID JOIN Doctors d ON a.doctor_ID=d.doctor_ID");
                StringBuilder sb = new StringBuilder("--- ALL APPOINTMENTS ---\n"); while (rs.next()) { sb.append("ID:").append(rs.getInt(1)).append(" | Pat:").append(rs.getString(2)).append(" | Doc:").append(rs.getString(3)).append(" | ").append(rs.getDate(4)).append(" ").append(rs.getTime(5)).append(" | ").append(rs.getString(6)).append("\n"); }
                out.setText(sb.toString());
            } else if (ae.getSource() == b7) {
                String id = ask("Search", "Patient ID:"); if(id.equals("")) return; PreparedStatement ps = con.prepareStatement("SELECT * FROM Appointments WHERE patient_ID=?"); ps.setInt(1, Integer.parseInt(id)); ResultSet rs = ps.executeQuery();
                StringBuilder sb = new StringBuilder("Appointments for Patient "+id+":\n"); while (rs.next()) { sb.append("ApptID:").append(rs.getInt("appointment_ID")).append(" Date:").append(rs.getDate("appointment_Date")).append(" Time:").append(rs.getTime("time")).append(" Status:").append(rs.getString("status")).append("\n"); } out.setText(sb.toString());
            } else if (ae.getSource() == b8) {
                String id = ask("Search", "Doctor ID:"); if(id.equals("")) return; PreparedStatement ps = con.prepareStatement("SELECT * FROM Appointments WHERE doctor_ID=?"); ps.setInt(1, Integer.parseInt(id)); ResultSet rs = ps.executeQuery();
                StringBuilder sb = new StringBuilder("Appointments for Doctor "+id+":\n"); while (rs.next()) { sb.append("ApptID:").append(rs.getInt("appointment_ID")).append(" Patient:").append(rs.getInt("patient_ID")).append(" Date:").append(rs.getDate("appointment_Date")).append("\n"); } out.setText(sb.toString());
            } else if (ae.getSource() == b9) {
                String aid = ask("Update", "Appointment ID:"); String date = ask("Update", "New Date YYYY-MM-DD:"); String time = ask("Update", "New Time HH:MM:SS:"); String reason = ask("Update", "New Reason:"); String status = ask("Update", "Status (Booked/Confirmed/Completed/Cancelled):"); if(aid.equals("")) return;
                PreparedStatement ps = con.prepareStatement("UPDATE Appointments SET appointment_Date=?, `time`=?, reason=?, status=? WHERE appointment_ID=?");
                ps.setDate(1, Date.valueOf(date)); ps.setTime(2, Time.valueOf(time)); ps.setString(3, reason); ps.setString(4, status); ps.setInt(5, Integer.parseInt(aid)); int r = ps.executeUpdate(); out.setText(r>0?"Updated Successfully":"ID Not Found");
            } else if (ae.getSource() == b10) {
                String aid = ask("Cancel", "Appointment ID to Cancel:"); if(aid.equals("")) return; PreparedStatement ps = con.prepareStatement("UPDATE Appointments SET status='Cancelled' WHERE appointment_ID=?"); ps.setInt(1, Integer.parseInt(aid)); int r = ps.executeUpdate(); out.setText(r>0?"Cancelled Successfully":"ID Not Found");
            } else if (ae.getSource() == b11) {
                String pid = ask("Patient Details", "Patient ID:"); if(pid.equals("")) return; PreparedStatement ps = con.prepareStatement("SELECT * FROM Patients WHERE patient_ID=?"); ps.setInt(1, Integer.parseInt(pid)); ResultSet rs = ps.executeQuery(); if (rs.next()) { out.setText("ID: " + rs.getInt(1) + "\nName: " + rs.getString(2) + "\nAge: " + rs.getInt(3) + "\nGender: " + rs.getString(4) + "\nPhone: " + rs.getString(5)); } else { out.setText("Patient Not Found"); }
            } else if (ae.getSource() == b12) { System.exit(0); }
        } catch (Exception ex) { out.setText("Error: " + ex.getMessage()); }
    }

    public static void main(String[] args) {
        new HAMS_AWT();
    }
}
