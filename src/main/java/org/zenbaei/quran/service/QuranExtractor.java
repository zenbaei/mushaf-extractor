package org.zenbaei.quran.service;

import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zenbaei.quran.all.Constants;
import org.zenbaei.quran.domain.AyahRange;
import org.zenbaei.quran.domain.Page;

import com.google.gson.Gson;

/**
 * Extracts from mushaf doc to text file, each file represents a page from the document.
 *
 * @author zenbaei
 *
 */
public class QuranExtractor {

	private static final Logger LOG = LoggerFactory.getLogger(QuranExtractor.class);
	private static final List<Page> pages = QuranScanner.toPages( QuranReader.asString(Constants.QURAN_MODIFIED_DOC_FILE_PATH) );
	private static final Gson gson = new Gson();

	private QuranExtractor() {}

	public static void main(final String[] args) {
		writeContentPerQuranPage(StandardOpenOption.CREATE_NEW);
	}

	/**
	 * Extracts files under from mushaf doc under resources data directory.
	 *
	 * It creates a directory with the page number then a file with page number and '.page'
	 * as extension.
	 *
	 * @param openOption
	 *
	 * @see StandardOpenOption
	 */
	public static void writeContentPerQuranPage(final OpenOption openOption) {
		write(".page", openOption, pg -> pg.content);
	}

	/**
	 * Writes a file with extension '.metadata' and the page number as name under every page folder. The file contains info about that page.
	 *
	 * @param openOption the file creation option
	 *
	 */
	public static void writeMetadataPerQuranPage(final OpenOption openOption) {
		write(".metadata", openOption, pg -> {
			final List<AyahRange> ranges = QuranScanner.toAyahRanges(pg.content);
			fillEmptySurahNameFromPreviousOne(ranges);
			final String asJson = gson.toJson(ranges);
			LOG.debug(asJson);
			return asJson;
		});
	}

	public static void writeQuranIndex(final OpenOption openOption) {
		final String dir = Constants.QURAN_EXTRACTED_FILES_BASE_PATH;
		Writer.createDirectory(dir);
		pages.forEach(pg -> {

		});
	}

	private static void fillEmptySurahNameFromPreviousOne(final List<AyahRange> ranges) {
		final StringBuilder currentSurah = new StringBuilder();
		ranges.forEach(rg -> {
			if (StringUtils.isNotEmpty(rg.surahName)) {
				currentSurah.delete(0, currentSurah.length());
				currentSurah.append(rg.surahName);
			} else {
				rg.surahName = currentSurah.toString();
			}
		});
	}

	private static void write(final String fileExtension, final OpenOption openOption, final Function<Page, String> function) {
		pages.forEach(pg -> {
			final String dir = Constants.QURAN_EXTRACTED_FILES_BASE_PATH + pg.number + "/";
			Writer.createDirectory(dir);
			final String filePath = dir + pg.number + fileExtension;
			Writer.write(filePath, function.apply(pg), openOption);
		});
	}


}
