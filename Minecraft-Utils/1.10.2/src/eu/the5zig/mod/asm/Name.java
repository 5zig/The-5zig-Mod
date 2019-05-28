package eu.the5zig.mod.asm;

public class Name {

	private String name;
	private String desc;

	public Name(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

	public Name(String name) {
		this(name, "");
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public boolean equals(String name, String desc) {
		return this.name.equals(name) && this.desc.equals(desc);
	}

	public Class<?> load() throws ClassNotFoundException {
		return Thread.currentThread().getContextClassLoader().loadClass(name);
	}

}
