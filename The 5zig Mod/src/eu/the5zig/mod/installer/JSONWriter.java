package eu.the5zig.mod.installer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Set;

public class JSONWriter {

	private Writer writer = null;
	private int indentStep = 2;
	private int indent = 0;

	public JSONWriter(Writer writer) {
		this.writer = writer;
	}

	public JSONWriter(Writer writer, int indentStep) {
		this.writer = writer;
		this.indentStep = indentStep;
	}

	public JSONWriter(Writer writer, int indentStep, int indent) {
		this.writer = writer;
		this.indentStep = indentStep;
		this.indent = indent;
	}

	public void writeObject(Object obj) throws IOException {
		if ((obj instanceof JSONObject)) {
			JSONObject jObj = (JSONObject) obj;
			writeJsonObject(jObj);
			return;
		}
		if ((obj instanceof JSONArray)) {
			JSONArray jArr = (JSONArray) obj;
			writeJsonArray(jArr);
			return;
		}
		this.writer.write(JSONValue.toJSONString(obj));
	}

	private void writeJsonArray(JSONArray jArr) throws IOException {
		writeLine("[");
		indentAdd();

		int num = jArr.size();
		for (int i = 0; i < num; i++) {
			Object val = jArr.get(i);

			writeIndent();

			writeObject(val);
			if (i < jArr.size() - 1) {
				write(",");
			}
			writeLine("");
		}
		indentRemove();
		writeIndent();
		this.writer.write("]");
	}

	private void writeJsonObject(JSONObject jObj) throws IOException {
		writeLine("{");
		indentAdd();

		Set keys = jObj.keySet();
		int keyNum = keys.size();
		int count = 0;
		for (Iterator it = keys.iterator(); it.hasNext(); ) {
			String key = (String) it.next();
			Object val = jObj.get(key);

			writeIndent();
			this.writer.write(JSONValue.toJSONString(key));
			this.writer.write(": ");

			writeObject(val);

			count++;
			if (count < keyNum) {
				writeLine(",");
			} else {
				writeLine("");
			}
		}
		indentRemove();
		writeIndent();
		this.writer.write("}");
	}

	private void writeLine(String str) throws IOException {
		this.writer.write(str);

		this.writer.write("\n");
	}

	private void write(String str) throws IOException {
		this.writer.write(str);
	}

	private void writeIndent() throws IOException {
		for (int i = 0; i < this.indent; i++) {
			this.writer.write(' ');
		}
	}

	private void indentAdd() {
		this.indent += this.indentStep;
	}

	private void indentRemove() {
		this.indent -= this.indentStep;
	}
}