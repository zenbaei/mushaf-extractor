package org.zenbaei.core.file.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * General file writer service.
 *
 * @author zenbaei
 *
 */
public class FileWriter {

	private static final Logger LOG = LoggerFactory.getLogger(FileWriter.class.getName());

	private FileWriter() {
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
		final Path path = Paths.get(filePath);
		final OpenOption option =
				(openOption == CustomOpenOption.OVERRIDE) ? StandardOpenOption.CREATE_NEW : openOption;

		try {
			if (openOption == CustomOpenOption.OVERRIDE) {
				LOG.info("Delete file [{}]", path);
				Files.delete(path);
			}

			LOG.info("Write file [{}] , file open option [{}]", filePath, openOption);

			try ( final BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"), option) ) {
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

		if (Files.exists(path)) {
			LOG.warn("Directory already exists");
			return;
		}

		LOG.info("Create directory [{}]", dir);

		try {
			Files.createDirectories(path);
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}
	}

}
