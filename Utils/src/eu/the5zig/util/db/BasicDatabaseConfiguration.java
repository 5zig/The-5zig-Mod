package eu.the5zig.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class BasicDatabaseConfiguration implements IDatabaseConfiguration {

	private final String driver;
	private final String host;
	private final int port;
	private final String user;
	private final String pass;
	private final String database;

	public BasicDatabaseConfiguration(String host, int port, String user, String pass, String database) {
		this("com.mysql.jdbc.Driver", host, port, user, pass, database);
	}

	public BasicDatabaseConfiguration(String driver, String host, int port, String user, String pass, String database) {
		this.driver = driver;
		this.host = host;
		this.port = port;
		this.user = user;
		this.pass = pass;
		this.database = database;
	}

	@Override
	public String getDriver() {
		return driver;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

	public String getDatabase() {
		return database;
	}

	public String getURL() {
		return String.format("jdbc:mysql://%s:%s/%s", getHost(), getPort(), getDatabase());
	}

	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(getURL(), getUser(), getPass());
	}

	@Override
	public int getThreadCount() {
		return 4;
	}

	@Override
	public String toString() {
		return host + ":" + port;
	}
}
