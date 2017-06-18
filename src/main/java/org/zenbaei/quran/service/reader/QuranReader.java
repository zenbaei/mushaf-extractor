package org.zenbaei.quran.service.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

public class QuranReader {

	/**
	 * Reads mushaf word document and appends it to a string.
	 *
	 * @param filePath the Word document file path
	 * @return String representing mushaf content
	 * @throws UncheckedIOException
	 */
	public static String asString(final String filePath) {
		final File file = new File(filePath);
		final StringBuilder content = new StringBuilder();

		try {
			final FileInputStream in = new FileInputStream(file.getAbsolutePath());
			final HWPFDocument document = new HWPFDocument(in);
			final WordExtractor extractor = new WordExtractor(document);
			content.append(extractor.getText());
			extractor.close();
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}

		return content.toString();
	}

}
