package itc.obsttaxi;

import java.util.List;

import itc.obsttaxi.dto.BestellPositionDTO;

public class BestellPositionFactory {

	public enum Obst { 	
		BANANE(1), APFEL(2), BIRNE(3), GRANATAPFEL(4), ORANGE(5); 
		
		private final int id;
		
	    Obst(int id) {
			this.id = id;
		}
	    
	    public int getId() {
	    	return id; 
	    }
	}

	
	private List<BestellPositionDTO> bestellPositionen; 
	
	public BestellPositionFactory() {
		
	}
	
	public BestellPositionFactory addObst(Obst obst, int menge) {
		bestellPositionen.add(new BestellPositionDTO(obst.getId(), menge));
		
		return this;
	}
	
	public BestellPositionDTO[] createBestellArray() {
		
		return (BestellPositionDTO[]) bestellPositionen.toArray();
	}

}
