package org.zenbaei.quraan.all;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.zenbaei.quraan.Application;
import org.zenbaei.quraan.token.Token;
import org.zenbaei.quraan.token.TokenDAO;

public class DisplayingMushafPageTest {


	@Before
	public void init() throws IOException{
		HibernateService.configureSessionFactory();
		Application.populateDb(Application.runMushafSppliter());
	}


	@Test
	public void assertLoadedPageSameAsWordPage(){
		HibernateService.configureSessionFactory();


		for(int page = 0; page < QuraanService.pages.size(); page++){
			final StringBuilder strBuilder = getTokensAsStringBuilder(page);

			final String pageContent = QuraanService.pages.get(page).content;
			final String dbContent =  strBuilder.toString();

			//split content by lines instead of being all one line
			final String[] arr = dbContent.split("\n");

			//add to every end of line \r\n to match the content from word
			String dbContentModified = "";
			for(int i = 0; i < arr.length; i++){
				final String trimmed = arr[i].trim();
				String replaced = trimmed + "";

				/*if(i + 1 == arr.length)//last line (trim will do the same)
					replaced += "";
				else*/

				if(replaced.contains("سُورَةُ")) //سورة الفاتحة
					replaced += "\r\n";
				else
					replaced += " \r\n";

				dbContentModified+= replaced;
			}

			if(!pageContent.trim().equals(dbContentModified.trim())){
				writeToFile("page_" + (page + 1), pageContent);
				writeToFile("db_" + (page + 1), dbContentModified);
			}

			assertEquals(pageContent.trim(), dbContentModified.trim());

			if(page + 1 == 604)
				assertTrue(dbContentModified.contains("سُورَةُ النَّاسِ"));
		}
	}

	private StringBuilder getTokensAsStringBuilder(final int page) {
		final List<Token> tokens = TokenDAO.getByPage(page + 1);
		final StringBuilder strBuilder = new StringBuilder();

		//replace empty rows with \n
		tokens.forEach(t -> {
			if (t.token.isEmpty())
				strBuilder.append("\n");

			strBuilder.append(t.token + " ");
		});
		return strBuilder;
	}

	public static void writeToFile(final String fileName, final String content){
		final Path path = Paths.get("target/mushaf/"+fileName + ".txt", "");
		try {
			final BufferedWriter w = Files.newBufferedWriter(path, StandardOpenOption.CREATE);
			w.write(content);
			w.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
