package Login;

public class ResponseWrapper {
    private boolean Error;
    private String Profile;
    private String ErrorMessage;
    
    
    public void setError(boolean Error){
        this.Error = Error;
    }
    public void setProfile(String Profile){
        this.Profile = Profile;
    }
    public void setErrorMessage(String ErrorMessage){
        this.ErrorMessage = ErrorMessage;
    }

    public String toJson(){
        return "";
    }
}