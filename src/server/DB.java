
/*
   Kyle Richardson
   CSE383-f15
   Program 2
   
   Maze Project Database Writing
   
   Class used to handle all queries to the database in the maze project
   
   NOTE ** Large portion of code re-used from previous labs **
   */

package server;

import java.sql.*;

public class DB {
	private String url = "jdbc:mysql://localhost/mazedata";
	private Connection conn = null;
	private String user = "mazeuser";
	private String pwd = "383";
	private Statement state;

	public DB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, pwd);
			state = conn.createStatement();
		} catch (ClassNotFoundException e) {
			System.err.println("Could not find proper class: " + e);
		} catch (SQLException e) {
			System.err.println("Could not connect to database: " + e);
		}
	}

	public void addUser(String user, int x, int y) throws SQLException {
		String query = "INSERT INTO maze VALUES(" + "\"" + user + "\", " + x
				+ ", " + y + ", " + System.currentTimeMillis() / 1000 + ", "
				+ 0 + ", " + "'ACTIVE')";
		state.executeUpdate(query);
	}

	public int[] getXY(String user) throws SQLException {

		String query = "SELECT x, y, state FROM maze WHERE user = " + "\""
				+ user + "\"";
		ResultSet rs = state.executeQuery(query);
		rs.next();
		if (!rs.getString("state").equals("ACTIVE")) {
			throw new SQLException("User state not ACTIVE");
		}
		int[] xy = { rs.getInt("x"), rs.getInt("y") };
		return xy;
	}

	public void move(int x, int y, String user) throws SQLException {
		String query = "UPDATE maze SET X = " + x + ", Y = " + y
				+ ", moves = moves + 1 WHERE user = \"" + user + "\"";
		state.executeUpdate(query);

	}

	public void deleteUser(String user) throws SQLException {
		String query = "DELETE FROM maze WHERE user = " + "\"" + user + "\"";
		state.executeUpdate(query);
	}

	public void finished(String user) throws SQLException {
		String query = "UPDATE maze SET state = 'FINISHED' WHERE user = " + "\""
				+ user + "\"";
		state.executeUpdate(query);
	}

	public ResultSet getMaze() throws SQLException {
		String query = "SELECT * FROM maze";
		ResultSet rs = state.executeQuery(query);
		return rs;
	}
}
