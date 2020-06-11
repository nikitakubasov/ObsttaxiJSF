package itc.obsttaxi.database;

public class Database {
	
	private static IDatabaseState state = new DatabaseClosed();

	public Database() {
	}
	
	public void open() throws Exception {
		state.closeState();
		
		state = new DatabaseOpen();
		state.openState();
	}
	
	public void close() throws Exception {
		state.closeState();
		
		state = new DatabaseClosed();
	}
	
	public DatabaseManager getManager() throws Exception {
		return state.getDatabaseManager();
	}

}
