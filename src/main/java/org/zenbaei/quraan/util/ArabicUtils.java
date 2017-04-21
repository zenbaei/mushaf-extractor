package org.zenbaei.quraan.util;

public class ArabicUtils {

	private static final String[] searchList = {"\u0610", "\u0611", "\u0612", "\u0613",
            "\u0614", "\u0615", "\u0616", "\u0617", "\u0618", "\u0619", "\u061A", "\u06D6", "\u06D7",
            "\u06D8", "\u06D9", "\u06DA", "\u06DB", "\u06DC", "\u06DD", "\u06DE", "\u06DF", "\u06E0",
            "\u06E1", "\u06E2", "\u06E3", "\u06E4", "\u06E5", "\u06E6", "\u06E7", "\u06E8", "\u06E9",
            "\u06EA", "\u06EB", "\u06EC", "\u06ED", "\u0640", "\u064B", "\u064C", "\u064D", "\u064E",
            "\u064F", "\u0650", "\u0651", "\u0652", "\u0653", "\u0654", "\u0655", "\u0656", "\u0657",
            "\u0658", "\u0659", "\u065A", "\u065B", "\u065C", "\u065D", "\u065E", "\u065F", "\u0670"};


    private static final String EMPTY_STRING = "";

    public static String normalizeEnhanced(String input) {

        for (final String s : searchList) {
            if (input.contains(s)) {
                input = input.replace(s, EMPTY_STRING);
            }
        }

        return input;
    }

	/**
	 * normalize Method
	 *
	 * @return String
	 */
	public static String normalize(String input) {

		// Remove honorific sign
		input = input.replaceAll("\u0610", "");// ARABIC SIGN SALLALLAHOU ALAYHE
												// WA SALLAM
		input = input.replaceAll("\u0611", "");// ARABIC SIGN ALAYHE ASSALLAM
		input = input.replaceAll("\u0612", "");// ARABIC SIGN RAHMATULLAH ALAYHE
		input = input.replaceAll("\u0613", "");// ARABIC SIGN RADI ALLAHOU ANHU
		input = input.replaceAll("\u0614", "");// ARABIC SIGN TAKHALLUS

		// Remove koranic anotation
		input = input.replaceAll("\u0615", "");// ARABIC SMALL HIGH TAH
		input = input.replaceAll("\u0616", "");// ARABIC SMALL HIGH LIGATURE
												// ALEF WITH LAM WITH YEH
		input = input.replaceAll("\u0617", "");// ARABIC SMALL HIGH ZAIN
		input = input.replaceAll("\u0618", "");// ARABIC SMALL FATHA
		input = input.replaceAll("\u0619", "");// ARABIC SMALL DAMMA
		input = input.replaceAll("\u061A", "");// ARABIC SMALL KASRA
		input = input.replaceAll("\u06D6", "");// ARABIC SMALL HIGH LIGATURE SAD
												// WITH LAM WITH ALEF MAKSURA
		input = input.replaceAll("\u06D7", "");// ARABIC SMALL HIGH LIGATURE QAF
												// WITH LAM WITH ALEF MAKSURA
		input = input.replaceAll("\u06D8", "");// ARABIC SMALL HIGH MEEM INITIAL
												// FORM
		input = input.replaceAll("\u06D9", "");// ARABIC SMALL HIGH LAM ALEF
		input = input.replaceAll("\u06DA", "");// ARABIC SMALL HIGH JEEM
		input = input.replaceAll("\u06DB", "");// ARABIC SMALL HIGH THREE DOTS
		input = input.replaceAll("\u06DC", "");// ARABIC SMALL HIGH SEEN
		input = input.replaceAll("\u06DD", "");// ARABIC END OF AYAH
		input = input.replaceAll("\u06DE", "");// ARABIC START OF RUB EL HIZB
		input = input.replaceAll("\u06DF", "");// ARABIC SMALL HIGH ROUNDED ZERO
		input = input.replaceAll("\u06E0", "");// ARABIC SMALL HIGH UPRIGHT
												// RECTANGULAR ZERO
		input = input.replaceAll("\u06E1", "");// ARABIC SMALL HIGH DOTLESS HEAD
												// OF KHAH
		input = input.replaceAll("\u06E2", "");// ARABIC SMALL HIGH MEEM
												// ISOLATED FORM
		input = input.replaceAll("\u06E3", "");// ARABIC SMALL LOW SEEN
		input = input.replaceAll("\u06E4", "");// ARABIC SMALL HIGH MADDA
		input = input.replaceAll("\u06E5", "");// ARABIC SMALL WAW
		input = input.replaceAll("\u06E6", "");// ARABIC SMALL YEH
		input = input.replaceAll("\u06E7", "");// ARABIC SMALL HIGH YEH
		input = input.replaceAll("\u06E8", "");// ARABIC SMALL HIGH NOON
		input = input.replaceAll("\u06E9", "");// ARABIC PLACE OF SAJDAH
		input = input.replaceAll("\u06EA", "");// ARABIC EMPTY CENTRE LOW STOP
		input = input.replaceAll("\u06EB", "");// ARABIC EMPTY CENTRE HIGH STOP
		input = input.replaceAll("\u06EC", "");// ARABIC ROUNDED HIGH STOP WITH
												// FILLED CENTRE
		input = input.replaceAll("\u06ED", "");// ARABIC SMALL LOW MEEM

		// Remove tatweel
		input = input.replaceAll("\u0640", "");

		// Remove tashkeel
		input = input.replaceAll("\u064B", "");// ARABIC FATHATAN
		input = input.replaceAll("\u064C", "");// ARABIC DAMMATAN
		input = input.replaceAll("\u064D", "");// ARABIC KASRATAN
		input = input.replaceAll("\u064E", "");// ARABIC FATHA
		input = input.replaceAll("\u064F", "");// ARABIC DAMMA
		input = input.replaceAll("\u0650", "");// ARABIC KASRA
		input = input.replaceAll("\u0651", "");// ARABIC SHADDA
		input = input.replaceAll("\u0652", "");// ARABIC SUKUN
		input = input.replaceAll("\u0653", "");// ARABIC MADDAH ABOVE
		input = input.replaceAll("\u0654", "");// ARABIC HAMZA ABOVE
		input = input.replaceAll("\u0655", "");// ARABIC HAMZA BELOW
		input = input.replaceAll("\u0656", "");// ARABIC SUBSCRIPT ALEF
		input = input.replaceAll("\u0657", "");// ARABIC INVERTED DAMMA
		input = input.replaceAll("\u0658", "");// ARABIC MARK NOON GHUNNA
		input = input.replaceAll("\u0659", "");// ARABIC ZWARAKAY
		input = input.replaceAll("\u065A", "");// ARABIC VOWEL SIGN SMALL V
												// ABOVE
		input = input.replaceAll("\u065B", "");// ARABIC VOWEL SIGN INVERTED
												// SMALL V ABOVE
		input = input.replaceAll("\u065C", "");// ARABIC VOWEL SIGN DOT BELOW
		input = input.replaceAll("\u065D", "");// ARABIC REVERSED DAMMA
		input = input.replaceAll("\u065E", "");// ARABIC FATHA WITH TWO DOTS
		input = input.replaceAll("\u065F", "");// ARABIC WAVY HAMZA BELOW
		input = input.replaceAll("\u0670", "");// ARABIC LETTER SUPERSCRIPT ALEF

		return input;
	}

}
