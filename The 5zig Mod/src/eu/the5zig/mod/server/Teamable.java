package eu.the5zig.mod.server;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 * <p/>
 * Class for Server Gamemodes to detect, if teams are allowed.
 */
public interface Teamable {

	boolean isTeamsAllowed();

	void setTeamsAllowed(boolean allowed);

}
