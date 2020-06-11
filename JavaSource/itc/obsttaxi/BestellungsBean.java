package itc.obsttaxi;

import java.sql.SQLException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import itc.obsttaxi.database.DatabaseManager;
import itc.obsttaxi.database.dao.BestellPosition;
import itc.obsttaxi.database.dao.Bestellung;
import itc.obsttaxi.database.dao.Kunde;
import itc.obsttaxi.dto.BestellPositionDTO;
import itc.obsttaxi.dto.KundeDTO;
import itc.obsttaxi.services.BestellungsService;
import itc.obsttaxi.services.IBestellungsService;

@SessionScoped
@ManagedBean(name = "bestellung")
public class BestellungsBean {

	private String adresse, vollerName;
	private int anzahlBanane, anzahlApfel, anzahlBirne, anzahlGranatapfel, anzahlOrangen, postleitzahl;
	
	private IBestellungsService bestellungsService;
	
	public BestellungsBean() {
		bestellungsService = new BestellungsService();
	}
	
	
	public String bestellen() {
		
		KundeDTO kunde = new KundeDTO();
		kunde.adresse = adresse;
		kunde.vollerName = vollerName;
		kunde.plz = postleitzahl;
		
		BestellPositionDTO[] positionen = new BestellPositionDTO[] {
			new BestellPositionDTO(1, anzahlBanane),
			new BestellPositionDTO(2, anzahlApfel),
			new BestellPositionDTO(3, anzahlBirne),
			new BestellPositionDTO(4, anzahlGranatapfel),
			new BestellPositionDTO(5, anzahlOrangen)
		};
		
		bestellungsService.bestellen(kunde, positionen);
		
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
