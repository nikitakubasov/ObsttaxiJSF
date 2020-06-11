package itc.obsttaxi.dto;

public class BestellPositionDTO {

	public int menge;
	
	public int obstId;
	
	
	public BestellPositionDTO(int obst, int menge) {
		this.menge = menge;
		this.obstId = obst;
	}

}
