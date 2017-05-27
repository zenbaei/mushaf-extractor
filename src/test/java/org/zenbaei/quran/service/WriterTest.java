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

import org.junit.Test;
import org.zenbaei.quran.BaseTest;
import org.zenbaei.quran.service.Writer;

public class WriterTest extends BaseTest {

	@Test
	public void testWriteFile() throws IOException {
		final String filePath = TEST_DIR + "2.txt";
		final String content = "hello";

		Writer.write(filePath, content, StandardOpenOption.CREATE);

		final Path path = Paths.get(filePath);
		final BufferedReader reader = Files.newBufferedReader(path);
		final StringBuilder newFileContent = new StringBuilder();
		String line = "";

		while (( line = reader.readLine()) != null) {
			newFileContent.append(line);
		}

		assertThat(newFileContent.toString(), is(equalTo(content)));
	}

	@Test
	public void testCreateDirectory() {
		final String dir = TEST_DIR + "/1";
		Writer.createDirectory(dir);
		assertThat(Files.exists(Paths.get(dir)), is(true));
	}

}
