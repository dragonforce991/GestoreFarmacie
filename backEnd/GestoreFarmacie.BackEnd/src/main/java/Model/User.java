package Model;
import Model.Role;

public class User {
	private String id;
	private String full_name;
	private String name;
	private String surname;
	private String email;
	private String phone_number;
	private Role role;
	private Integer farmacia;
	private String password;
	private String nomeFarmacia;
	
	public User(String id, String full_name, String name, String surname, String email, String phone_number,
			Role role) {
		this.id = id;
		this.full_name = full_name;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phone_number = phone_number;
		this.role = role;
	}
	public User() {
		role = new Role();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public void setFarmacia(Integer farmacia) {
		this.farmacia=farmacia;
	}
	public Integer getFarmacia() {
		return this.farmacia;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNomeFarmacia() {
		return nomeFarmacia;
	}
	public void setNomeFarmacia(String nomeFarmacia) {
		this.nomeFarmacia = nomeFarmacia;
	}
	
	
}

