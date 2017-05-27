package org.zenbaei.quran.service;

import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.zenbaei.quran.all.Constants;
import org.zenbaei.quran.domain.Page;

/**
 * Extracts from mushaf doc to text file, each file represents a page from the document.
 *
 * @author zenbaei
 *
 */
public class QuranExtractor {

	private QuranExtractor() {}

	public static void main(final String[] args) {
		writePagesToTextFiles(StandardOpenOption.CREATE);
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
	public static void writePagesToTextFiles(final OpenOption openOption) {
		final String content = QuranReader.asString(Constants.QURAN_MODIFIED_DOC_FILE_PATH);
		final List<Page> pages = QuranScanner.toPages(content);
		pages.forEach(pg -> {
			final String dir = Constants.QURAN_EXTRACTED_FILES_BASE_PATH + pg.number + "/";
			Writer.createDirectory(dir);
			final String filePath = dir + pg.number + ".page";
			Writer.write(filePath, pg.content.trim(), openOption);
		});
	}
}
