import java.sql.*;


public class dbmsmain {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/Students";
        String user = "postgres";
        String password = "Abdulakeem1.";

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            System.out.println("All Students");


            getAllStudents(connection);

            addStudent(connection, 4, "Bola", "Elegbede", "bola@gmail.com", "2023-09-04");

            updateStudentEmail(connection, 3, "jimjim@example.com");

            deleteStudent(connection, 4);


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void getAllStudents(Connection connection) throws SQLException {
        //CREATE STATEMENT
        Statement statement = connection.createStatement();
        statement.executeQuery("SELECT * FROM students");  //Query to retrieve all students from the table
        ResultSet resultset = statement.getResultSet();
        while (resultset.next()) {
            System.out.print(resultset.getInt("student_id") + " \t"); //I added a student_id column when I created the Database a while ago
            System.out.print(resultset.getString("first_name") + " \t");
            System.out.print(resultset.getString("last_name") + " \t");
            System.out.print(resultset.getString("email") + " \t");
            System.out.println(resultset.getString("enrollment_date"));
        }

    }

    public static void addStudent(Connection connection, Integer student_id, String first_name, String last_name, String email, String enrollmentDate) {
        String insertsql = "INSERT INTO students (student_id, first_name, last_name, email, enrollment_date) VALUES ( ?, ?, ?, ?, ?)"; //easier way to set the values
        try (PreparedStatement pstmt = connection.prepareStatement(insertsql)) {
            pstmt.setInt(1, student_id);
            pstmt.setString(2, first_name);
            pstmt.setString(3, last_name);
            pstmt.setString(4, email);
            pstmt.setDate(5, Date.valueOf(enrollmentDate));
            pstmt.executeUpdate();
            System.out.println("Data inserted using PreparedStatement");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateStudentEmail(Connection connection, int studentId, String newEmail) throws SQLException {
        String updateQuery = "UPDATE students SET email = ? WHERE student_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
            pstmt.setString(1, newEmail);
            pstmt.setInt(2, studentId);
            int rowsUpdated = pstmt.executeUpdate(); //to confirm row was updated without having to use getAllStudents
            System.out.println(rowsUpdated + " student(s) updated.");
        }

    }

    public static void deleteStudent(Connection connection, int studentId) throws SQLException {
        String query = "DELETE FROM students WHERE student_id = ?"; //query to delete student with student id as the parameter
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println(rowsDeleted + " student(s) deleted."); //Used this to confirm that the row was deleted
        }
    }

}
