package org.zenbaei.quran;

import java.io.UncheckedIOException;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zenbaei.quran.service.extractor.QuranFileExtractor;

/**
 * The application start point.
 *
 * @author zenbaei
 *
 */
public class Application {

	private static final QuranFileExtractor quranFileWriterImpl = QuranFileExtractor.getInstance();
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	private static final OpenOption OPEN_OPTION = StandardOpenOption.CREATE_NEW;

	private Application() {}

	public static void main(final String[] args) {
		extract();
	}


	private static void extract() {
		try {
			quranFileWriterImpl.extractContentPerQuranPage(OPEN_OPTION);
		} catch (final UncheckedIOException ex) {
			LOG.debug("Quran Pages already extracted");
		}
		try {
			quranFileWriterImpl.extractMetadataPerQuranPage(OPEN_OPTION);
		} catch (final UncheckedIOException ex) {
			LOG.debug("Quran metadata already extracted");
		}
		try {
			quranFileWriterImpl.extractQuranIndex(OPEN_OPTION);
		} catch (final UncheckedIOException ex) {
			LOG.debug("Quran index already extracted");
		}
	}


}
