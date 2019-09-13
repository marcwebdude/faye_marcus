package com.faye_marcus.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.sql.*;

import org.springframework.stereotype.Service;

@Service
public class CoursesHardcodedService {

	private static List<Course> courses = new ArrayList<>();
	private static long idCounter = 0;
	String username;
	String description;
	int id;


	static{courses.add(new Course(++idCounter,"in28minutes","Learn Full stack with Spring Boot and Angular"));courses.add(new Course(++idCounter,"in28minutes","Learn Full stack with Spring Boot and React"));courses.add(new Course(++idCounter,"in28minutes","Master Microservices with Spring Boot and Spring Cloud"));courses.add(new Course(++idCounter,"in28minutes","Deploy Spring Boot Microservices to Cloud with Docker and Kubernetes"));}

	public List<Course> findAll() {
		return courses;
	}

	public Course save(Course course) {
		if (course.getId() == -1 || course.getId() == 0) {
			course.setId(++idCounter);
	        String sql = "INSERT INTO course(username,description) VALUES(" + username + "," + description +")";
	        
	        try (Connection conn = this.connect();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setString(1, username);
	            pstmt.setString(2, description);
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
			courses.add(course);
			
		} else {
	        String sql = "UPDATE course SET username = " + username + " , "
	                + "description = " + description + " "
	                + "WHERE id = " + id;
	 
	        try (Connection conn = this.connect();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	 
	            // set the corresponding param
	            pstmt.setString(1, username);
	            pstmt.setString(2, description);
	            pstmt.setInt(3, id);
	            // update 
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	        
			deleteById(course.getId());
			courses.add(course);
		}
		return course;
	}

	public Course deleteById(long id) {
		Course course = findById(id);

		if (course == null)
			return null;

		if (courses.remove(course)) {
	        String sql = "DELETE FROM course WHERE id = " + id;
	        
	        try (Connection conn = this.connect();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	 
	            // set the corresponding param
	            pstmt.setFloat(1, id);
	            // execute the delete statement
	            pstmt.executeUpdate();
	 
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
			return course;
		}

		return null;
	}

	public Course findById(long id) {
		for (Course course : courses) {
			if (course.getId() == id) {
		        String sql = "SELECT * FROM course WHERE id = " + id;
		        
		        try (Connection conn = this.connect();
		             Statement stmt  = conn.createStatement();
		             ResultSet rs    = stmt.executeQuery(sql)){
		            
		            // loop through the result set
		            while (rs.next()) {
		                System.out.println(rs.getInt("id") +  "\t" + 
		                                   rs.getString("username") + "\t" +
		                                   rs.getString("description"));
		            }
		        } catch (SQLException e) {
		            System.out.println(e.getMessage());
		        }
				return course;
			}
		}

		return null;
	}
	
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:data.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
 
}