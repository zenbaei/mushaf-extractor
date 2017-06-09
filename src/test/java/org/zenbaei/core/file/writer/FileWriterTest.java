package org.zenbaei.core.file.writer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.zenbaei.core.file.writer.CustomOpenOption;
import org.zenbaei.core.file.writer.FileWriter;
import org.zenbaei.quran.BaseTest;

public class FileWriterTest extends BaseTest {

	@Test
	public void testWriteFile() throws IOException {
		final String fileName = RandomStringUtils.random(5);
		final String filePath = appendToTestDir(fileName);
		final String content = "hello";

		FileWriter.write(filePath, content, StandardOpenOption.CREATE);

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
		final String dirName = RandomStringUtils.random(1);
		final String dir = appendToTestDir(dirName);
		FileWriter.createDirectory(dir);
		assertThat(Files.exists(Paths.get(dir)), is(true));
	}

	@Test
	public void test_write_with_override_option_should_succeed_with_CREATE_NEW() throws IOException {
		final String fileName = RandomStringUtils.random(5);
		final String filePath = appendToTestDir(fileName);
		final BufferedWriter bw = Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE_NEW);
		bw.write("hello");
		bw.close();
		FileWriter.write(filePath, "overridden", CustomOpenOption.OVERRIDE);
		final BufferedReader reader = Files.newBufferedReader(Paths.get(filePath));
		assertThat(reader.readLine(), is(equalTo("overridden")));
		reader.close();
	}

}
