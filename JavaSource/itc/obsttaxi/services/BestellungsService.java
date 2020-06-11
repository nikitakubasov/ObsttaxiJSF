package itc.obsttaxi.services;

import java.sql.SQLException;

import itc.obsttaxi.database.Database;
import itc.obsttaxi.database.DatabaseManager;
import itc.obsttaxi.database.dao.BestellPosition;
import itc.obsttaxi.database.dao.Bestellung;
import itc.obsttaxi.database.dao.Kunde;
import itc.obsttaxi.dto.BestellPositionDTO;
import itc.obsttaxi.dto.KundeDTO;

public class BestellungsService implements IBestellungsService {

	private Database database;

	public BestellungsService() {
		database = new Database();

	}

	@Override
	public void bestellen(KundeDTO kundeDto, BestellPositionDTO[] positionen) {
		try {
			database.open();

			database.getManager().handleDB();

			int kundenid = database.getManager().GetNewKundenid();
			int bestellungsid = database.getManager().GetNewBestellungsid();

			Kunde kunde = new Kunde();
			kunde.vollerName = kundeDto.vollerName;
			kunde.adresse = kundeDto.adresse;
			kunde.plz = kundeDto.plz;
			kunde.id = kundenid;

			database.getManager().AddKunde(kunde);

			Bestellung bestellung = new Bestellung();
			bestellung.bestellungsid = bestellungsid;
			bestellung.kunde = kunde;

			database.getManager().AddBestellung(bestellung);

			for (int a = 0; a < positionen.length; a++) {
				if (positionen[a].menge > 0) {

					BestellPosition position = new BestellPosition();
					position.bestellung = bestellung;
					position.menge = positionen[a].menge;
					try {
						position.obst = database.getManager().GetObst(positionen[a].obstId);
					} catch (SQLException e) {
						e.printStackTrace();
					}

					database.getManager().AddBestellPosition(position);
				}
			}

			database.close();

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
