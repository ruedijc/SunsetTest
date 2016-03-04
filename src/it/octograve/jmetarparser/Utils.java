package it.octograve.jmetarparser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class.
 * 
 * @author Alessandro Rossi
 * 
 * @version 0.1
 */
class Utils {

	/**
	 * Resolves a regular expression and returns a results list.
	 * 
	 * @param toProcess
	 *            The string to process.
	 * @param PATTERN
	 *            The pattern to match.
	 * @return An array of Strings which contains matching substrings.
	 */
	public static String[] resolveRegex(String toProcess, final String PATTERN) {
		Pattern pattern = Pattern.compile(PATTERN);
		Matcher matcher = pattern.matcher(toProcess);
		ArrayList<String> results = new ArrayList<String>();
		while (matcher.find()) {
			results.add(matcher.group().trim());
		}
		if (results.isEmpty()) {
			return null;
		}
		return results.toArray(new String[results.size()]);
	}

}
