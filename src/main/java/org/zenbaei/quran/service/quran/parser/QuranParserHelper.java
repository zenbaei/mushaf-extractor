package org.zenbaei.quran.service.quran.parser;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.zenbaei.quran.domain.QuranMetadata;

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
	public static List<QuranMetadata> fillEmptySurahNameFromPreviousOne(final List<QuranMetadata> metadataList) {
		final StringBuilder currentSurah = new StringBuilder();
		return metadataList.stream()
				.map(metadata -> {
					if (StringUtils.isNotEmpty(metadata.surahName)) {
						currentSurah.delete(0, currentSurah.length());
						currentSurah.append(metadata.surahName);
						return metadata;
					} else {
						return new QuranMetadata(metadata.fromAyah, metadata.toAyah, currentSurah.toString());
					}
				}).collect(Collectors.toList());
	}

}
