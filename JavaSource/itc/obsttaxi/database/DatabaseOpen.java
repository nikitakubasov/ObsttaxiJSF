package itc.obsttaxi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseOpen implements IDatabaseState {
	
	private static Connection connection;
	
	private static final String DB_PATH = System.getProperty("user.dir") + "/" + "testdb.db";
	
	private DatabaseManager manager;

	public DatabaseOpen() {
	}

	@Override
	public DatabaseManager getDatabaseManager() throws Exception {
		
		if(manager == null) manager = new DatabaseManager(connection);
		
		return manager;
	}

	@Override
	public void openState() {
		try {
			if (connection != null && connection.isClosed() == false)
				return;

			connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		
	}

	@Override
	public void closeState() throws Exception{
			try {
				connection.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
	}

}
