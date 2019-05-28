package eu.the5zig.util.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public interface IDatabaseConfiguration {

	String getDriver();

	Connection getConnection() throws SQLException;

	int getThreadCount();
}
