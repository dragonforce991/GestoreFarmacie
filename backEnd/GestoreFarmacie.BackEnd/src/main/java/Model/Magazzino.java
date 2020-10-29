package Model;

public class Magazzino {
	
	String id;
	Integer quantita;
	
	/*
	 * Intenzionalmente lasciati come String e non come oggetto, per favorire l'inserimento di record (utilizzato solo come wrapper per db).
	 * Se sarà necessario un ritorno di questo tipo di record, verrà impostato un wrapper {idFarmacia {Prodotto--> quantita}} 
	 */
	String idProdotto;
	String idFarmacia;
	
	
	public Magazzino(String id, Integer quantita, String idProdotto, String idFarmacia) {
		this.id = id;
		this.quantita = quantita;
		this.idProdotto = idProdotto;
		this.idFarmacia = idFarmacia;
	}
	public Magazzino() {}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getQuantita() {
		return quantita;
	}
	public void setQuantita(Integer quantita) {
		this.quantita = quantita;
	}
	public String getIdProdotto() {
		return idProdotto;
	}
	public void setIdProdotto(String idProdotto) {
		this.idProdotto = idProdotto;
	}
	public String getIdFarmacia() {
		return idFarmacia;
	}
	public void setIdFarmacia(String idFarmacia) {
		this.idFarmacia = idFarmacia;
	}
	
	
}
