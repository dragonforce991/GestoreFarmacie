package Database;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.mysql.jdbc.PreparedStatement;

import Model.Farmacia;

public class FarmaciaManagement {
	public Boolean insertFarmacia(Farmacia f) {
		try {
			Connection conn = Connect.getConnection();
			return insertFarmacia(f,conn);
		}catch(Exception e) {
			return false;
		}
	}
	
	public Boolean insertFarmacia(Farmacia f, Connection conn) {
		try {
			
			PreparedStatement stmt =(PreparedStatement) conn.prepareStatement("Insert into farmacia(Nome,Indirizzo,Telefono,Titolare) Values(?,?,?,?)");
			
			stmt.setString(1, f.getNome());
			stmt.setString(2, f.getIndirizzo());
			stmt.setString(3, f.getTelefono());
			stmt.setString(4, f.getTitolare());
			
			stmt.execute();
			return true;
		}catch(Exception e) {
			System.out.println(e);
			return false;
		}
		
	}
}
