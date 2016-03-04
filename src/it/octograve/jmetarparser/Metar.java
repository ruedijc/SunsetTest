package it.octograve.jmetarparser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

/**
 * Data structure to represent a METAR observation.
 * 
 * To generate a Metar object starting from a string, just invoke the static
 * parseMetar() method:
 * 
 * <pre>
 * Metar myMetar = Metar.parseMetar(myMetarString);
 * </pre>
 * 
 * @author Alessandro Rossi
 * 
 * @version 0.1
 */
public class Metar {

	private static final String ICAO_PATTERN = "\\w{4}\\s";

	private static final String DATE_PATTERN = "\\d{6}Z";

	private String metarString;

	private String icao;

	private Date date;

	private Wind wind;

	private Visibility visibility;

	private ArrayList<Condition> conditions;

	private ArrayList<Cloud> clouds;

	private Temperature temperature;

	private Pressure pressure;

	/**
	 * Sole constructor.
	 */
	private Metar() {
		conditions = new ArrayList<Condition>();
		clouds = new ArrayList<Cloud>();
	}

	/**
	 * Returns an ArrayList which contains all of clouds conditions of this
	 * obervation.
	 * 
	 * @return An ArrayList which contains all of clouds conditions.
	 */
	public ArrayList<Cloud> getClouds() {
		return clouds;
	}

	private void setClouds(ArrayList<Cloud> clouds) {
		this.clouds = clouds;
	}

	/**
	 * Returns the METAR's ICAO, a four-letter string indexing the station which
	 * has reported this observation.
	 * 
	 * @return The METAR's ICAO.
	 */
	public String getIcao() {
		return icao;
	}

	private void setIcao(String icao) {
		this.icao = icao;
	}

	/**
	 * Returns an ArrayList which contains all of weather phenomena of this
	 * obervation.
	 * 
	 * @return An ArrayList which contains all of weather phenomena.
	 */
	public ArrayList<Condition> getConditions() {
		return conditions;
	}

	private void setConditions(ArrayList<Condition> conditions) {
		this.conditions = conditions;
	}

	/**
	 * Returns the observation date.
	 * 
	 * @return The observation date.
	 */
	public Date getDate() {
		return date;
	}

	private void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Returns the METAR string which has been parsed into this object.
	 * 
	 * @return The METAR string which has been parsed into this object.
	 */
	public String getMetarString() {
		return metarString;
	}

	private void setMetarString(String metarString) {
		this.metarString = metarString;
	}

	/**
	 * Returns the pressure.
	 * 
	 * @return The pressure.
	 */
	public Pressure getPressure() {
		return pressure;
	}

	private void setPressure(Pressure pressure) {
		this.pressure = pressure;
	}

	/**
	 * Returns the temperature in Celsius grades.
	 * 
	 * @return the temperature in Celsius grades.
	 */
	public Temperature getTemperature() {
		return temperature;
	}

	private void setTemperature(Temperature temperature) {
		this.temperature = temperature;
	}

	/**
	 * Returns visibility.
	 * 
	 * @return Visibility.
	 */
	public Visibility getVisibility() {
		return visibility;
	}

	private void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	/**
	 * Returns wind's direction and speed.
	 * 
	 * @return Wind's direction and speed.
	 */
	public Wind getWind() {
		return wind;
	}

	private void setWind(Wind wind) {
		this.wind = wind;
	}

	/**
	 * Checks if this Metar is equal to provided object.
	 */
	public boolean equals(Object o) {
		if (o instanceof Metar) {
			Metar m = (Metar) o;
			return m.getIcao().equals(icao) && m.getDate().equals(date)
					&& m.getTemperature().equals(temperature)
					&& m.getPressure().equals(pressure)
					&& m.getWind().equals(wind) && m.getClouds().equals(clouds)
					&& m.getConditions().equals(conditions)
					&& m.getVisibility().equals(visibility);
		}
		return false;
	}

	/**
	 * Returns the Metar in string form.
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("METAR " + icao + ": [");
		buffer.append(date + "; ");
		buffer.append(temperature + "; ");
		buffer.append(wind + "; ");
		buffer.append(visibility + "; ");
		buffer.append(pressure);
		if (!clouds.isEmpty()) {
			buffer.append("; ");
		}
		for (Iterator i = clouds.iterator(); i.hasNext();) {
			buffer.append(i.next());
			if (i.hasNext()) {
				buffer.append(", ");
			}
		}
		if (!conditions.isEmpty()) {
			buffer.append("; ");
		}
		for (Iterator i = conditions.iterator(); i.hasNext();) {
			buffer.append(i.next());
			if (i.hasNext()) {
				buffer.append(", ");
			}
		}
		buffer.append("]");
		return buffer.toString();
	}

	/**
	 * Parses the provided METAR string into a Date object.
	 * 
	 * @param metarString
	 *            The METAR string to parse.
	 * @return The parsed Date object.
	 * @throws MetarParsingException
	 *             In case of invalid METAR string.
	 */
	private static Date parseDate(String metarString) {
		if (metarString == null) {
			throw new NullPointerException();
		}
		String[] results = Utils.resolveRegex(metarString, DATE_PATTERN);
		if (results == null) {
			throw new MetarParsingException("Date string not found. "
					+ "Invalid METAR string (" + metarString + ").");
		}
		String dateString = results[0].substring(0, results[0].indexOf("Z"));
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		int day = Integer.parseInt(dateString.substring(0, 2));
		int hour = Integer.parseInt(dateString.substring(2, 4));
		int mins = Integer.parseInt(dateString.substring(4, 6));
		calendar.set(Calendar.DATE, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, mins);
		if (calendar.after(Calendar.getInstance())) {
			calendar.add(Calendar.MONTH, -1);
		}
		return calendar.getTime();
	}

	/**
	 * Parses the provided METAR string into a Metar object.
	 * 
	 * @param metarString
	 *            The METAR string to parse.
	 * @return The parsed Metar object.
	 * @throws MetarParsingException
	 *             In case of invalid METAR string.
	 */
	public static Metar parseMetar(String metarString) {
		if (metarString == null) {
			throw new NullPointerException();
		}
		String[] results = Utils.resolveRegex(metarString, ICAO_PATTERN);
		if (results == null) {
			throw new MetarParsingException("ICAO string not found. "
					+ "Invalid METAR string (" + metarString + ").");
		}
		Metar metar = new Metar();
		metar.setMetarString(metarString);
		String icao = results[0];
		metar.setIcao(icao);
		int detailsStart = metarString.indexOf(icao) + icao.length();
		int detailsEnd = metarString.contains("RMK") ? metarString
				.indexOf("RMK") : metarString.length();
		String detailsString = metarString.substring(detailsStart, detailsEnd)
				.trim();
		metar.setDate(parseDate(detailsString));
		metar.setWind(Wind.parseWind(detailsString));
		metar.setVisibility(Visibility.parseVisibility(detailsString));
		metar.setTemperature(Temperature.parseTemperature(detailsString));
		metar.setPressure(Pressure.parsePressure(detailsString));
		metar.setClouds(Cloud.parseClouds(detailsString));
		metar.setConditions(Condition.parseConditions(detailsString));
		return metar;
	}

}
