package org.zenbaei.quran.service.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.zenbaei.quran.domain.QuranPageMetadata;
import org.zenbaei.quran.domain.SurahIndex;

/**
 * Holds optional methods that acts on results from {@code QuranScanner}.
 *
 * @author zenbaei
 *
 */
public class QuranParserHelper {

	private QuranParserHelper() {
	}

	/**
	 * Loops on the List and fill empty surah name from last used surah name.
	 *
	 * <p>
	 * While looping it keeps the current surah name and checks on next loop if
	 * surah name is empty it assign it the current surah name.
	 * </p>
	 *
	 * @param metadataList
	 * @return a new List with no empty surah name
	 */
	public static List<QuranPageMetadata> fillEmptySurahNameFromPreviousOne(final List<QuranPageMetadata> metadataList) {
		final StringBuilder currentSurah = new StringBuilder();
		return metadataList.stream()
				.map(metadata -> {
					if (StringUtils.isNotEmpty(metadata.surahName)) {
						currentSurah.delete(0, currentSurah.length());
						currentSurah.append(metadata.surahName);
						return metadata;
					} else {
						return new QuranPageMetadata(metadata.fromAyah, metadata.toAyah, currentSurah.toString());
					}
				}).collect(Collectors.toList());
	}

	/**
	 * Iterates over SurahIndex list and return surahName with its order.
	 * <p>
	 * Surah order is determined based on SurahIndex order in list which is expected to be extracted
	 * in right order from quran page.
	 * </p>
	 *
	 * @param surahIndexes
	 * @return {@link HashMap}
	 *
	 * @see QuranParser#toSurahIndex(List)
	 */
	public static Map<String, Integer> toSurahOrderMap(final List<SurahIndex> surahIndexes) {
		final AtomicInteger counter = new AtomicInteger(0);
		return surahIndexes.stream().collect(
				Collectors.toMap(
						si -> si.surahName,
						si -> counter.incrementAndGet())
				);
	}

}
