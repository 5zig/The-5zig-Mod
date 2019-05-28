package eu.the5zig.util.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class FileDatabaseConfiguration implements IDatabaseConfiguration {

	private final String driver;
	private File file;
	private final String properties;

	public FileDatabaseConfiguration(File file, String... properties) {
		this.driver = "org.h2.Driver";
		this.file = file;
		StringBuilder stringBuilder = new StringBuilder();
		for (String property : properties) {
			stringBuilder.append(";").append(property);
		}
		this.properties = stringBuilder.toString();
	}

	@Override
	public String getDriver() {
		return driver;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public String getProperties() {
		return properties;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:h2:" + getFile().getAbsolutePath() + getProperties());
	}

	@Override
	public int getThreadCount() {
		return 1;
	}

	@Override
	public String toString() {
		return file.toString();
	}
}
