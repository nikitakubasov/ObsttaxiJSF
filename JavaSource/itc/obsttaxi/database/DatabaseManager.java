package itc.obsttaxi.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DatabaseManager {

	private static final DatabaseManager databaseManager = new DatabaseManager();
	private static Connection connection;
	
	private static boolean isHandled;

	private static final String DB_PATH = System.getProperty("user.dir") + "/" + "testdb.db";

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	    System.out.println(DB_PATH);
	}

	private DatabaseManager() {
	}

	public static DatabaseManager getInstance() {
		return databaseManager;
	}

	public static void initDBConnection() {
		
		try {
			if (connection != null)
				return;

			connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					if (!connection.isClosed() && connection != null) {
						connection.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static Obst GetObst(int id) throws SQLException {
		Obst obst = new Obst();

		Statement stmt = DatabaseManager.connection.createStatement();

		ResultSet rs = stmt.executeQuery("SELECT * FROM obst WHERE id ==" + id);
		rs.next();

		obst.name = rs.getString("name");
		obst.preis = rs.getFloat("preis");

		rs.close();

		return obst;
	}

	public static void AddKunde(String vollerName, String adresse, int postleitzahl, int kundenid) {
		try (PreparedStatement ps = connection
				.prepareStatement("INSERT INTO kunden ('name', 'adresse', 'kundenid') VALUES (?, ?, ?);")) {

			ps.setString(1, vollerName);
			ps.setString(2, adresse + " " + postleitzahl);
			ps.setInt(3, kundenid);
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void AddBestellung(int kundenId, int bestellungsid) {
		try (PreparedStatement ps = connection
				.prepareStatement("INSERT INTO bestellungen ('datum', 'kundenid', 'id') VALUES (?, ?, ?);")) {

			ps.setInt(2, kundenId);
			ps.setDate(1, Date.valueOf(LocalDate.now()));
			ps.setInt(3, bestellungsid);
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int GetNewBestellungsid() {

		int maxValue = 0;

		try {
			Statement stmt = DatabaseManager.connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM bestellungen");
			if (rs.next()) {
				maxValue = rs.getInt(1);
			}

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();

		}

		return maxValue + 1;
	}
	

	public static int GetNewKundenid() {

		int maxValue = 0;

		try {
			Statement stmt = DatabaseManager.connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(kundenid) FROM kunden");
			if (rs.next()) {
				maxValue = rs.getInt(1);
			}

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();

		}

		return maxValue + 1;
	}

	public static void AddBestellPosition(int kundenId, int obstid, int menge) {
		try (PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO bestell_positionen ('menge', 'bestellungsid', 'obstid') VALUES (?, ?, ?);")) {

			ps.setInt(1, menge);
			ps.setInt(2, kundenId);
			ps.setInt(3, obstid);

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void handleDB() {
		try {
			Statement stmt = connection.createStatement();

			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS obst (name varchar(30), preis decimal(4, 2), id integer primary key);");

			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS kunden (name varchar(20), adresse varchar(30), kundenid integer primary key);");

			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS bestellungen (datum varchar(20), kundenid integer, id integer primary key,  FOREIGN KEY(kundenid) REFERENCES kunden(kundenid));");

			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS bestell_positionen (menge integer, bestellungsid integer, obstid integer, id integer primary key,  "
							+ "FOREIGN KEY(obstid) REFERENCES obst(obstid), FOREIGN KEY(bestellungsid) REFERENCES bestellungen(id));");

			PreparedStatement ps = connection.prepareStatement("INSERT INTO obst VALUES (?, ?, ?);");

			ps.setString(1, "Banane");
			ps.setFloat(2, 1f);
			ps.setInt(3, 1);
			ps.addBatch();

			ps.setString(1, "Apfel");
			ps.setFloat(2, 2f);
			ps.setInt(3, 2);
			ps.addBatch();

			ps.setString(1, "Birne");
			ps.setFloat(2, 2f);
			ps.setInt(3, 3);
			ps.addBatch();
			
			ps.setString(1, "Orangen");
			ps.setFloat(2, 2f);
			ps.setInt(3, 4);
			ps.addBatch();

			ps.setString(1, "Granatapfel");
			ps.setFloat(2, 1f);
			ps.setInt(3, 5);
			ps.addBatch();

			connection.setAutoCommit(false);
			ps.executeBatch();
			connection.setAutoCommit(true);

		} catch (SQLException e) {
			System.err.println("Couldn't handle DB-Query");
			e.printStackTrace();
		}
		
		isHandled = true;
	}
	
	public static boolean isHandled() {
		return isHandled;
	}

}
