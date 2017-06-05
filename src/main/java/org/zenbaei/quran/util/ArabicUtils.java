package org.zenbaei.quran.util;

import java.util.Optional;

public class ArabicUtils {

	private static final String[] TASHKIL_CHARACTERS = {"\u0610", "\u0611", "\u0612", "\u0613",
            "\u0614", "\u0615", "\u0616", "\u0617", "\u0618", "\u0619", "\u061A", "\u06D6", "\u06D7",
            "\u06D8", "\u06D9", "\u06DA", "\u06DB", "\u06DC", "\u06DD", "\u06DE", "\u06DF", "\u06E0",
            "\u06E1", "\u06E2", "\u06E3", "\u06E4", "\u06E5", "\u06E6", "\u06E7", "\u06E8", "\u06E9",
            "\u06EA", "\u06EB", "\u06EC", "\u06ED", "\u0640", "\u064B", "\u064C", "\u064D", "\u064E",
            "\u064F", "\u0650", "\u0651", "\u0652", "\u0653", "\u0654", "\u0655", "\u0656", "\u0657",
            "\u0658", "\u0659", "\u065A", "\u065B", "\u065C", "\u065D", "\u065E", "\u065F", "\u0670"};
    private static final String EMPTY_STRING = "";
    private static final String OR = "|";


    /**
     * Remove tashkil from arabic word.
     *
     * @param input
     * @return
     */
    public static String removeTashkil(String input) {
        for (final String s : TASHKIL_CHARACTERS) {
            if (input.contains(s)) {
                input =  input.replace(s, EMPTY_STRING);
            }
        }
        return input;
    }

    /**
     * Creates a string representing arabic numbers in unicode characters separated by regex '|' operator.
     *
     * @param suffix to optionally append a regex expression after the arabic number
     * @return
     */
    public static String formRegexWithArabicNumbers(final Optional<String> suffixRegex) {
		// 1632 - 1641
		final StringBuilder regex = new StringBuilder();

		for(int i = 1632; i <= 1641; i++){
			if(regex.length() > 0) {
				regex.append(OR);
			}
			// casting to char will convert the unicode int number to the arabic character one ١٢..
			regex.append((char) i);
			suffixRegex.ifPresent(regex::append);
		}
		return regex.toString();
	}
}
