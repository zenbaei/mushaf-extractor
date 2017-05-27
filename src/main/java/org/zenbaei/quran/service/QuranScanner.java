package org.zenbaei.quran.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.zenbaei.quran.domain.Ayah;
import org.zenbaei.quran.domain.AyahRange;
import org.zenbaei.quran.domain.Page;
import org.zenbaei.quran.util.ArabicUtils;
import org.zenbaei.quran.util.NumberUtils;

public class QuranScanner {

	private static final String PAGE_REGEX = ArabicUtils.formRegexWithArabicNumbers(Optional.of("\\S*$"));
	private static final String AYAH_REGEX = ArabicUtils.formRegexWithArabicNumbers(Optional.of("\\S*\\s"));
	private static final String REGEX_LINE_SEPARATOR_UNX_WIN = "(\\r\\n|\\n)\\s";
	private static final String SURAH_LITERAL = "سُورَةُ";
	private static final String SURAH_NAME_REGEX = "\\S+\\s?\\S*(\\r\\n|\\n)";

	private QuranScanner() {}

	/**
	 * Scans mushaf string, splits on page ending and return a list of pages.
	 *
	 * @param content
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
		final Pattern p = Pattern.compile(AYAH_REGEX);
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
			lastAyah.sentence += "\r\n";
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

		return SurahService.extractSurahHeading(lastLine);
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
	 * Return a single {@code AyahRange} if the page contains only ayat from one surah or a list
	 * of AyahRange if the page has more than one surah. It uses {@code QuraanScanner#toAyahs(Page)}.
	 *
	 * It splits page by 'سورة' literal then scan starting and ending ayah range for each splitted block then
	 * for each splitted block it extracts the first word on the first line as the surah name.
	 *
	 * @param content the content of the mushaf page
	 * @return a List of AyahRange
	 */
	public static List<AyahRange> toAyahRanges(final String content) {
		final String[] result =  content.split(SURAH_LITERAL);
		final List<String> surahs = Arrays.stream(result)
				.filter(str -> !str.trim().isEmpty())
				.collect(Collectors.toList());
		final List<AyahRange> ayahRanges = new ArrayList<>();

		if (surahs.isEmpty()) {
			surahs.add(content);
		}

		for (final String splittedPage : surahs) {
			final List<Ayah> ayahs = toAyahs(splittedPage, 0); // page number is not needed here
			AyahRange ay;

			if (ayahs.isEmpty()) {
				ay = new AyahRange(0, 0, Optional.of(splittedPage.trim()));
			} else {
				ay = new AyahRange(ayahs.get(0).enNumber,
						ayahs.get(ayahs.size() - 1).enNumber, extractSurahName(splittedPage));
			}

			ayahRanges.add(ay);
		}
		return ayahRanges;
	}

	/**
	 * It is not a general method. It depends on having the surah name in first line with no "سورة" literal.
	 *
	 * It extracts the first word on the first line.
	 *
	 * @param content
	 * @return
	 */
	private static Optional<String> extractSurahName(final String content) {
		final Pattern pattern = Pattern.compile(SURAH_NAME_REGEX);
		final Matcher matcher = pattern.matcher(content.trim());
		if (matcher.lookingAt()) {
			return Optional.of(matcher.group().trim());
		}
		return Optional.empty();
	}

}
