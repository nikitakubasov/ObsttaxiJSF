package itc.obsttaxi.database;

public interface IDatabaseState {

	public DatabaseManager getDatabaseManager() throws Exception;
	
	public void openState();
	
	public void closeState() throws Exception;
}
