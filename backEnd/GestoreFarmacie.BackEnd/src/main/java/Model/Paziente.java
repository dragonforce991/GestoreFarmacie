package Model;

import java.time.LocalDate;




public class Paziente {
	private String idPazienti;
	private String Codice_Fiscale;
	private String Nome;
	private String Cognome;
	private LocalDate DataDiNascita;
	private User OperatoreRegistrazione;
	private String Telefono;
	
	
	public Paziente(String idPazienti, String codice_Fiscale, String nome, String cognome, LocalDate dataDiNascita,
			User operatoreRegistrazione, String telefono) {
		this.idPazienti = idPazienti;
		Codice_Fiscale = codice_Fiscale;
		Nome = nome;
		Cognome = cognome;
		DataDiNascita = dataDiNascita;
		OperatoreRegistrazione = operatoreRegistrazione;
		Telefono = telefono;
	}
	public Paziente() {}
	public String getIdPazienti() {
		return idPazienti;
	}
	public void setIdPazienti(String idPazienti) {
		this.idPazienti = idPazienti;
	}
	public String getCodice_Fiscale() {
		return Codice_Fiscale;
	}
	public void setCodice_Fiscale(String codice_Fiscale) {
		Codice_Fiscale = codice_Fiscale;
	}
	public String getNome() {
		return Nome;
	}
	public void setNome(String nome) {
		Nome = nome;
	}
	public String getCognome() {
		return Cognome;
	}
	public void setCognome(String cognome) {
		Cognome = cognome;
	}
	public LocalDate getDataDiNascita() {
		return DataDiNascita;
	}
	public void setDataDiNascita(LocalDate dataDiNascita) {
		DataDiNascita = dataDiNascita;
	}
	public User getOperatoreRegistrazione() {
		return OperatoreRegistrazione;
	}
	public void setOperatoreRegistrazione(User operatoreRegistrazione) {
		OperatoreRegistrazione = operatoreRegistrazione;
	}
	public String getTelefono() {
		return Telefono;
	}
	public void setTelefono(String telefono) {
		Telefono = telefono;
	}
	
}
