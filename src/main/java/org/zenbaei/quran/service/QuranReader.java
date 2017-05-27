package org.zenbaei.quran.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

public class QuranReader {

	/**
	 * Reads mushaf word doc and splits it by pages.
	 *
	 * @param in
	 * @return {@code List} of {@code Page}.
	 * @throws IOException
	 */
	public static String asString(final String fileName) {
		final File file = new File(fileName);
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
