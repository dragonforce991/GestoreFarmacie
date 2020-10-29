package Model;

import java.util.ArrayList;
import java.util.Date;

public class ProdottiAcquistatiFull {
	
	
	private String idAcquistiProdotti__r;
	private Integer quantita; 
	private Prodotto Prodotto;
	private String Codice_Regionale_Medico ;
	private Date DataRicetta;

	public ProdottiAcquistatiFull(String idAcquistiProdotti__r, Integer quantita, Prodotto prodotto,
			String codice_Regionale_Medico, Date dataRicetta) {
		super();
		this.idAcquistiProdotti__r = idAcquistiProdotti__r;
		this.quantita = quantita;
		Prodotto = prodotto;
		Codice_Regionale_Medico = codice_Regionale_Medico;
		DataRicetta = dataRicetta;
	}
	public ProdottiAcquistatiFull(String idAcquistiProdotti__r, Integer quantita, Prodotto prodotto) {
		this.idAcquistiProdotti__r = idAcquistiProdotti__r;
		this.quantita = quantita;
		Prodotto = prodotto;
	}
	public ProdottiAcquistatiFull() {}
	
	public String getIdAcquistiProdotti__r() {
		return idAcquistiProdotti__r;
	}
	public void setIdAcquistiProdotti__r(String idAcquistiProdotti__r) {
		this.idAcquistiProdotti__r = idAcquistiProdotti__r;
	}
	public Integer getQuantita() {
		return quantita;
	}
	public void setQuantita(Integer quantita) {
		this.quantita = quantita;
	}
	public Prodotto getProdotti() {
		return Prodotto;
	}
	public void setProdotti(Prodotto prodotti) {
		Prodotto = prodotti;
	}
	public String getCodice_Regionale_Medico() {
		return Codice_Regionale_Medico;
	}

	public void setCodice_Regionale_Medico(String codice_Regionale_Medico) {
		Codice_Regionale_Medico = codice_Regionale_Medico;
	}

	public Date getDataRicetta() {
		return DataRicetta;
	}

	public void setDataRicetta(Date dataRicetta) {
		DataRicetta = dataRicetta;
	}
	
	
}
