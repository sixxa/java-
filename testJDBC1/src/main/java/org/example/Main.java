package org.example;

import java.sql.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        StudentDAO dao = new StudentDAO();
        dao.connect();
        Student s1 = dao.getStudent(3);
        Student s2 = new Student();
        s2.userName = "merabi";
        dao.addStudent(s2);
        System.out.println(s1.userName);

    }
}
 class StudentDAO {
    Connection con = null;
    public void connect() {
       try {
           Class.forName("com.mysql.cj.jdbc.Driver");
           con = DriverManager.getConnection("jdbc:mysql://localhost:3306/aliens", "root", "Atomhtml132!");
       } catch (Exception ex) {
           System.out.println(ex.getMessage());
       }
    }

    public Student getStudent(int userID) {
        Student s = new Student();
        s.userID = userID;
        try {
            String query = "select userName from student where userID ="+userID;
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            rs.next();
            String name = rs.getString("userName");
            s.userName = name;
            return s;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    public void addStudent( Student s) {
        String query = "insert into student (userName) values (?)";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, s.userName);
            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
 }
 class Student {
    int userID;
    String userName;
 }