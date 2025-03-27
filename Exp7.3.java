import java.sql.*;
import java.util.Scanner;

// Model: Student Class
class Student {
    int studentID;
    String name;
    String department;
    double marks;

    public Student(int studentID, String name, String department, double marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }
}

// DAO: Database Operations
class StudentDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/StudentDB";
    private static final String USER = "root";
    private static final String PASSWORD = "password";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void addStudent(Student student) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Student VALUES (?, ?, ?, ?)");) {
            stmt.setInt(1, student.studentID);
            stmt.setString(2, student.name);
            stmt.setString(3, student.department);
            stmt.setDouble(4, student.marks);
            stmt.executeUpdate();
            System.out.println("Student added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewStudents() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Student");) {
            while (rs.next()) {
                System.out.println(rs.getInt("StudentID") + " | " + rs.getString("Name") + " | " + rs.getString("Department") + " | " + rs.getDouble("Marks"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStudent(int studentID, String name, String department, double marks) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE Student SET Name=?, Department=?, Marks=? WHERE StudentID=?");) {
            stmt.setString(1, name);
            stmt.setString(2, department);
            stmt.setDouble(3, marks);
            stmt.setInt(4, studentID);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "Student updated successfully!" : "Student not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(int studentID) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Student WHERE StudentID=?");) {
            stmt.setInt(1, studentID);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "Student deleted successfully!" : "Student not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// Controller: Student Management System
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentDAO studentDAO = new StudentDAO();
        int choice;

        do {
            System.out.println("\nStudent Management System");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter StudentID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Department: ");
                    String dept = scanner.nextLine();
                    System.out.print("Enter Marks: ");
                    double marks = scanner.nextDouble();
                    studentDAO.addStudent(new Student(id, name, dept, marks));
                    break;
                case 2:
                    studentDAO.viewStudents();
                    break;
                case 3:
                    System.out.print("Enter StudentID to update: ");
                    int updateID = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter New Name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter New Department: ");
                    String newDept = scanner.nextLine();
                    System.out.print("Enter New Marks: ");
                    double newMarks = scanner.nextDouble();
                    studentDAO.updateStudent(updateID, newName, newDept, newMarks);
                    break;
                case 4:
                    System.out.print("Enter StudentID to delete: ");
                    int deleteID = scanner.nextInt();
                    studentDAO.deleteStudent(deleteID);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 5);

        scanner.close();
    }
}
