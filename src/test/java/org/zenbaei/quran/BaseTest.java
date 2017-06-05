package org.zenbaei.quran;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseTest {

	protected final Logger LOG = LoggerFactory.getLogger(getClass());
	private static final String TEST_DIR = "target/junit/";
	private static final Path TEST_DIR_PATH = Paths.get(TEST_DIR);

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

	protected String appendToTestDir(final String fileName) {
		return TEST_DIR + fileName;
	}

}
