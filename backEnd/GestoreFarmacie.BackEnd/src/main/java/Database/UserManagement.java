package Database;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.PreparedStatement;


import Model.UserWrapper;
import RestServices.Utility;
import Model.User;
import Model.Pages;
import Model.Role;
public class UserManagement {
	
	public Boolean setFarmacia(int userId, int farmaciaId, Connection conn) {
		try{
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement("UPDATE USER SET Farmacia = (?) WHERE Id = (?)");
			Utility.setStatement(stmt,1, farmaciaId);
			Utility.setStatement(stmt,2, userId);
			stmt.execute();
			return true;
		}catch(Exception e) {
			return false;
		}
		
	}
	
	public ArrayList<User> getUsers(String idFarmacia){
		ArrayList<User> userList = new ArrayList<User>();
		try {
			Connection conn = Connect.getConnection();
			String sql = "Select name, Phone_Number, Role, surname, email, Id, farmacia from user ";
			if(idFarmacia != null) {
				sql +="where farmacia = (?)"; 
			}
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sql);
			if(idFarmacia != null)
				Utility.setStatement(stmt,1, idFarmacia);
			ResultSet rs = stmt.executeQuery();
			String Role=null;
			while (rs.next()) {
				User user = new User();
				user.setName(rs.getString("name"));
				user.setPhone_number(rs.getString("Phone_Number"));
				Role = rs.getString("Role");
				user.setSurname(rs.getString("surname"));
				user.setEmail(rs.getString("email"));
				user.setId(rs.getString("Id"));
				user.setFarmacia(rs.getInt("farmacia"));
				if(Role != null) {
					Role r = new Role();
					r.setId(Role);
					user.setRole(r);
				}
				userList.add(user);
			}
			
			return userList;
		}catch(Exception e ) {
			System.out.println(e);
			return null;
		}
	}
	
	public ArrayList<User> getUsersChat(String idFarmacia, User CurrentUser){
		ArrayList<User> userList = new ArrayList<User>();
		try {
			Connection conn = Connect.getConnection();
			String sql = "Select name, Phone_Number, Role, surname, email, Id, farmacia from user where Id != (?) ";
			if(idFarmacia != null) {
				sql +="and farmacia = (?)";
				if(!"2".equals(CurrentUser.getRole().getId())) {
					sql += " and Role > '1'";
				}
			}
			else {
				sql += "and Role <= '2'";
			}
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sql);
			Utility.setStatement(stmt,1, CurrentUser.getId());
			if(idFarmacia != null)
				Utility.setStatement(stmt,2, idFarmacia);
			ResultSet rs = stmt.executeQuery();
			String Role=null;
			while (rs.next()) {
				User user = new User();
				user.setName(rs.getString("name"));
				user.setPhone_number(rs.getString("Phone_Number"));
				Role = rs.getString("Role");
				user.setSurname(rs.getString("surname"));
				user.setEmail(rs.getString("email"));
				user.setId(rs.getString("Id"));
				user.setFarmacia(rs.getInt("farmacia"));
				if(Role != null) {
					Role r = new Role();
					r.setId(Role);
					user.setRole(r);
				}
				userList.add(user);
			}
			
			return userList;
		}catch(Exception e ) {
			System.out.println(e);
			return null;
		}
	}
	
	public User getUser(String id) {
		User user=null;
		try {
			Connection conn = Connect.getConnection();
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement("Select name, Phone_Number, Role, surname, email, Id, farmacia from user where Id = (?) ");
			Utility.setStatement(stmt,1, id);
			ResultSet rs = stmt.executeQuery();
			String Role=null;
			while (rs.next()) {
				user = new User();
				user.setName(rs.getString("name"));
				user.setPhone_number(rs.getString("Phone_Number"));
				Role = rs.getString("Role");
				user.setSurname(rs.getString("surname"));
				user.setEmail(rs.getString("email"));
				user.setId(rs.getString("Id"));
				user.setFarmacia(rs.getInt("farmacia"));
				if(Role != null) {
					Role r = new Role();
					r.setId(Role);
					user.setRole(r);
				}
			}
			
			return user;
		}catch(Exception e ) {
			System.out.println(e);
			return null;
		}
	}
	
	
	public User getUserFromAccessToken(String value) {
		User u = Utility.getUserFromJWT(value);
		return u;
	}
	public UserWrapper getUser(String email, String password) {
		UserWrapper userWrapper = new UserWrapper();
		User user = new User();
		try {
			Connection conn = Connect.getConnection();
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement("Select name, Phone_Number, Role, surname, email, Id, farmacia from user where email = (?) and Password = (?) ");
			Utility.setStatement(stmt,1, email);
			Utility.setStatement(stmt,2, password);
			ResultSet rs = stmt.executeQuery();
			String Role=null;
			while (rs.next()) {
				user.setName(rs.getString("name"));
				user.setPhone_number(rs.getString("Phone_Number"));
				Role = rs.getString("Role");
				user.setSurname(rs.getString("surname"));
				user.setEmail(rs.getString("email"));
				user.setId(rs.getString("Id"));
				user.setFarmacia(rs.getInt("farmacia"));
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
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement("select Id, Name, icon, Default_Path, ricettaEnabled from role where role.id = (?)");
			Utility.setStatement(stmt,1, RoleId);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				role.setDefault_path(rs.getString("Default_Path"));
				role.setIcon(rs.getString("icon"));
				role.setId(rs.getString("Id"));
				role.setName(rs.getString("Name"));
				role.setRicettaEnabled(rs.getBoolean("ricettaEnabled"));
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
			Utility.setStatement(stmt,1, roleId);
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
	public Integer insertNewUser(User u,String Role,String password) {
		try {
			return insertNewUser(u,Role,password,Connect.getConnection());
		}catch(Exception e) {
			return null;
		}
	}
	public Integer insertNewUser(User u,String Role,String password,Connection conn) {
		try {
			PreparedStatement stmt =(PreparedStatement) conn.prepareStatement("Insert into user(name,surname,email,Phone_Number,Role,Password,Farmacia) values(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			Utility.setStatement(stmt,1, u.getName());
			Utility.setStatement(stmt,2, u.getSurname());			
			Utility.setStatement(stmt,3, u.getEmail());
			Utility.setStatement(stmt,4, u.getPhone_number());
			Utility.setStatement(stmt,5, Role);
			Utility.setStatement(stmt,6, password);
			Utility.setStatement(stmt, 7, u.getFarmacia());
			stmt.execute();
			ResultSet generatedKeys = stmt.getGeneratedKeys();
			generatedKeys.next();
			return generatedKeys.getInt(1);

		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
}
