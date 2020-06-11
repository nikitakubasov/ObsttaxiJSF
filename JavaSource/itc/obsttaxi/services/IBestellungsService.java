package itc.obsttaxi.services;

import itc.obsttaxi.dto.BestellPositionDTO;
import itc.obsttaxi.dto.KundeDTO;

public interface IBestellungsService {

	public void bestellen(KundeDTO kunde, BestellPositionDTO[] positionen);
}
