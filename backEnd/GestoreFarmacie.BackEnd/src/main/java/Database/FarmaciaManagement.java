package Database;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import Model.Farmacia;
import java.sql.PreparedStatement;
public class FarmaciaManagement {
	public Integer insertFarmacia(Farmacia f) {
		try {
			Connection conn = Connect.getConnection();
			return insertFarmacia(f,conn);
		}catch(Exception e) {
			return null;
		}
	}
	
	public Integer insertFarmacia(Farmacia f, Connection conn) {
		try {
			
			PreparedStatement stmt =(PreparedStatement) conn.prepareStatement("Insert into farmacia(Nome,Indirizzo,Telefono,Titolare) Values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			
			stmt.setString(1, f.getNome());
			stmt.setString(2, f.getIndirizzo());
			stmt.setString(3, f.getTelefono());
			stmt.setString(4, f.getTitolare());
			
			stmt.execute();
			ResultSet generatedKey = stmt.getGeneratedKeys();
			generatedKey.next();
			return generatedKey.getInt(1);
			
			
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
		
	}
}
