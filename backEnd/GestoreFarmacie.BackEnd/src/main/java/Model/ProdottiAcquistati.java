package Model;

import java.util.Date;

public class ProdottiAcquistati {
	private String idAcquistiProdotti__r;
	private Integer quantita;
	/*
	 * Intenzionalmente lasciati come String e non come oggetto, per favorire l'inserimento di record (utilizzato solo come wrapper per db).
	 * Se sarà necessario un ritorno di questo tipo di record, verrà impostato un wrapper comprendente acquisto e list<prodotti>
	 */
	private String IdAcquisto;
	private String IdProdotto;
	private String Codice_Regionale_Medico ;
	private Date DataRicetta;
	public ProdottiAcquistati() {};

	public ProdottiAcquistati(String idAcquistiProdotti__r, Integer quantita, String idAcquisto, String idProdotto,
			String codice_Regionale_Medico, Date dataRicetta) {
		super();
		this.idAcquistiProdotti__r = idAcquistiProdotti__r;
		this.quantita = quantita;
		IdAcquisto = idAcquisto;
		IdProdotto = idProdotto;
		Codice_Regionale_Medico = codice_Regionale_Medico;
		DataRicetta = dataRicetta;
	}



	public ProdottiAcquistati(String idAcquistiProdotti__r, String idAcquisto, String idProdotto, Integer quantita) {
		this.idAcquistiProdotti__r = idAcquistiProdotti__r;
		this.IdAcquisto = idAcquisto;
		this.IdProdotto = idProdotto;
		this.quantita = quantita;
	}
	public String getIdAcquistiProdotti__r() {
		return idAcquistiProdotti__r;
	}
	public void setIdAcquistiProdotti__r(String idAcquistiProdotti__r) {
		this.idAcquistiProdotti__r = idAcquistiProdotti__r;
	}
	public String getIdAcquisto() {
		return IdAcquisto;
	}
	public void setIdAcquisto(String idAcquisto) {
		IdAcquisto = idAcquisto;
	}
	public String getIdProdotto() {
		return IdProdotto;
	}
	public void setIdProdotto(String idProdotto) {
		IdProdotto = idProdotto;
	}
	public Integer getQuantita() {
		return quantita;
	}
	public void setQuantita(Integer quantita) {
		this.quantita = quantita;
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
