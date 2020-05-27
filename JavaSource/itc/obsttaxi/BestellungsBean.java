package itc.obsttaxi;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import itc.obsttaxi.database.DatabaseManager;

@SessionScoped
@ManagedBean(name = "bestellung")
public class BestellungsBean {

	private String adresse, vollerName;
	private int anzahlBanane, anzahlApfel, anzahlBirne, anzahlGranatapfel, anzahlOrangen, postleitzahl;
	
	public String bestellen() {
		
		if(!DatabaseManager.isHandled()) {
			DatabaseManager.initDBConnection();
			DatabaseManager.handleDB();
		}
		
		int kundenid = DatabaseManager.GetNewKundenid();
		int bestellungsid = DatabaseManager.GetNewBestellungsid();
		
		DatabaseManager.AddKunde(vollerName, adresse, postleitzahl, kundenid);
		
		DatabaseManager.AddBestellung(kundenid, bestellungsid);
		
		if(anzahlBanane > 0) {
			DatabaseManager.AddBestellPosition(kundenid, 1, anzahlBanane);
		}
		
		if(anzahlApfel > 0) {
			DatabaseManager.AddBestellPosition(kundenid, 2, anzahlApfel);
		}
		
		if(anzahlBirne > 0) {
			DatabaseManager.AddBestellPosition(kundenid, 3, anzahlBirne);
		}
		
		if(anzahlOrangen > 0) {
			DatabaseManager.AddBestellPosition(kundenid, 4, anzahlOrangen);
		}
		
		if(anzahlGranatapfel > 0) {
			DatabaseManager.AddBestellPosition(kundenid, 5, anzahlGranatapfel);
		}
		
		return "finished";
	}
	
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public String getVollerName() {
		return vollerName;
	}
	public void setVollerName(String vollerName) {
		this.vollerName = vollerName;
	}
	public int getAnzahlBanane() {
		return anzahlBanane;
	}
	public void setAnzahlBanane(int anzahlBanane) {
		this.anzahlBanane = anzahlBanane;
	}
	public int getAnzahlApfel() {
		return anzahlApfel;
	}
	public void setAnzahlApfel(int anzahlApfel) {
		this.anzahlApfel = anzahlApfel;
	}
	public int getAnzahlBirne() {
		return anzahlBirne;
	}
	public void setAnzahlBirne(int anzahlBirne) {
		this.anzahlBirne = anzahlBirne;
	}
	public int getAnzahlGranatapfel() {
		return anzahlGranatapfel;
	}
	public void setAnzahlGranatapfel(int anzahlGranatapfel) {
		this.anzahlGranatapfel = anzahlGranatapfel;
	}
	public int getAnzahlOrangen() {
		return anzahlOrangen;
	}
	public void setAnzahlOrangen(int anzahlOrangen) {
		this.anzahlOrangen = anzahlOrangen;
	}

	public int getPostleitzahl() {
		return postleitzahl;
	}

	public void setPostleitzahl(int postleitzahl) {
		this.postleitzahl = postleitzahl;
	}

	
}
