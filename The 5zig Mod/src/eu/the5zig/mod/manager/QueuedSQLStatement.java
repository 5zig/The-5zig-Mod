package eu.the5zig.mod.manager;

import eu.the5zig.util.Callback;
import eu.the5zig.util.db.Database;

public class QueuedSQLStatement {

	private Callback<Integer> callback;
	private boolean sync;
	private String query;
	private boolean returnGeneratedKeys;
	private Object[] parameters;

	private Callback<Database> databaseCallback;

	public QueuedSQLStatement(Callback<Integer> callback, boolean sync, boolean returnGeneratedKeys, String query, Object... parameters) {
		this.callback = callback;
		this.sync = sync;
		this.query = query;
		this.returnGeneratedKeys = returnGeneratedKeys;
		this.parameters = parameters;
	}

	public QueuedSQLStatement(Callback<Database> databaseCallback) {
		this.databaseCallback = databaseCallback;
	}

	public String getQuery() {
		return query;
	}

	public boolean isReturnGeneratedKeys() {
		return returnGeneratedKeys;
	}

	public boolean isSync() {
		return sync;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public Callback<Integer> getCallback() {
		return callback;
	}

	public Callback<Database> getDatabaseCallback() {
		return databaseCallback;
	}
}
