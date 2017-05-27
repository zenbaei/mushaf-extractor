package org.zenbaei.quran.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Writes Quraan to file system.
 *
 * @author zenbaei
 *
 */
public class Writer {

	private static final Logger LOG = LoggerFactory.getLogger(Writer.class.getName());

	private Writer() {
	}

	/**
	 * Writes page content under a given directory with file name equal to page number.
	 *
	 * @param page {@code Page} page.number is used as file name
	 * @param directory the directory to save the file into
	 * @param override if set true and file exists then file will be deleted then recreated
	 * @throws UncheckedIOException
	 */
	public static void write(final String filePath, final String content, final OpenOption openOption) {
		LOG.info("Writing file [{}] , file open option [{}]", filePath, openOption);
		final Path path = Paths.get(filePath);
		try {
			try ( final BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"), openOption) ) {
				writer.write(content);
			}
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}


	}

	/**
	 * Creates a directory if not exists, otherwise does nothing.
	 *
	 * @param dir
	 *
	 * @throws UncheckedIOException
	 */
	public static void createDirectory(final String dir) {
		final Path path = Paths.get(dir);
		if (Files.notExists(path)) {
			try {
				Files.createDirectories(path);
			} catch (final IOException e) {
				throw new UncheckedIOException(e);
			}
		}
	}



}
