package it.octograve.jmetarparser;

import java.util.ArrayList;

/**
 * Representaion of cloudiness at a fixed height as a detail of a METAR
 * observation.
 * 
 * @author Alessandro Rossi.
 * 
 * @version 0.1
 */
public class Cloud {

	private static final String PATTERN = "[A-Z]{3}\\d{3}";

	private CloudType type;

	private long height;

	/**
	 * Enumeration of cloud types.
	 */
	public enum CloudType {

		/** Few clouds, 1/8 to 2/8 coverage. */
		FEW,

		/** Scattered clouds, 3/8 to 4/8 coverage. */
		SCT,

		/** Broken coverage, 5/8 to 7/8 coverage. */
		BKN,

		/** Overcast, 8/8 coverage. */
		OVC;

	}

	/**
	 * Sole constructor
	 */
	private Cloud() {
	}

	/**
	 * Returns the height of this clouds group in feet.
	 * 
	 * @return the height of this clouds group in feet.
	 */
	public long getHeight() {
		return height;
	}

	private void setHeight(long height) {
		this.height = height;
	}

	/**
	 * Returns the type of this clouds group.
	 * 
	 * @return The type of this clouds group.
	 */
	public CloudType getType() {
		return type;
	}

	private void setType(CloudType type) {
		this.type = type;
	}

	/**
	 * Checks if this Cloud object is equal to provided object.
	 */
	public boolean equals(Object o) {
		if (o instanceof Cloud) {
			Cloud c = (Cloud) o;
			return type.equals(c.getType()) && height == c.getHeight();
		}
		return false;
	}

	/**
	 * String representation of this Cloud.
	 */
	public String toString() {
		return type + "@" + height;
	}

	/**
	 * Parses a METAR string into an ArrayList of Clouds to be used as a Metar's
	 * detail and returns it.
	 * 
	 * @param metarString
	 *            The METAR string to parse.
	 * @return An ArrayList of Clouds.
	 */
	public static ArrayList<Cloud> parseClouds(String metarString) {
		if (metarString == null) {
			throw new IllegalArgumentException("null string");
		}
		ArrayList<Cloud> clouds = new ArrayList<Cloud>();
		if (metarString.contains("SKC")) {
			return clouds;
		}
		String[] results = Utils.resolveRegex(metarString, PATTERN);
		if (results == null) {
			return clouds;
		}
		for (String s : results) {
			Cloud c = new Cloud();
			if (s.startsWith("FEW")) {
				c.setType(CloudType.FEW);
			} else if (s.startsWith("SCT")) {
				c.setType(CloudType.SCT);
			} else if (s.startsWith("BKN")) {
				c.setType(CloudType.BKN);
			} else if (s.startsWith("OVC")) {
				c.setType(CloudType.OVC);
			} else {
				continue;
			}
			c.setHeight(Long.parseLong(s.substring(3)) * 100l);
			clouds.add(c);
		}
		return clouds;
	}

}
