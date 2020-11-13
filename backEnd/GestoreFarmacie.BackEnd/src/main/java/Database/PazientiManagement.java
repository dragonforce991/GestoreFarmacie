package Database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import Model.Paziente;
import RestServices.Utility;

public class PazientiManagement {

	public ArrayList<Paziente> getPaziente(Paziente p) {
		ArrayList<Paziente> pList = new ArrayList<Paziente>();
		if(p.getCodice_Fiscale() != null) {
			Paziente p1 = getPazienteByCF(p);
			if(p1!=null)
				pList.add(p1);
		}else if(p.getCognome() != null || p.getNome()!= null || p.getDataDiNascita() != null) {
			pList=getPazienteByGen(p);
		}
		return pList;
	}
	public Integer insertPaziente(Paziente p, Connection conn) {
		try {
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement("INSERT INTO pazienti(Codice_Fiscale,Nome,Cognome,DataDiNascita,OperatoreRegistrazione,Telefono)VALUES((?),(?),(?),(?),(?),(?))",Statement.RETURN_GENERATED_KEYS);
			Utility.setStatement(stmt,1,p.getCodice_Fiscale());
			Utility.setStatement(stmt,2, p.getNome());
			Utility.setStatement(stmt,3, p.getCognome());
			Utility.setStatement(stmt,4, p.getDataDiNascita());
			Utility.setStatement(stmt,5,p.getOperatoreRegistrazione().getId());
			Utility.setStatement(stmt,6,p.getTelefono());
			System.out.println(p.getDataDiNascita());
			System.out.println(stmt.toString());
			stmt.execute();
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			return rs.getInt(1);
		}catch(Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			System.out.println(e.getStackTrace());
			
			return null;
			}
	}
	public Integer insertPaziente(Paziente p) {
		try {
			Connection conn = Connect.getConnection();
			return insertPaziente(p,conn);
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	public ArrayList<Paziente> getPazienteByGen(Paziente p){
		try{
			Connection conn = Connect.getConnection();
			ArrayList<Paziente> returnList = new ArrayList<Paziente>();
			Boolean needAnd = false;
			int cognomeIndex =0;
			int nomeIndex = 0;
			int dataIndex = 0;
			int index = 0;
			String sqlClause = "SELECT idPazienti,Codice_Fiscale,Nome,Cognome,DataDiNascita,OperatoreRegistrazione,Telefono FROM pazienti WHERE";
			if(p.getCognome() != null) {
				needAnd = true;
				cognomeIndex = ++index; 
				sqlClause += " Cognome = (?) "; 
			}
			if(p.getNome() != null) {
				sqlClause += needAnd ? " AND Nome = (?) " : " Nome = (?) ";
				needAnd = true;
				nomeIndex = ++index;
			}
			if(p.getDataDiNascita() != null) {
				dataIndex = ++index;
				needAnd = true;
				sqlClause += needAnd ? " AND DataDiNascita = (?) " : " DataDiNascita = (?) ";
			}
			if(!needAnd) {
				return null;
			}
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sqlClause);
			if(cognomeIndex != 0) {
				Utility.setStatement(stmt,cognomeIndex, p.getCognome());
			}
			if(nomeIndex != 0) {
				Utility.setStatement(stmt,nomeIndex,p.getNome());
			}
			if(dataIndex != 0) {
				Utility.setStatement(stmt,dataIndex,p.getDataDiNascita());
			}
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				returnList.add(getPazienteFromResultSet(rs));
			}
			return returnList;
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	private Paziente getPazienteFromResultSet(ResultSet rs) throws SQLException {
		Paziente p = new Paziente();
		//DateFormat df = DateFormat.getDateInstance();
		p.setCodice_Fiscale(rs.getString("Codice_Fiscale"));
		Date d = rs.getDate("DataDiNascita");
		if(d != null) {
			p.setDataDiNascita(d.toLocalDate());
		}
		
		p.setCognome(rs.getString("Cognome"));
		p.setNome(rs.getString("Nome"));
		p.setIdPazienti(rs.getString("idPazienti"));
		p.setTelefono(rs.getString("Telefono"));
		return p;
	}
	public Paziente getPazienteByCF(Paziente p) {
		try{
			Connection conn = Connect.getConnection();
			String sqlClause = "SELECT idPazienti,Codice_Fiscale,Nome,Cognome,DataDiNascita,OperatoreRegistrazione,Telefono FROM pazienti WHERE Codice_Fiscale = (?)";
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sqlClause);
			Utility.setStatement(stmt,1,p.getCodice_Fiscale());
			stmt.execute();
			ResultSet rs = stmt.getResultSet();

			rs.next();
			return getPazienteFromResultSet(rs);
			
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	public ArrayList<Paziente> getPazienti(){
		
		try{
			ArrayList<Paziente> retList = new ArrayList<Paziente>();
			Connection conn = Connect.getConnection();
			String sqlClause = "SELECT idPazienti, Codice_Fiscale, Nome, Cognome, DataDiNascita, OperatoreRegistrazione, Telefono FROM pazienti ";
			Statement st = conn.createStatement();
			
			if(st.execute(sqlClause)) {
				ResultSet rs = st.getResultSet();
				while(rs.next()) {
					retList.add(getPazienteFromResultSet(rs));
				}
			}
			
			return retList;
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	
	
	
}
