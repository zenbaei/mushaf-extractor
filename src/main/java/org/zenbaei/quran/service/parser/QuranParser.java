package org.zenbaei.quran.service.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.zenbaei.quran.domain.Ayah;
import org.zenbaei.quran.domain.Page;
import org.zenbaei.quran.domain.QuranPageMetadata;
import org.zenbaei.quran.domain.SurahIndex;
import org.zenbaei.quran.util.ArabicUtils;
import org.zenbaei.quran.util.NumberUtils;

public class QuranParser {

	private static final String PAGE_REGEX = ArabicUtils.formRegexWithArabicNumbers(Optional.of("\\S*$"));
	private static final String AYAH_NUMBER_REGEX = ArabicUtils.formRegexWithArabicNumbers(Optional.of("\\S*\\s"));
	private static final String REGEX_LINE_SEPARATOR_UNX_WIN = "(\\r\\n|\\n)\\s";
	private static final String SURAH_LITERAL = "سُورَةُ";
	private static final String SURAH_NAME_REGEX = "\\S+\\s?\\S*(\\r\\n|\\n)";

	private QuranParser() {}

	/**
	 * Scans mushaf string, splits on pages' ending constructing a {@code Page} per mushaf string page.
	 *
	 * @param content representing mushaf content
	 * @return {@code List} of {@code Page}
	 */
	public static List<Page> toPages(final String content) {
		final Pattern pt = Pattern.compile(PAGE_REGEX);
		final String[] fileData = content.split(REGEX_LINE_SEPARATOR_UNX_WIN);

		final List<Page> pages = new ArrayList<>();
		int counter = 1;

		for (String data : fileData) {
			if (data.isEmpty()) {
				break;
			}

			// بعض السور في اخر ايه ما بيكون مسافة بعد رقم الآية
			final Matcher m = pt.matcher(data);
			if (m.find()) {
				final String number = m.group();
				data = m.replaceFirst(number + " ");
			}
			final Page p = new Page(counter, data);
			pages.add(p);
			counter++;
		}
		return pages;
	}

	/**
	 * Scans mushaf content, splits by single ayah then return a list of them.
	 *
	 * @param content the content of the mushaf page
	 * @param number the number of the mushaf page it is just saved within Ayah object
	 *
	 * @return list of {@code Ayah}
	 */
	public static List<Ayah> toAyahs(final String content, final int number) {
		final Pattern p = Pattern.compile(AYAH_NUMBER_REGEX);
		final Matcher m = p.matcher(content);

		final List<Ayah> ayahs = new ArrayList<>();

		int i = 0;

		while (m.find()) {
			final String ayahNumber = m.group().trim();
			final String ayah = content.substring(i, m.end());

			final int enNumber = NumberUtils.convertFromArabicToEnglish(ayahNumber);

			assertNotMissingAyahWithinSamePage(enNumber, ayahs);

			final Ayah ay = new Ayah(number, ayahNumber, enNumber, ayah.trim());
			i = m.end();
			ayahs.add(ay);
		}

		String surahHeading = "";

		if (!ayahs.isEmpty())
			surahHeading = getSurahHeadingBenethLastAyahIfExists(content, ayahs);

		if (!surahHeading.trim().isEmpty()) {
			final Ayah lastAyah = ayahs.get(ayahs.size() - 1);
			final Ayah modifiedAyah = new Ayah(lastAyah.pageNumber, lastAyah.arNumber,
					lastAyah.enNumber, lastAyah.sentence + "\r\n");
			ayahs.remove(lastAyah);
			ayahs.add(modifiedAyah);
			final Ayah a = new Ayah(number, lastAyah.arNumber, lastAyah.enNumber, surahHeading);
			ayahs.add(a);
		}

		return ayahs;
	}

	private static String getSurahHeadingBenethLastAyahIfExists(final String block, final List<Ayah> ayahs) {
		final Ayah lastAyah = ayahs.get(ayahs.size() - 1);
		final int index = block.indexOf(lastAyah.sentence);
		final String lastLine = block.substring(index);

		if (lastLine.trim().isEmpty())
			return "";

		return extractSurahHeading(lastLine);
	}

	private static void assertNotMissingAyahWithinSamePage(final int currentNumber, final List<Ayah> ayahs) {
		if (!ayahs.isEmpty() && currentNumber > 1) {
			final Ayah a = ayahs.get(ayahs.size() - 1);
			if (a.enNumber != currentNumber - 1) {
				a.print();
				throw new IllegalStateException("Current number is: " + currentNumber);
			}
		}
	}

	/**
	 * Returns the ayah start and end position along with surah name for the given string.
	 *
	 * <p>
	 * It splits the string content by 'سورة' literal then scans the starting and ending ayah range for each splitted block then
	 * for each splitted block it uses the first line as surah name.
	 * </p>
	 *
	 * @param content the string content of the mushaf page
	 *
	 * @return a List of QuranPageMetadata the List will contain a single {@code QuranPageMetadata} if the string contains only ayat from one surah or many
	 * if the string contains more than one surah
	 */
	public static List<QuranPageMetadata> toMetadata(final String content) {
		final String[] result =  content.split(SURAH_LITERAL);
		final List<String> surahs = Arrays.stream(result)
				.filter(str -> !str.trim().isEmpty())
				.collect(Collectors.toList());
		final List<QuranPageMetadata> ayahRanges = new ArrayList<>();

		if (surahs.isEmpty()) { // if no surah on page then add the content in first index
			surahs.add(content);
		}

		for (final String splittedPage : surahs) {
			final List<Ayah> ayahs = toAyahs(splittedPage, 0); // page number is not needed here
			QuranPageMetadata ay;

			if (ayahs.isEmpty()) {
				ay = new QuranPageMetadata(0, 0, splittedPage.trim());
			} else {
				ay = new QuranPageMetadata(ayahs.get(0).enNumber,
						ayahs.get(ayahs.size() - 1).enNumber, extractSurahName(splittedPage));
			}

			ayahRanges.add(ay);
		}
		return ayahRanges;
	}

	/**
	 * Parses the Page content to extract surah name and page number.
	 *
	 * <p>
	 * Pages with no surah will be ignored and Pages with surah as last line
	 * will have their page number incremented by one.
	 * </p>
	 *
	 * @param pages
	 *
	 * @return {@code List<SurahIndex>}
	 */
	public static List<SurahIndex> toSurahIndex(final List<Page> pages) {
		final List<SurahIndex> list = new ArrayList<>();
		pages.forEach(pg ->
			QuranParser.toMetadata(pg.content)
					.stream()
					.filter(metadata -> StringUtils.isNotEmpty(metadata.surahName))
					.forEach(metadata -> {
						int pageNumber = pg.number;
						if (metadata.fromAyah == 0) { // surah as last line
							pageNumber++;
						}
						list.add( new SurahIndex(metadata.surahName, pageNumber) );
					})
				);
		return list;
	}

	/**
	 * It is not a general method. It depends on having the surah name in first line with no "سورة" literal.
	 *
	 * <p>
	 * It extracts the first word on the first line based on a regex.
	 * </p>
	 *
	 * @param content
	 *
	 * @return String as surah name or empty String if no matching
	 */
	private static String extractSurahName(final String content) {
		final Pattern pattern = Pattern.compile(SURAH_NAME_REGEX);
		final Matcher matcher = pattern.matcher(content.trim());
		if (matcher.lookingAt()) {
			return matcher.group().trim();
		}
		return "";
	}

	public static String extractSurahHeading(final String block) {
		String regex = "\\s\\S+";//\\s"; // space then non space(one or more) then space
		regex = "سُورَةُ" + regex;

		final Pattern p = Pattern.compile(regex);
		final Matcher m = p.matcher(block);

		if (!m.find())
			return "";

		final String surah = m.group() + " "; //added space for split by ayah to recognize the second token of surah
		//	surah = StringUtils.remove(surah, (char) 11);

		return surah;
		// StringUtils.removePattern(page.block, p.pattern());
	}

	/**
	 * Go over the existing {@code QuranPageMetadata#surahName} and gets the last one if exists.
	 *
	 * @param metadataList
	 * @return
	 */
	public static Optional<String> getLastSurahName(List<QuranPageMetadata> metadataList) {
		List<String> surahs = metadataList
								.stream()
								.filter(meta -> StringUtils.isNotEmpty(meta.surahName))
								.map(meta -> meta.surahName)
								.collect(Collectors.toList());
		return surahs.isEmpty() ? Optional.empty() : Optional.of(surahs.get(surahs.size() -1));
	}

	/**
	 * For some reasons! when the quran content extracted file is used in javascript (quran ionic app) the number are displayed reversed.
	 * @param quranPageContent
	 * @return quranPageContent with number reversed
	 */
	public static String switchArabicNumbers(String quranPageContent) {
		Matcher m = Pattern.compile(AYAH_NUMBER_REGEX).matcher(quranPageContent);
		while (m.find()) {
			String machedStr = m.group().trim();
			String reversedArabicNumbers = reverseArabicNumbers(machedStr);
			quranPageContent = quranPageContent.replace(machedStr, reversedArabicNumbers);
		}
		return quranPageContent;
	}

	/**
	 * Search the content for Arabic numbers and reverse them.
	 * @param str
	 * @return
	 */
	protected static String reverseArabicNumbers(String str) {
		List<String> charList = Arrays.asList(str.split("|"));
		Collections.reverse(charList);
		return charList.stream()
				.reduce(String::concat).get();
	}

}
