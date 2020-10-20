package Database;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.mysql.jdbc.PreparedStatement;


import Model.UserWrapper;
import RestServices.Utility;
import Model.User;
import Model.Pages;
import Model.Role;
public class UserManagement {
	public User getUserFromAccessToken(String value) {
		User u = Utility.getUserFromJWT(value);
		return u;
	}
	public UserWrapper getUser(String email, String password) {
		UserWrapper userWrapper = new UserWrapper();
		User user = new User();
		try {
			Connection conn = Connect.getConnection();
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement("Select name, Phone_Number, Role, surname, email, Id from user where email = (?) and Password = (?) ");
			stmt.setString(1, email);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			String Role=null;
			while (rs.next()) {
				user.setName(rs.getString("name"));
				user.setPhone_number(rs.getString("Phone_Number"));
				Role = rs.getString("Role");
				user.setSurname(rs.getString("surname"));
				user.setEmail(rs.getString("email"));
				user.setId(rs.getString("Id"));
			}
			if(Role != null)
				user.setRole(getRole(Role));
		}catch(Exception e ) {
			System.out.println(e);
		}
		userWrapper.setUser(user);
		return userWrapper;
	}
	
	public Role getRole(String RoleId) {
		Role role = new Role();
		try {
		
			Connection conn = Connect.getConnection();
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement("select Id, Name, icon, Default_Path from role where role.id = (?)");
			stmt.setString(1, RoleId);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				role.setDefault_path(rs.getString("Default_Path"));
				role.setIcon(rs.getString("icon"));
				role.setId(rs.getString("Id"));
				role.setName(rs.getString("Name"));
				role.setPages(getPages(RoleId));
			}
		}catch(Exception e) {
			System.out.println(e);
		}
		return role;
		
	}
	
	public ArrayList<Pages> getPages(String roleId){
		ArrayList<Pages> pages = new ArrayList<Pages>();
		try {
			Connection conn = Connect.getConnection();
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement("select pages.Id, Name, Title, Url, Icon, canModify, canDelete, canCreate from pages inner join pagesrole__r on pages.Id = pagesrole__r.IdPage where pagesrole__r.IdRole=(?)");
			stmt.setString(1, roleId);
			ResultSet rs = stmt.executeQuery();
			
			
			while(rs.next()) {
				Pages page = new Pages();
				page.setIcon(rs.getString("Icon"));
				page.setCreate(rs.getBoolean("canCreate"));
				page.setDelete(rs.getBoolean("canDelete"));
				page.setId(rs.getString("Id"));
				page.setModify(rs.getBoolean("canModify"));
				page.setName(rs.getString("Name"));
				page.setTitle(rs.getString("Title"));
				page.setUrl(rs.getString("Url"));
				pages.add(page);
			}
		}catch (Exception e) {
			System.out.println(e);
		}
		return pages;
		
	}
	public Boolean insertNewUser(User u,String Role,String password) {
		try {
			return insertNewUser(u,Role,password,Connect.getConnection());
		}catch(Exception e) {
			return false;
		}
	}
	public Boolean insertNewUser(User u,String Role,String password,Connection conn) {
		try {
			PreparedStatement stmt =(PreparedStatement) conn.prepareStatement("Insert into user(name,surname,email,Phone_Number,Role,Password,Farmacia) values(?,?,?,?,?,?,?)");
			
			stmt.setString(1, u.getName());
			stmt.setString(2, u.getSurname());
			stmt.setString(3, u.getEmail());
			stmt.setString(4, u.getPhone_number());
			stmt.setString(5, Role);
			stmt.setString(6, password);
			stmt.setInt(7, u.getFarmacia());
			
			stmt.execute();
			return true;
		}catch(Exception e) {
			System.out.println(e);
			return false;
		}
	}
}
