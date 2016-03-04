package it.octograve.jmetarparser;

/**
 * Representation of value as a detail of a METAR.
 * 
 * @author Alessandro Rossi
 * 
 * @version 0.1
 */
public class Pressure {

	private static final String PATTERN = "[AQ]\\d{4}";

	private PressureMeasure measure;

	private float value;

	/**
	 * Pressure measures.
	 */
	public enum PressureMeasure {

		/** Millibars. */
		MBARS,

		/** Inches of mercury. */
		IOM;

	}

	/**
	 * Sole constructor.
	 */
	private Pressure() {
	}

	/**
	 * Returns value measure.
	 * 
	 * @return Pressure measure.
	 */
	public PressureMeasure getMeasure() {
		return measure;
	}

	private void setMeasure(PressureMeasure measure) {
		this.measure = measure;
	}

	/**
	 * Returns value value.
	 * 
	 * @return Pressure value.
	 */
	public float getValue() {
		return value;
	}

	private void setValue(float pressure) {
		this.value = pressure;
	}

	/**
	 * Checks if this Pressure is equal to provided object.
	 */
	public boolean equals(Object o) {
		if (o instanceof Pressure) {
			Pressure p = (Pressure) o;
			return value == p.getValue()
					&& measure.equals(p.getMeasure());
		}
		return false;
	}

	/**
	 * String representation of this Pressure.
	 */
	public String toString() {
		return value + " " + measure;
	}

	/**
	 * Parses a METAR string into a Pressure object to be used as a Metar's
	 * detail.
	 * 
	 * @param metarString
	 *            The METAR string to parse.
	 * @return The parsed Pressure.
	 */
	public static Pressure parsePressure(String metarString) {
		if (metarString == null) {
			throw new IllegalArgumentException("null string");
		}
		String[] results = Utils.resolveRegex(metarString, PATTERN);
		if (results == null) {
			return null;
		}
		String pressureString = results[0];
		Pressure pressure = new Pressure();
		if (pressureString.startsWith("Q")) {
			pressure.setMeasure(PressureMeasure.MBARS);
			pressure.setValue(Float.parseFloat(pressureString.substring(1)));
		} else if (pressureString.startsWith("A")) {
			pressure.setMeasure(PressureMeasure.IOM);
			float i = Float.parseFloat(pressureString.substring(1, 3));
			float d = Float.parseFloat(pressureString.substring(3));
			pressure.setValue((d / 100f) + i);
		} else {
			return null;
		}
		return pressure;
	}

}
