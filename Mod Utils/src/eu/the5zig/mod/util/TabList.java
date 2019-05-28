package eu.the5zig.mod.util;

public class TabList {

	private String header;
	private String footer;

	public TabList(String header, String footer) {
		this.header = header;
		this.footer = footer;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}
}
