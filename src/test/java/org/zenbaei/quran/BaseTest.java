package org.zenbaei.quran;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zenbaei.quran.all.Constants;
import org.zenbaei.quran.domain.Page;
import org.zenbaei.quran.service.QuranReader;
import org.zenbaei.quran.service.QuranScanner;

public abstract class BaseTest {

	protected static final Logger LOG = LoggerFactory.getLogger(BaseTest.class.getName());
	protected static final String TEST_DIR = "target/junit/";
	private static final Path TEST_DIR_PATH = Paths.get(TEST_DIR);
	protected static final List<Page> pages = QuranScanner.toPages( QuranReader.asString(Constants.QURAN_MODIFIED_DOC_FILE_PATH) );

	@BeforeClass
	public static void setup() {
		createTestDirectory();
	}

	private static void createTestDirectory() {
		if (Files.notExists(TEST_DIR_PATH)) {
			try {
				Files.createDirectories(TEST_DIR_PATH);
			} catch (final IOException e) {
				throw new UncheckedIOException(e);
			}
		}
	}

}
