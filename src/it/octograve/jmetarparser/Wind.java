package it.octograve.jmetarparser;

/**
 * Representation of observed wind as a detail of a Metar.
 * 
 * @author Alessandro Rossi.
 * 
 * @version 0.1
 */
public class Wind {

	private static final String PATTERN = "\\s[\\w/]{5,8}KT";

	private int direction;

	private int minSpeed;

	private int maxSpeed;

	/**
	 * Sole constructor.
	 */
	private Wind() {
	}

	/**
	 * Returns wind direction, in degrees.
	 * 
	 * @return Wind direction, in degrees.
	 */
	public int getDirection() {
		return direction;
	}

	private void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * Returns wind maximun speed, in knots.
	 * 
	 * @return Wind maximun speed, in knots.
	 */
	public int getMaxSpeed() {
		return maxSpeed;
	}

	private void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	/**
	 * Returns wind minimum speed, in knots.
	 * 
	 * @return Wind minimum speed, in knots.
	 */
	public int getMinSpeed() {
		return minSpeed;
	}

	private void setMinSpeed(int minSpeed) {
		this.minSpeed = minSpeed;
	}

	/**
	 * Checks if this Wind is equal to provided object.
	 */
	public boolean equals(Object o) {
		if (o instanceof Wind) {
			Wind w = (Wind) o;
			return w.getDirection() == direction && w.getMaxSpeed() == maxSpeed
					&& w.getMinSpeed() == minSpeed;
		}
		return false;
	}

	/**
	 * String representation of this Condition.
	 */
	public String toString() {
		return "D" + direction + "s" + minSpeed + "S" + maxSpeed;
	}

	/**
	 * Parses a METAR string into a Wind object to be used as a Metar's detail.
	 * 
	 * @param metarString
	 *            The METAR string to parse.
	 * @return The parsed Wind.
	 */
	public static Wind parseWind(String metarString) {
		if (metarString == null) {
			throw new IllegalArgumentException("null string");
		}
		String[] results = Utils.resolveRegex(metarString, PATTERN);
		if (results == null) {
			return null;
		}
		String windString = results[0];
		Wind wind = new Wind();
		String directionString = windString.substring(0, 3);
		if (directionString.equalsIgnoreCase("VRB")
				|| directionString.equalsIgnoreCase("///")) {
			wind.setDirection(-1); // TODO check '///' meaning
		} else {
			wind.setDirection(Integer.parseInt(directionString));
		}
		String speedString = windString.substring(3, windString.indexOf("KT"));
		if (speedString.contains("//")) {
			return null;
		} else if (speedString.contains("G")) {
			String minSpeed = speedString
					.substring(0, speedString.indexOf("G"));
			String maxSpeed = speedString
					.substring(speedString.indexOf("G") + 1);
			wind.setMinSpeed(Integer.parseInt(minSpeed));
			wind.setMaxSpeed(Integer.parseInt(maxSpeed));
		} else {
			int speed = Integer.parseInt(speedString);
			wind.setMinSpeed(speed);
			wind.setMaxSpeed(speed);
		}
		return wind;
	}

}
