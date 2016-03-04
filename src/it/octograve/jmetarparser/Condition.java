package it.octograve.jmetarparser;

import static it.octograve.jmetarparser.Condition.Descriptor.BLOWING;
import static it.octograve.jmetarparser.Condition.Descriptor.FREEZING;
import static it.octograve.jmetarparser.Condition.Descriptor.LOW_DRIFTING;
import static it.octograve.jmetarparser.Condition.Descriptor.PARTIAL;
import static it.octograve.jmetarparser.Condition.Descriptor.PATCHES;
import static it.octograve.jmetarparser.Condition.Descriptor.SHALLOW;
import static it.octograve.jmetarparser.Condition.Descriptor.SHOWERS;
import static it.octograve.jmetarparser.Condition.Descriptor.THUNDERSTORM;
import static it.octograve.jmetarparser.Condition.Phenomenon.DRIZZLE;
import static it.octograve.jmetarparser.Condition.Phenomenon.DUST_WHIRLS;
import static it.octograve.jmetarparser.Condition.Phenomenon.FOG;
import static it.octograve.jmetarparser.Condition.Phenomenon.FUNNEL_CLOUD;
import static it.octograve.jmetarparser.Condition.Phenomenon.HAIL;
import static it.octograve.jmetarparser.Condition.Phenomenon.HAZE;
import static it.octograve.jmetarparser.Condition.Phenomenon.ICE_CRYSTALS;
import static it.octograve.jmetarparser.Condition.Phenomenon.ICE_PELLETS;
import static it.octograve.jmetarparser.Condition.Phenomenon.MIST;
import static it.octograve.jmetarparser.Condition.Phenomenon.RAIN;
import static it.octograve.jmetarparser.Condition.Phenomenon.SAND;
import static it.octograve.jmetarparser.Condition.Phenomenon.SANDSTORM;
import static it.octograve.jmetarparser.Condition.Phenomenon.SMOKE;
import static it.octograve.jmetarparser.Condition.Phenomenon.SNOW;
import static it.octograve.jmetarparser.Condition.Phenomenon.SNOW_GRAINS;
import static it.octograve.jmetarparser.Condition.Phenomenon.SNOW_PELLETS;
import static it.octograve.jmetarparser.Condition.Phenomenon.SPRAY;
import static it.octograve.jmetarparser.Condition.Phenomenon.SQUALLS;
import static it.octograve.jmetarparser.Condition.Phenomenon.UNKNOWN;
import static it.octograve.jmetarparser.Condition.Phenomenon.VOLCANIC_ASH;
import static it.octograve.jmetarparser.Condition.Phenomenon.WIDEPREAD_DUST;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Representetion of a weather phenomenon as a detail of a METAR observation.
 * 
 * @author Alessandro Rossi
 * 
 * @version 0.1
 */
public class Condition {

	private final static String PATTERN = "[+-]?([A-Z][A-Z])?[A-Z][A-Z]\\s";

	private Intensity intensity;

	private Descriptor descriptor;

	private Phenomenon phenomenon;

	/**
	 * Intensity of the phenomenon.
	 */
	public enum Intensity {

		/** Heavy phenomenon. */
		HEAVY,

		/** Moderate phenomenon. */
		MODERATE,

		/** Loght phenomenon. */
		LIGHT;

	}

	/**
	 * Phenomenon's descriptor.
	 */
	public enum Descriptor {

		/** Shallow. */
		SHALLOW,

		/** Partial. */
		PARTIAL,

		/** Patches. */
		PATCHES,

		/** Low drifting. */
		LOW_DRIFTING,

		/** Blowing. */
		BLOWING,

		/** Showers. */
		SHOWERS,

		/** Thunderstorm. */
		THUNDERSTORM,

		/** Freezing. */
		FREEZING;

	}

	/**
	 * Enumeration of possible phenomena.
	 */
	public enum Phenomenon {

		/** Drizzle. */
		DRIZZLE,

		/** Rain. */
		RAIN,

		/** Snow. */
		SNOW,

		/** Snow grains. */
		SNOW_GRAINS,

		/** Ice crystals. */
		ICE_CRYSTALS,

		/** Ice pellets. */
		ICE_PELLETS,

		/** Hail. */
		HAIL,

		/** Snow pellets. */
		SNOW_PELLETS,

		/** Mist. */
		MIST,

		/** Fog. */
		FOG,

		/** Smoke. */
		SMOKE,

		/** Volcanic ash. */
		VOLCANIC_ASH,

		/** Widespread dust. */
		WIDEPREAD_DUST,

		/** Sand. */
		SAND,

		/** Haze. */
		HAZE,

		/** Spray. */
		SPRAY,

		/** Dust whirls. */
		DUST_WHIRLS,

		/** Squalls. */
		SQUALLS,

		/** Funnel cloud(s). */
		FUNNEL_CLOUD,

		/** Sandstorm. */
		SANDSTORM,

		/** Unknown phenomenon. */
		UNKNOWN;

	}

	/** Association table for descriptors. */
	private static Hashtable<String, Descriptor> descriptorTable = new Hashtable<String, Descriptor>();

	/** Association table for phenomena. */
	private static Hashtable<String, Phenomenon> phenomenaTable = new Hashtable<String, Phenomenon>();

	static {
		descriptorTable.put("MI", SHALLOW);
		descriptorTable.put("PR", PARTIAL);
		descriptorTable.put("BC", PATCHES);
		descriptorTable.put("DR", LOW_DRIFTING);
		descriptorTable.put("BL", BLOWING);
		descriptorTable.put("SH", SHOWERS);
		descriptorTable.put("TS", THUNDERSTORM);
		descriptorTable.put("FZ", FREEZING);

		phenomenaTable.put("DZ", DRIZZLE);
		phenomenaTable.put("RA", RAIN);
		phenomenaTable.put("SN", SNOW);
		phenomenaTable.put("SG", SNOW_GRAINS);
		phenomenaTable.put("IC", ICE_CRYSTALS);
		phenomenaTable.put("PL", ICE_PELLETS);
		phenomenaTable.put("GR", HAIL);
		phenomenaTable.put("GS", SNOW_PELLETS);
		phenomenaTable.put("BR", MIST);
		phenomenaTable.put("FG", FOG);
		phenomenaTable.put("FU", SMOKE);
		phenomenaTable.put("VA", VOLCANIC_ASH);
		phenomenaTable.put("DU", WIDEPREAD_DUST);
		phenomenaTable.put("SA", SAND);
		phenomenaTable.put("HZ", HAZE);
		phenomenaTable.put("PY", SPRAY);
		phenomenaTable.put("PO", DUST_WHIRLS);
		phenomenaTable.put("SQ", SQUALLS);
		phenomenaTable.put("FC", FUNNEL_CLOUD);
		phenomenaTable.put("SS", SANDSTORM);
		phenomenaTable.put("UP", UNKNOWN);
	}

	/**
	 * Sole constructor.
	 */
	private Condition() {
	}

	/**
	 * Returns phenomenon's descriptor.
	 * 
	 * @return Phenomenon's descriptor.
	 */
	public Descriptor getDescriptor() {
		return descriptor;
	}

	private void setDescriptor(Descriptor descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Returns phenomenon's intensity.
	 * 
	 * @return Phenomenon's intensity.
	 */
	public Intensity getIntensity() {
		return intensity;
	}

	private void setIntensity(Intensity intensity) {
		this.intensity = intensity;
	}

	/**
	 * Returns phenomenon type.
	 * 
	 * @return Phenomenon type.
	 */
	public Phenomenon getPhenomenon() {
		return phenomenon;
	}

	private void setPhenomenon(Phenomenon phenomenon) {
		this.phenomenon = phenomenon;
	}

	/**
	 * Checks if this Condition is equal to provided object.
	 */
	public boolean equals(Object o) {
		if (o instanceof Condition) {
			Condition c = (Condition) o;
			return intensity.equals(c.getIntensity())
					&& descriptor.equals(c.getDescriptor())
					&& phenomenon.equals(c.getPhenomenon());
		}
		return false;
	}

	/**
	 * String representation of this Condition.
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(intensity + " ");
		if (descriptor != null) {
			buffer.append(descriptor + " ");
		}
		buffer.append(phenomenon);
		return buffer.toString();
	}

	/**
	 * Parses a METAR string into an ArrayList of Condtions to be used as a
	 * Metar's detail and returns it.
	 * 
	 * @param metarString
	 *            The METAR string to parse.
	 * @return An ArrayList of Conditions.
	 */
	public static ArrayList<Condition> parseConditions(String metarString) {
		if (metarString == null) {
			throw new IllegalArgumentException("null string");
		}
		ArrayList<Condition> conditions = new ArrayList<Condition>();
		String[] results = Utils.resolveRegex(metarString, PATTERN);
		if (results == null) {
			return conditions;
		}
		for (String s : results) {
			Condition c = new Condition();
			if (s.startsWith("+")) {
				c.setIntensity(Intensity.HEAVY);
			} else if (s.startsWith("-")) {
				c.setIntensity(Intensity.LIGHT);
			} else {
				c.setIntensity(Intensity.MODERATE);
			}
			int i = s.indexOf("+") + s.indexOf("-") + 2;
			Descriptor d = null;
			Phenomenon p = null;
			if (s.length() > 2 + i) {
				d = descriptorTable.get(s.substring(i, i + 2));
				p = phenomenaTable.get(s.substring(i + 2, i + 4));
			} else {
				p = phenomenaTable.get(s.substring(i, i + 2));
			}
			if (p == null) {
				continue;
			}
			c.setDescriptor(d);
			c.setPhenomenon(p);
			conditions.add(c);
		}
		return conditions;
	}

}
