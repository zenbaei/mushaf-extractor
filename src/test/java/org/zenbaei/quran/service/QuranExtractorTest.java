package org.zenbaei.quran.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.BeforeClass;
import org.junit.Test;
import org.zenbaei.quran.BaseTest;
import org.zenbaei.quran.service.QuranExtractor;

public class QuranExtractorTest extends BaseTest {

	private static final String EXPECTED_OUTPUT_DIR = "src/main/resources/data/";

	@BeforeClass
	public static void setup() {
		extractQuraan();
	}

	private static void extractQuraan() {
		try {
			QuranExtractor.writePagesToTextFiles(StandardOpenOption.CREATE_NEW);
		} catch (final Exception e) {
			LOG.info("Quraan text files have been already extracted");
		}
	}

	@Test
	public void test_extractor_should_output_under_resources_data_dir() throws IOException {
		final Path path = Paths.get(EXPECTED_OUTPUT_DIR + "1/", "1.page");
		assertThat(Files.exists(path), is(true));
	}

	@Test
	public void test_document_pages_cotent_should_equal_to_extracted_text_files_content() throws IOException {
		for (int i = 0; i < pages.size(); i++) {
			final StringBuilder textFileContent = new StringBuilder();
			final int pageNu = i + 1;
			final String dir = EXPECTED_OUTPUT_DIR + pageNu + "/";
			final String file = pageNu + ".page";
			final Path path = Paths.get(dir, file);
			final BufferedReader reader = Files.newBufferedReader(path);
			String line = "";

			while ((line = reader.readLine()) != null) {
				textFileContent.append(line);
			}

			final String originalDoc = pages.get(i).content.replaceAll("\n", "").trim();
			assertThat(textFileContent.toString().trim(), is(equalTo(originalDoc)));
		}
	}
}
