package itc.obsttaxi.database;

public class DatabaseClosed implements IDatabaseState  {

	public DatabaseClosed() {
		
	}

	@Override
	public DatabaseManager getDatabaseManager() throws Exception {
		
		throw new Exception("Datenbank ist nicht geöffnet.");
	}

	@Override
	public void openState() {

	}

	@Override
	public void closeState() throws Exception {
		
	}

}
