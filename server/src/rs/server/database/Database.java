package rs.server.database;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.resources.Player;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {

	public static class InvalidUsernameOrPasswordException extends Exception {
	}

	public static class AlreadyLoggedInException extends Exception {
	}
	public static final String DB_NAME = "rs";
	Connection _oConnection;

	public Database() {}

	public static void main(String[] sArgs) {
		Database o = new Database();
	}

	public boolean connect() {
		String sUrl = "jdbc:mysql://localhost:3306/mysql";
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			_oConnection = DriverManager.getConnection(sUrl, "root", "123456");
			checkDatabase();
			logEveryoneOut();
			return true;
		} catch (SQLException ex) {
//			System.out.println(ex.getSQLState());
//			ex.printStackTrace();
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public void disconnect() {
		try {
			_oConnection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Validates password and attempts to log in a player
	 * 
	 * @param sName Player username
	 * @param sPassword Player password
	 * @return Newly created Player object with data from database
	 */
	public Player logIn(String sName, String sPassword) throws InvalidUsernameOrPasswordException, AlreadyLoggedInException {
		Statement oStatement;
		ResultSet oResultset;
		boolean bAlreadyLoggedIn = false;
		Player oPlayer = null;

		try {
			oStatement = _oConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			oResultset = oStatement.executeQuery("SELECT * FROM users WHERE Username = '" + sName + "' AND Password = PASSWORD('" + sPassword + "')");

			while (oResultset.next()) {
				if (oResultset.getInt("IsLoggedIn") == 1) {
					bAlreadyLoggedIn = true;
				} else {
					oPlayer = new Player();
					oPlayer.Id = oResultset.getInt("Id");
					oPlayer.Username = oResultset.getString("Username");
					oPlayer.MapPosition.set(oResultset.getFloat("X"), oResultset.getFloat("Y"));
					oPlayer.Map = oResultset.getInt("Map");
					oPlayer.SpriteFilename = oResultset.getString("SpriteFilename");

				}
			}
			oStatement.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (bAlreadyLoggedIn) {
			throw new AlreadyLoggedInException();
		} else if (oPlayer == null) {
			throw new InvalidUsernameOrPasswordException();
		}

		try {
			oStatement = _oConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			oStatement.executeUpdate("UPDATE users SET IsLoggedIN = 1 WHERE Id = '" + oPlayer.Id + "'");
			oStatement.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return oPlayer;
	}

	public void logOut(int iId) {
		Statement oStatement;
		ResultSet oResultset;

		try {
			oStatement = _oConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			oStatement.executeUpdate("UPDATE users SET IsLoggedIn = 0 WHERE Id = '" + iId + "'");
			oStatement.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setIp(int iId, String sIp) {
		Statement oStatement;

		try {
			oStatement = _oConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			oStatement.executeUpdate("UPDATE users SET LastIp = \"" + sIp + "\" WHERE Id = '" + iId + "'");
			oStatement.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void logEveryoneOut() {
		Statement oStatement;

		try {
			oStatement = _oConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			oStatement.executeUpdate("UPDATE users SET IsLoggedIn = 0 WHERE 1");
			oStatement.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setCoords(int iId, float x, float y) {
		Statement oStatement;

		try {
			oStatement = _oConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			oStatement.executeUpdate("UPDATE users SET X = " + x + ", Y = " + y + " WHERE Id = '" + iId + "'");
			oStatement.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setMap(int iId, int i) {
		Statement oStatement;

		try {
			oStatement = _oConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			oStatement.executeUpdate("UPDATE users SET Map = " + i + " WHERE Id = '" + iId + "'");
			oStatement.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public List<Player> getUsers() {
		Statement oStatement;
		ResultSet oResultset;
		Player oPlayer = null;
		List<Player> oPlayers = new ArrayList<Player>();

		try {
			oStatement = _oConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			oResultset = oStatement.executeQuery("SELECT * FROM users");

			while (oResultset.next()) {
				oPlayer = new Player();
				oPlayer.Id = oResultset.getInt("Id");
				oPlayer.Username = oResultset.getString("Username");
				oPlayer.MapPosition.set(oResultset.getFloat("X"), oResultset.getFloat("Y"));
				oPlayer.Map = oResultset.getInt("Map");
				oPlayer.SpriteFilename = oResultset.getString("SpriteFilename");
				oPlayer.IsLoggedIn = oResultset.getInt("IsLoggedIn") == 1;
				oPlayers.add(oPlayer);
			}
			oStatement.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return oPlayers;
	}

	public Player getUser(int iId) {
		Statement oStatement;
		ResultSet oResultset;
		Player oPlayer = null;

		try {
			oStatement = _oConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			oResultset = oStatement.executeQuery("SELECT * FROM users WHERE Id = \"" + iId + "\"");

			while (oResultset.next()) {
				oPlayer = new Player();
				oPlayer.Id = oResultset.getInt("Id");
				oPlayer.Username = oResultset.getString("Username");
				oPlayer.MapPosition.set(oResultset.getFloat("X"), oResultset.getFloat("Y"));
				oPlayer.Map = oResultset.getInt("Map");
				oPlayer.SpriteFilename = oResultset.getString("SpriteFilename");
				oPlayer.IsLoggedIn = oResultset.getInt("IsLoggedIn") == 1;
				oPlayer.LastIp = oResultset.getString("LastIp");
				oPlayer.Privileges = oResultset.getInt("Privileges");
			}
			oStatement.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return oPlayer;
	}

	public void checkDatabase() {
		Statement oStatement;
		ResultSet oResultset;

		try {
			oStatement = _oConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			oResultset = oStatement.executeQuery("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '" + DB_NAME + "' ");
			if (!oResultset.next()) {
				System.out.println("Database doesnt exists, creating");
				oResultset.close();
				oStatement = _oConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				oStatement.execute("CREATE DATABASE " + DB_NAME);
				oStatement.close();
			}

			oStatement = _oConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			oStatement.execute("USE " + DB_NAME);
			oStatement.close();

			oStatement = _oConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			oResultset = oStatement.executeQuery("SHOW TABLES");

			List<String> oTables = new ArrayList<String>();

			while(oResultset.next()){
				String s = oResultset.getString(1);
				oTables.add(s);
			}

			oStatement.close();
			oResultset.close();

			if(!oTables.contains("users")){
				System.out.println("TABLE users doesnt exist, creating");
				oStatement = _oConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				oStatement.execute("CREATE TABLE users(Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, Username VARCHAR(100) NOT NULL DEFAULT '', Password VARCHAR(100) NOT NULL DEFAULT '', IsLoggedIn TINYINT(1) DEFAULT 0, LastIp VARCHAR(20) NOT NULL DEFAULT '', LastPort INT NOT NULL DEFAULT 0, Privileges INT NOT NULL DEFAULT 0, Map INT NOT NULL DEFAULT 0, X FLOAT NOT NULL DEFAULT 0, Y FLOAT NOT NULL DEFAULT 0, SpriteFilename VARCHAR(50) DEFAULT '') COLLATE utf8_unicode_ci ");
				oStatement.close();

				addUser("lainmaster", "123456");
			}

			if(!oTables.contains("items_unusable")){
				System.out.println("TABLE items_unusable doesnt exist, creating");
				InputStream is = getClass().getResourceAsStream("/rs/server/database/Table.Items.Unusable");
				String sql;
				try{
					sql = convertStreamToString(is);
					System.out.println(sql);
					oStatement = _oConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					oStatement.execute(sql);
					oStatement.close();
				}catch(IOException ex){
					Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
				}

			}

			if(!oTables.contains("items_consumable")){
				System.out.println("TABLE items_consumable doesnt exist, creating");
				InputStream is = getClass().getResourceAsStream("/rs/server/database/Table.Items.Consumable");
				String sql;
				try{
					sql = convertStreamToString(is);
					System.out.println(sql);
					oStatement = _oConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					oStatement.execute(sql);
					oStatement.close();
				}catch(IOException ex){
					Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
				}

			}



		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void addUser(String sUsername, String sPassword){
		Statement oStatement;
		try {
			oStatement = _oConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			oStatement.execute("INSERT INTO users(Username, Password) VALUES('" + sUsername + "', PASSWORD('" + sPassword + "'))");
			oStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void setPrivileges(int iId, int iPrivileges){
		Statement oStatement;

		try {
			oStatement = _oConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			oStatement.executeUpdate("UPDATE users SET Privileges = \"" + iPrivileges + "\" WHERE Id = '" + iId + "'");
			oStatement.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setSprite(int iId, String sFile){
		Statement oStatement;

		try {
			oStatement = _oConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			oStatement.executeUpdate("UPDATE users SET SpriteFilename = \"" + sFile + "\" WHERE Id = '" + iId + "'");
			oStatement.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

}
