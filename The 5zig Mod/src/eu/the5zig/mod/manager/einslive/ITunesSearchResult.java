package eu.the5zig.mod.manager.einslive;

import com.google.common.collect.Lists;

import java.util.List;

public class ITunesSearchResult {

	private int resultCount;
	private List<ITunesSearchResultEntry> results = Lists.newArrayList();

	public int getResultCount() {
		return resultCount;
	}

	public List<ITunesSearchResultEntry> getResults() {
		return results;
	}
}
