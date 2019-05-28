package eu.the5zig.mod.server.simplehg;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.util.Vector2i;
import eu.the5zig.mod.util.Vector3i;

import java.util.List;
import java.util.Map;

public class ServerSimpleHG {

	public static class SimpleHG extends GameMode {

		private String kit;
		private Feast feast;
		private final List<MiniFeast> miniFeasts = Lists.newArrayList();
		private final Map<String, String> kitMap = Maps.newHashMap();

		public String getKit() {
			return kit;
		}

		public void setKit(String kit) {
			this.kit = kit;
		}

		public Feast getFeast() {
			return feast;
		}

		public void setFeast(Feast feast) {
			this.feast = feast;
		}

		public List<MiniFeast> getMiniFeasts() {
			return miniFeasts;
		}

		public Map<String, String> getKitMap() {
			return kitMap;
		}

		@Override
		public String getName() {
			return "SimpleHG";
		}
	}

	public static class Feast {

		private final Vector3i location;
		private final long time;

		public Feast(Vector3i location, long time) {
			this.location = location;
			this.time = time;
		}

		public Vector3i getLocation() {
			return location;
		}

		public long getTime() {
			return time;
		}
	}

	public static class MiniFeast {

		private final Vector2i start;
		private final Vector2i end;
		private final long time;

		public MiniFeast(Vector2i start, Vector2i end) {
			this.start = start;
			this.end = end;
			this.time = System.currentTimeMillis();
		}

		public Vector2i getStart() {
			return start;
		}

		public Vector2i getEnd() {
			return end;
		}

		public long getTime() {
			return time;
		}
	}

}
