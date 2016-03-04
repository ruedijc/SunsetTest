package it.octograve.jmetarparser;

/**
 * Representation of temperature and dew point as a detail of a METAR.
 * 
 * @author Alessandro Rossi.
 * 
 * @version 0.1
 */
public class Temperature {

	private static final String PATTERN = "M?\\d{2}/M?\\d{2}";

	private int temperature;

	private int dewPoint;

	/**
	 * Sole constructor.
	 */
	private Temperature() {
	}

	/**
	 * Returns the dew point, in celsius degrees.
	 * 
	 * @return The dew point, in celsius degrees.
	 */
	public int getDewPoint() {
		return dewPoint;
	}

	private void setDewPoint(int dewPoint) {
		this.dewPoint = dewPoint;
	}

	/**
	 * Returns the temperature, in celsius degrees.
	 * 
	 * @return The temperature, in celsius degrees.
	 */
	public int getTemperature() {
		return temperature;
	}

	private void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	/**
	 * Checks if this Temperature is equal to provided object.
	 */
	public boolean equals(Object o) {
		if (o instanceof Temperature) {
			Temperature t = (Temperature) o;
			return temperature == t.getTemperature()
					&& dewPoint == t.getDewPoint();
		}
		return false;
	}

	/**
	 * String representation of this Temperature.
	 */
	public String toString() {
		return "T" + temperature + "D" + dewPoint;
	}

	/**
	 * Parses a METAR string into a Temperature object to be used as a Metar's
	 * detail.
	 * 
	 * @param metarString
	 *            The METAR string to parse.
	 * @return The parsed Temperature.
	 */
	public static Temperature parseTemperature(String metarString) {
		if (metarString == null) {
			throw new IllegalArgumentException("null string");
		}
		String[] results = Utils.resolveRegex(metarString, PATTERN);
		if (results == null) {
			return null;
		}
		String temperatureString = results[0].substring(0, results[0]
				.indexOf("/"));
		String dewPointString = results[0]
				.substring(results[0].indexOf("/") + 1);
		int temp, dew;
		if (temperatureString.startsWith("M")) {
			temp = Integer.parseInt(temperatureString.substring(1));
			temp = -temp;
		} else {
			temp = Integer.parseInt(temperatureString);
		}
		if (dewPointString.startsWith("M")) {
			dew = Integer.parseInt(dewPointString.substring(1));
			dew = -dew;
		} else {
			dew = Integer.parseInt(dewPointString);
		}
		Temperature temperature = new Temperature();
		temperature.setTemperature(temp);
		temperature.setDewPoint(dew);
		return temperature;
	}

}
