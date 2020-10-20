package Model;
import Model.User;
import RestServices.Utility;
import com.google.gson.Gson;
public class UserWrapper {
	private String accessToken;
	private User user;
	
	public UserWrapper() {
		this.user = new User();
		this.accessToken= null;
	}
	
	public UserWrapper(User user, String accessToken) {
		this.user = user;
		this.accessToken=accessToken;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getAccessToken() {
		Gson g = new Gson();
		this.accessToken =  Utility.createJWT("1", this.user.getFull_name(), g.toJson(this.user), 5000000);
		return this.accessToken;
		
	}
	public void setAccessToken() {
		if(this.accessToken == null)
			this.accessToken = getAccessToken();
	}
	
	
}
