package org.zenbaei.quran.service.extractor;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;
import org.zenbaei.quran.BaseTest;
import org.zenbaei.quran.all.Constants;
import org.zenbaei.quran.domain.Page;
import org.zenbaei.quran.service.parser.QuranParser;
import org.zenbaei.quran.service.reader.QuranReader;

public class QuranFileExtractorTest extends BaseTest {

	private static final String EXPECTED_OUTPUT_DIR = "src/main/resources/data/";
	private static final List<Page> PAGES = QuranParser
			.toPages(QuranReader.asString(Constants.QURAN_MODIFIED_DOC_FILE_PATH));

	@Test
	public void test_extractor_should_output_under_resources_data_dir() throws IOException {
		final Path path = Paths.get(EXPECTED_OUTPUT_DIR + "1/", "1.page");
		assertThat(Files.exists(path), is(true));
	}

	@Test
	public void test_document_PAGES_cotent_should_equal_to_extracted_text_files_content() throws IOException {
		for (int i = 0; i < PAGES.size(); i++) {
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

			final String originalDoc = PAGES.get(i).content.replaceAll("\n", "").trim();
			assertThat(textFileContent.toString().trim(), is(equalTo(originalDoc)));
			reader.close();
		}
	}

	@Test
	public void test_extract_quran_metadata_under_the_expected_folders() throws IOException {
		for (int i = 0; i < PAGES.size(); i++) {
			final int pageNu = i + 1;
			final String dir = EXPECTED_OUTPUT_DIR + pageNu + "/";
			final String file = pageNu + ".metadata";
			final Path path = Paths.get(dir, file);
			assertThat(Files.exists(path), is(true));

			final BufferedReader reader = Files.newBufferedReader(path);
			assertThat(reader.readLine().isEmpty(), is(false));
			reader.close();
		}
	}

	@Test
	public void test_quran_metadata_extracted_content() throws IOException {
		final int pageNu = 1;
		final String dir = EXPECTED_OUTPUT_DIR + pageNu + "/";
		final String file = pageNu + ".metadata";
		final Path path = Paths.get(dir, file);

		final String expectedOutput = "[{\"fromAyah\":1,\"toAyah\":7,\"surahOrder\":1}]";
		final BufferedReader reader = Files.newBufferedReader(path);
		assertThat(reader.readLine(), containsString(expectedOutput));
		reader.close();
	}

	@Test
	public void test_extract_quran_index_under_the_expected_folders() throws IOException {
		final String dir = EXPECTED_OUTPUT_DIR;
		final Path path = Paths.get(dir, Constants.QURAN_INDEX_FILE_NAME);
		assertThat(Files.exists(path), is(true));

		final BufferedReader reader = Files.newBufferedReader(path);
		assertThat(reader.readLine().isEmpty(), is(false));
		reader.close();
	}

	@Test
	public void test_quran_index_extracted_content() throws IOException {
		final String expected_string = "{\"surahName\":\"الفَاتِحَةِ\",\"pageNumber\":1}";
		final String dir = EXPECTED_OUTPUT_DIR;
		final Path path = Paths.get(dir, Constants.QURAN_INDEX_FILE_NAME);
		assertThat(Files.exists(path), is(true));

		final BufferedReader reader = Files.newBufferedReader(path);
		final String firstLine = reader.readLine();
		assertThat(firstLine, containsString(expected_string));
		reader.close();
	}

}
