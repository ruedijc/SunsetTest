package it.octograve.jmetarparser;

/**
 * Representation of value as a detail of a Metar.
 * 
 * @author Alessandro Rossi
 * 
 * @version 0.1
 */
public class Visibility {

	private static final String PATTERN = "\\s\\d{4}[A-Z]{0,3}\\s";

	private int value;

	private VisibilityMeasure measure;

	private Modifier modifier;

	/**
	 * Visibility measures
	 */
	public enum VisibilityMeasure {

		/** Statue miles */
		SM,

		/** Meters */
		METERS,

		/** Kilometers */
		KILOMETERS;

	}

	/**
	 * Visibility modifier. Suggests when value exceeds any superior or
	 * inferior limit.
	 */
	public enum Modifier {

		/** Less than than the stated amount. */
		LESS_THAN,

		/** Equals to the stated amount. */
		EQUALS_TO,

		/** Greater than the stated amount. */
		MORE_THAN;

	}

	/**
	 * Sole contructor.
	 */
	private Visibility() {
	}

	/**
	 * Returns value value.
	 * 
	 * @return Visibility value.
	 */
	public int getValue() {
		return value;
	}

	private void setValue(int visibility) {
		this.value = visibility;
	}

	/**
	 * Returns value measure.
	 * 
	 * @return Visibility measure.
	 */
	public VisibilityMeasure getMeasure() {
		return measure;
	}

	private void setMeasure(VisibilityMeasure measure) {
		this.measure = measure;
	}

	/**
	 * Returns visbility modifer.
	 * 
	 * @return Visibility modifier.
	 */
	public Modifier getModifier() {
		return modifier;
	}

	private void setModifier(Modifier modifier) {
		this.modifier = modifier;
	}

	/**
	 * Checks if this Visibility is equal to provided object.
	 */
	public boolean equals(Object o) {
		if (o instanceof Visibility) {
			Visibility v = (Visibility) o;
			return v.getValue() == value
					&& v.getMeasure().equals(measure)
					&& v.getModifier().equals(modifier);
		}
		return false;
	}

	/**
	 * String representation of this Visibility.
	 */
	public String toString() {
		return modifier + " " + value + " " + measure;
	}

	/**
	 * Parses a METAR string into a Visibility object to be used as a Metar's
	 * detail.
	 * 
	 * @param metarString
	 *            The METAR string to parse.
	 * @return The parsed Visibility.
	 */
	public static Visibility parseVisibility(String metarString) {
		if (metarString == null) {
			throw new IllegalArgumentException("null string");
		}
		if (metarString.contains("CAVOK")) {
			Visibility visibility = new Visibility();
			visibility.setModifier(Modifier.MORE_THAN);
			visibility.setValue(10);
			visibility.setMeasure(VisibilityMeasure.KILOMETERS);
			return visibility;
		}
		String[] results = Utils.resolveRegex(metarString, PATTERN);
		if (results == null) {
			return null;
		}
		String visibilityString = results[0];
		Visibility visibility = new Visibility();
		if (visibilityString.contains("SM")) {
			visibility.setMeasure(VisibilityMeasure.SM);
			visibility.setModifier(Modifier.EQUALS_TO);
			visibility.setValue(Integer.parseInt(visibilityString
					.substring(0, visibilityString.indexOf("SM"))));
		} else {
			int value = Integer.parseInt(visibilityString);
			if (value == 0) {
				visibility.setMeasure(VisibilityMeasure.METERS);
				visibility.setModifier(Modifier.LESS_THAN);
				visibility.setValue(50);
			} else if (value == 9999) {
				visibility.setMeasure(VisibilityMeasure.KILOMETERS);
				visibility.setModifier(Modifier.MORE_THAN);
				visibility.setValue(10);
			} else {
				if (value <= 10) {
					visibility.setMeasure(VisibilityMeasure.KILOMETERS);
				} else {
					visibility.setMeasure(VisibilityMeasure.METERS);
				}
				visibility.setModifier(Modifier.EQUALS_TO);
				visibility.setValue(value);
			}
		}
		return visibility;
	}

}
