package Model;

import java.util.ArrayList;
import java.util.Date;

public class Acquisto {
	private String acquisto;
	private Paziente paziente;
	private User user;
	private float Totale;
	private Date DataAcquisto;
	private ArrayList<ProdottiAcquistatiFull> prodotti;
	private String idFarmacia;
	public Acquisto(String acquisto, Paziente paziente, User user,float totale, Date dataAcquisto) {
		this.acquisto = acquisto;
		this.paziente = paziente;
		this.user = user;

		Totale = totale;
		DataAcquisto = dataAcquisto;
	}
	public Acquisto(String acquisto, Paziente paziente, User user, 
			float totale, Date dataAcquisto, ArrayList<ProdottiAcquistatiFull> prodotti) {
		this.acquisto = acquisto;
		this.paziente = paziente;
		this.user = user;
		Totale = totale;
		DataAcquisto = dataAcquisto;
		this.prodotti = prodotti;
	}
	public Acquisto() {	}
	public String getAcquisto() {
		return acquisto;
	}
	public void setAcquisto(String acquisto) {
		this.acquisto = acquisto;
	}
	public Paziente getPaziente() {
		return paziente;
	}
	public void setPaziente(Paziente paziente) {
		this.paziente = paziente;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public float getTotale() {
		return Totale;
	}
	public void setTotale(float totale) {
		Totale = totale;
	}
	public Date getDataAcquisto() {
		return DataAcquisto;
	}
	public void setDataAcquisto(Date dataAcquisto) {
		DataAcquisto = dataAcquisto;
	}
	public ArrayList<ProdottiAcquistatiFull> getProdotti() {
		return prodotti;
	}
	public void setProdotti(ArrayList<ProdottiAcquistatiFull> prodotti) {
		this.prodotti = prodotti;
	}
	public String getIdFarmacia() {
		return idFarmacia;
	}
	public void setIdFarmacia(String idFarmacia) {
		this.idFarmacia = idFarmacia;
	}
	
	
	
}
