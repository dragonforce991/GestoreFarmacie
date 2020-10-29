package Model;

public class Farmacia {
	

	private String Id;
	private String Nome;
	private String Indirizzo;
	private String Telefono;
	private String Titolare;
	
	public Farmacia(String id, String nome, String indirizzo, String telefono, String titolare) {
		this.Id = id;
		this.Nome = nome;
		this.Indirizzo = indirizzo;
		this.Telefono = telefono;
		this.Titolare = titolare;
	}
	public Farmacia() {	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getNome() {
		return Nome;
	}
	public void setNome(String nome) {
		Nome = nome;
	}
	public String getIndirizzo() {
		return Indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		Indirizzo = indirizzo;
	}
	public String getTelefono() {
		return Telefono;
	}
	public void setTelefono(String telefono) {
		Telefono = telefono;
	}
	public String getTitolare() {
		return Titolare;
	}
	public void setTitolare(String titolare) {
		Titolare = titolare;
	}
	
}
