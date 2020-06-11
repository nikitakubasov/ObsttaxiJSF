package itc.obsttaxi.database;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;

import itc.obsttaxi.database.dao.BestellPosition;
import itc.obsttaxi.database.dao.Bestellung;
import itc.obsttaxi.database.dao.Kunde;
import itc.obsttaxi.database.dao.Obst;

public class DatabaseManager {
	
	private Connection connection;
	
	private static boolean isHandled;

	private static final String DB_PATH = System.getProperty("user.dir") + "/" + "testdb.db";

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	    isHandled = new File(DB_PATH).exists();
	}

	protected DatabaseManager(Connection connection) {
		this.connection = connection;
	}

	public Obst GetObst(int id) throws SQLException {
		Obst obst = new Obst();

		Statement stmt = connection.createStatement();

		ResultSet rs = stmt.executeQuery("SELECT * FROM obst WHERE id ==" + id);
		rs.next();

		obst.name = rs.getString("name");
		obst.preis = rs.getFloat("preis");
		obst.id = rs.getInt("id");

		rs.close();

		return obst;
	}

	public void AddKunde(Kunde kunde) {
		try (PreparedStatement ps = connection
				.prepareStatement("INSERT INTO kunden ('name', 'adresse', 'kundenid') VALUES (?, ?, ?);")) {

			ps.setString(1, kunde.vollerName);
			ps.setString(2, kunde.adresse + " " + kunde.plz);
			ps.setInt(3, kunde.id);
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void AddBestellung(Bestellung bestellung) {
		try (PreparedStatement ps = connection
				.prepareStatement("INSERT INTO bestellungen ('datum', 'kundenid', 'id') VALUES (?, ?, ?);")) {

			ps.setInt(2, bestellung.kunde.id);
			ps.setDate(1, Date.valueOf(LocalDate.now()));
			ps.setInt(3, bestellung.bestellungsid);
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int GetNewBestellungsid() {

		int maxValue = 0;

		try {
			Statement stmt = connection.createStatement();
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
	

	public int GetNewKundenid() {

		int maxValue = 0;

		try {
			Statement stmt = connection.createStatement();
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

	public void AddBestellPosition(BestellPosition bestellposition) {
		try (PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO bestell_positionen ('menge', 'bestellungsid', 'obstid') VALUES (?, ?, ?);")) {

			ps.setInt(1, bestellposition.menge);
			ps.setInt(2, bestellposition.bestellung.bestellungsid);
			ps.setInt(3, bestellposition.obst.id);

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void handleDB() {
		
		if(isHandled) return;
		
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
}
