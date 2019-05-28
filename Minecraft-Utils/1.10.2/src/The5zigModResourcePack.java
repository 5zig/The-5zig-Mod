import com.google.common.collect.ImmutableSet;

import java.io.File;
import java.util.Set;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class The5zigModResourcePack extends bwu {

	public static final Set<String> resourceDomains = ImmutableSet.of("the5zigmod");

	public The5zigModResourcePack() {
		super(new bws() {
			@Override
			public File a(kn kn) {
				return null;
			}

			@Override
			public boolean b(kn kn) {
				return false;
			}

			@Override
			public File a() {
				return null;
			}
		});
	}

	/**
	 * getResourceDomains
	 *
	 * @return a Set with all resource domains of this resource pack.
	 */
	@Override
	public Set<String> c() {
		return resourceDomains;
	}

	/**
	 * getName
	 *
	 * @return the Name of this Pack.
	 */
	@Override
	public String b() {
		return "The 5zig Mod";
	}
}
