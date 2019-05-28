package eu.the5zig.mod.server;

import com.google.common.collect.Lists;
import eu.the5zig.util.Callback;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.regex.Pattern;

public class MultiLineIgnore {

	private final RegisteredServerInstance instance;

	private String startMessage;
	private String endMessage;

	private Pattern startPattern;
	private int numberOfMessages;

	private Pattern abort;
	private Callback<IMultiPatternResult> callback;

	private boolean startedListening = false;
	private final List<String> messages = Lists.newArrayList();

	public MultiLineIgnore(RegisteredServerInstance instance, String startMessage, String endMessage, Callback<IMultiPatternResult> callback) {
		this.instance = instance;
		this.startMessage = startMessage;
		this.endMessage = endMessage;
		this.callback = callback;
	}

	public MultiLineIgnore(RegisteredServerInstance instance, Pattern startPattern, int numberOfMessages, Pattern abort, Callback<IMultiPatternResult> callback) {
		this.instance = instance;
		this.startPattern = startPattern;
		this.numberOfMessages = numberOfMessages;
		this.abort = abort;
		this.callback = callback;
	}

	public String getStartMessage() {
		return startMessage;
	}

	public String getEndMessage() {
		return endMessage;
	}

	public Pattern getStartPattern() {
		return startPattern;
	}

	public int getNumberOfMessages() {
		return numberOfMessages;
	}

	public int getCurrentMessageCount() {
		return messages.size();
	}

	public Pattern getAbort() {
		return abort;
	}

	public Callback<IMultiPatternResult> getCallback() {
		return callback;
	}

	public boolean hasStartedListening() {
		return startedListening;
	}

	public void setStartedListening(boolean startedListening) {
		this.startedListening = startedListening;
	}

	public void add(String message) {
		Validate.validState(startedListening, "Hadn't started listening yet!");
		messages.add(message);
	}

	public void callCallback() {
		Validate.validState(startedListening, "Hadn't started listening yet!");
		callback.call(new MultiPatternResult(instance, messages));
	}
}
