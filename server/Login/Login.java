package Login;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/Login")
public class Login extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        //Connect to DB
        //Query the user
        
        try{
            PrintWriter out = response.getWriter();
            out.println("Servlet di prova");
        }catch(IOException e){

        }
        
    }
}