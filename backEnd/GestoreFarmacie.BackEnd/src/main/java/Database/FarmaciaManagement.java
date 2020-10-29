package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import Model.Farmacia;
import RestServices.Utility;

import java.sql.PreparedStatement;

public class FarmaciaManagement {

	public ArrayList<Farmacia> getFarmacie(){
		try {
			ArrayList<Farmacia> retList = new ArrayList<Farmacia>();
			Connection conn = Connect.getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("Select Id, Nome, Indirizzo, Telefono, Titolare from Farmacia");
			while(rs.next())
				retList.add(getFarmaciaFromResultSet(rs));
			return retList;
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	private Farmacia getFarmaciaFromResultSet(ResultSet rs) {
		try {
			Farmacia f = new Farmacia();
			f.setId(rs.getString("Id"));
			f.setIndirizzo(rs.getString("Nome"));
			f.setNome(rs.getString("Indirizzo"));
			f.setTelefono(rs.getString("Telefono"));
			f.setTitolare(rs.getString("Titolare"));
			return f;
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	public Integer insertFarmacia(Farmacia f) {
		try {
			Connection conn = Connect.getConnection();
			return insertFarmacia(f, conn);
		} catch (Exception e) {
			return null;
		}
	}

	public Integer insertFarmacia(Farmacia f, Connection conn) {
		try {

			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(
					"Insert into farmacia(Nome,Indirizzo,Telefono,Titolare) Values(?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			Utility.setStatement(stmt, 1, f.getNome());
			Utility.setStatement(stmt, 2, f.getIndirizzo());
			Utility.setStatement(stmt, 3, f.getTelefono());
			Utility.setStatement(stmt, 4, f.getTitolare());

			stmt.execute();
			ResultSet generatedKey = stmt.getGeneratedKeys();
			generatedKey.next();
			return generatedKey.getInt(1);

		} catch (Exception e) {
			System.out.println(e);
			return null;
		}

	}
}
