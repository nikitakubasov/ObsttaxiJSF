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
    
    private static final String DB_PATH = System.getProperty("user.dir") + "/" + "testdb.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private DatabaseManager(){
    }
    
    public static DatabaseManager getInstance(){
        return databaseManager;
    }
    
    private void initDBConnection() {
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
    
    public Obst GetObst(int id) throws SQLException {
    	Obst obst = new Obst();
    	
        Statement stmt = DatabaseManager.connection.createStatement();
    	
        ResultSet rs = stmt.executeQuery("SELECT * FROM obst WHERE id ==" + id);
        rs.next();
        
        obst.name = rs.getString("name");
        obst.preis = rs.getFloat("preis");
        
        rs.close();
        
        return obst;
    }

    public void AddKunde(String vollerName, String adresse, int postleitzahl){
        try (PreparedStatement ps = connection
                .prepareStatement("INSERT INTO kunden ('name', 'adresse') VALUES (?, ?);")) {

            ps.setString(1, vollerName);
            ps.setString(2, adresse + " " + postleitzahl);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void AddBestellung(int kundenId){
        try (PreparedStatement ps = connection
                .prepareStatement("INSERT INTO bestellungen ('datum', 'kundenid') VALUES (?, ?);")) {

            ps.setInt(2, kundenId);
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void AddBestellPosition(int kundenId, int obstid, int menge){
        try (PreparedStatement ps = connection
                .prepareStatement("INSERT INTO bestell_positionen ('menge', 'bestellungsid', 'kundenid') VALUES (?, ?, ?);")) {

            ps.setInt(1, menge);
            ps.setInt(2, kundenId);
            ps.setInt(3, kundenId);

            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleDB() {
        try {
            Statement stmt = connection.createStatement();
            
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS obst (name varchar(30), preis decimal(4, 2), id integer primary key);");
            
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS kunden (name varchar(20), adresse varchar(30), kundenid integer primary key);");
            
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bestellungen (datum varchar(20), kundenid integer, id integer primary key,  FOREIGN KEY(kundenid) REFERENCES kunden(kundenid));");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bestell_positionen (menge integer, bestellungsid integer, obstid integer, id integer primary key,  "
            		+ "FOREIGN KEY(kundenid) REFERENCES obst(obstid), FOREIGN KEY(bestellungsid) REFERENCES bestellungen(id));");

            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO obst VALUES (?, ?, ?);");

            ps.setString(1, "Banane");
            ps.setFloat(2, 1f);
            ps.setInt(3, 1);
            ps.addBatch();
            
            ps.setString(1, "Apfel");
            ps.setFloat(2, 2f);
            ps.setInt(3, 2);
            ps.addBatch();
            
            ps.setString(1, "Birne");
            ps.setFloat(2, 1f);
            ps.setInt(3, 3);
            ps.addBatch();

            connection.setAutoCommit(false);
            ps.executeBatch();
            connection.setAutoCommit(true);

            ResultSet rs = stmt.executeQuery("SELECT * FROM obst;");
            while (rs.next()) {
                System.out.println("Name = " + rs.getString("name"));
                System.out.println("Preis = " + rs.getDouble("preis"));
            }
            rs.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Couldn't handle DB-Query");
            e.printStackTrace();
        }
    }
    
    public static void main (String[] args) {
    	DatabaseManager.databaseManager.initDBConnection();
    	DatabaseManager.databaseManager.handleDB();
    }

}
