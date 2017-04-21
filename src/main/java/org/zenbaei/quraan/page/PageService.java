package org.zenbaei.quraan.page;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.zenbaei.quraan.ayah.AyahService;

public class PageService {

	static int counter = 1;

	public static List<Page> splitByPages(final FileInputStream in) throws IOException {

		final String suffix = "\\S*$";
		final String arabicNumberRegEx = AyahService.formRegexWithArabicNumbers(suffix);
		final Pattern pt = Pattern.compile(arabicNumberRegEx);

		final HWPFDocument document = new HWPFDocument(in);

		final WordExtractor extractor = new WordExtractor(document);

		final String content = extractor.getText();

		extractor.close();

		final String[] fileData = content.split("\\r\\n\\s");

		final List<Page> pages = new ArrayList<>();

		for(String data : fileData){
			if(counter == 604)
				break;

			//بعض السور في اخر ايه ما بيكون مسافة بعد رقم الآية
			final Matcher m = pt.matcher(data);
			if(m.find()){
				final String number = m.group();
				data = m.replaceFirst(number + " ");
			}
			final Page p = new Page(counter, data);
			pages.add(p);
			counter++;
		}

		return pages;
	}

}
