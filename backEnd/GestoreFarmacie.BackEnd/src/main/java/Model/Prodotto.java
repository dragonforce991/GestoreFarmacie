package Model;

import java.util.ArrayList;

public class Prodotto {
	private String idProdotto;
	private Boolean ObbligoRicetta;
	private Float CostoUnitario;
	private String Nome;
	private String Azienda;
	private String Descizione;
	private String Codice;
	private ArrayList<String> ParoleChiave;
	
	public Prodotto() {}
	public Prodotto(String idProdotto, Boolean obbligoRicetta, Float costoUnitario, String nome, String azienda,
			String descizione, String codice, ArrayList<String> paroleChiave) {
		this.idProdotto = idProdotto;
		ObbligoRicetta = obbligoRicetta;
		CostoUnitario = costoUnitario;
		Nome = nome;
		Azienda = azienda;
		Descizione = descizione;
		Codice = codice;
		ParoleChiave = paroleChiave;
	}

	public Prodotto(String idProdotto, Boolean obbligoRicetta, Float costoUnitario, String nome, String azienda,
			String descizione, String codice) {
		this.idProdotto = idProdotto;
		ObbligoRicetta = obbligoRicetta;
		CostoUnitario = costoUnitario;
		Nome = nome;
		Azienda = azienda;
		Descizione = descizione;
		Codice = codice;
	}

	public String getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(String idProdotto) {
		this.idProdotto = idProdotto;
	}

	public Boolean getObbligoRicetta() {
		return ObbligoRicetta;
	}

	public void setObbligoRicetta(Boolean obbligoRicetta) {
		ObbligoRicetta = obbligoRicetta;
	}

	public Float getCostoUnitario() {
		return CostoUnitario;
	}

	public void setCostoUnitario(Float costoUnitario) {
		CostoUnitario = costoUnitario;
	}

	public String getNome() {
		return Nome;
	}

	public void setNome(String nome) {
		Nome = nome;
	}

	public String getAzienda() {
		return Azienda;
	}

	public void setAzienda(String azienda) {
		Azienda = azienda;
	}

	public String getDescizione() {
		return Descizione;
	}

	public void setDescizione(String descizione) {
		Descizione = descizione;
	}

	public String getCodice() {
		return Codice;
	}

	public void setCodice(String codice) {
		Codice = codice;
	}

	public ArrayList<String> getParoleChiave() {
		return ParoleChiave;
	}

	public void setParoleChiave(ArrayList<String> paroleChiave) {
		ParoleChiave = paroleChiave;
	}
	
	
	
	
}
