package eu.the5zig.util.db;

import eu.the5zig.util.db.exceptions.NoConnectionException;

import java.sql.Connection;

public class DummyDatabase extends Database {

	public DummyDatabase() throws NoConnectionException {
		super(null);
	}

	@Override
	protected synchronized Connection openConnection() {
		connected = true;
		return null;
	}

	@Override
	public <T> SQLQuery<T> get(Class<T> entity) {
		return new DummySQLQuery<T>(this, entity);
	}

	@Override
	public int doUpdate(String query, Object... fields) {
		return 0;
	}

	@Override
	protected int doUpdateWithGeneratedKeys(String query, Object... fields) {
		return 0;
	}

	@Override
	public synchronized Connection getConnection() throws NoConnectionException {
		return null;
	}

	@Override
	public synchronized boolean hasConnection() {
		return true;
	}

	@Override
	public synchronized void closeConnection() {
	}
}
