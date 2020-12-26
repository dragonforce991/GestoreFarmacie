package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import Model.Magazzino;
import Model.ProdottiAcquistati;
import RestServices.Utility;

public class ProdottiAcquistatiManagement {
	public String insertProdottiAcquistati(ArrayList<ProdottiAcquistati> prodList, String IdFarmacia,String IdAcquisto) {
		try {
			return insertProdottiAcquistati(prodList, Connect.getConnection(),IdFarmacia,IdAcquisto);
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	public String insertProdottiAcquistati(ArrayList<ProdottiAcquistati> prodList,Connection conn, String IdFarmacia,String IdAcquisto) {
		
		try {
			MagazzinoManagement maga = new MagazzinoManagement();
			ArrayList<Magazzino> magazzino = maga.getMagazzino(IdFarmacia);
			HashMap<String,Magazzino> magazzinoMap = new HashMap<String,Magazzino>();
			ArrayList<Magazzino> updateMagazzino = new ArrayList<Magazzino>();
			for(Magazzino m : magazzino) {
				magazzinoMap.put(m.getIdProdotto(), m);
			}
			for(ProdottiAcquistati p : prodList) {
				if(magazzinoMap.containsKey(p.getIdProdotto())) {
					Magazzino m = magazzinoMap.get(p.getIdProdotto());
					m.setQuantita(m.getQuantita() - p.getQuantita());
					if(m.getQuantita() < 0) {
						return "Quantita insufficiente in magazzino per il prodotto " + p.getIdProdotto();
					}
					updateMagazzino.add(m);
				}else {
					return "Prodotto non presente in magazzino "+ p.getIdProdotto();
				}
			}
			
			if(maga.insertMagazzino(updateMagazzino,conn)) {
				String sql = "INSERT INTO acquistiprodotti__r(IdAcquisto,IdProdotto,quantita,DataRicetta,CodiceRegionale)VALUES((?),(?),(?),(?),(?))";
				PreparedStatement stmt = (PreparedStatement)conn.prepareStatement(sql);
				int count = 0;
				for(ProdottiAcquistati pa : prodList) {
					Utility.setStatement(stmt,1, IdAcquisto);
					Utility.setStatement(stmt,2, pa.getIdProdotto());
					Utility.setStatement(stmt,3, pa.getQuantita());
					Utility.setStatement(stmt, 4, pa.getDataRicetta());
					Utility.setStatement(stmt, 5,pa.getCodice_Regionale_Medico());
					stmt.addBatch();
					count++;
					if(count % 100 == 0 || count == prodList.size())
						stmt.executeBatch();
					
				}
				return "OK";
			}else {
				return "Errore modifica magazzino";
			}
			
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	public ArrayList<ProdottiAcquistati> getProdottiAcquistati(String idFarmacia){
		try {
			Connection conn = Connect.getConnection();
			ArrayList<ProdottiAcquistati> retList = new  ArrayList<ProdottiAcquistati>();
			String sql = "SELECT idAcquistiProdotti__r, IdAcquisto, IdProdotto, quantita,DataRicetta, CodiceRegionale FROM gestfarma.acquistiprodotti__r where IdAcquisto in(Select idAcquisti from acquisti where IdFarmacia = (?));";
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sql);
			Utility.setStatement(stmt,1,idFarmacia);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				retList.add(getProdottoAcquistatoFromResultSet(rs));
			}
			return retList;
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
		
	};
	private ProdottiAcquistati getProdottoAcquistatoFromResultSet(ResultSet rs) throws SQLException {
		ProdottiAcquistati pa = new ProdottiAcquistati();
		pa.setIdAcquistiProdotti__r(rs.getString("idAcquistiProdotti__r"));
		pa.setIdAcquisto(rs.getString("IdAcquisto"));
		pa.setIdProdotto(rs.getString("IdProdotto"));
		pa.setQuantita(rs.getInt("quantita"));
		pa.setCodice_Regionale_Medico(rs.getString("CodiceRegionale"));
		pa.setDataRicetta(rs.getDate("DataRicetta"));
		return pa;
		
	}
	public ArrayList<ProdottiAcquistati> getProdottiAcquistati(){
		try {
			Connection conn = Connect.getConnection();
			ArrayList<ProdottiAcquistati> retList = new  ArrayList<ProdottiAcquistati>();
			String sql = "SELECT idAcquistiProdotti__r, IdAcquisto, IdProdotto, quantita,DataRicetta, CodiceRegionale FROM gestfarma.acquistiprodotti__r where IdAcquisto in(Select idAcquisti from acquisti);";
			PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				retList.add(getProdottoAcquistatoFromResultSet(rs));
			}
			return retList;
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	};
	
	
	
	
}
