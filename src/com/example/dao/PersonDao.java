package com.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Person;

public class PersonDao {
	
	public List<Person> getPersonList(String startDate, String endDate){
		Connection conn = null;
		PreparedStatement ps = null;
		List<Person> personList = new ArrayList<Person>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/new_schema", "yourDBAccount", "yourDBPassword");
			String sql =" select * from Person where (Birthday Between ? AND ?) order by Id";
			ps = conn.prepareStatement(sql);
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Person person = new Person();
                person.setId(rs.getInt("Id"));
                person.setName(rs.getString("Name"));
                person.setAge(rs.getString("Age"));
                person.setPhone(rs.getString("Phone"));
                person.setEmail(rs.getString("Email"));
                person.setBirthday(rs.getString("Birthday").substring(0, 10));
                personList.add(person);
            }
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				ps.close();
	            conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
        return personList;
	}
}
