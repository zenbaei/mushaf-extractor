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
	
	public static List<Page> splitByPages(FileInputStream in) throws IOException {
		String suffix = "\\S*$";
		String arabicNumberRegEx = AyahService.formRegexWithArabicNumbers(suffix);
		Pattern pt = Pattern.compile(arabicNumberRegEx);
		
		HWPFDocument document = new HWPFDocument(in);

		WordExtractor extractor = new WordExtractor(document);
		
		String content = extractor.getText();
		
		extractor.close();
		
		String[] fileData = content.split("\\r\\n\\s");

		List<Page> pages = new ArrayList<>();
		
		for(String data : fileData){
			if(counter == 604)
				break;
			
			//بعض السور في اخر ايه ما بيكون مسافة بعد رقم الآية
			Matcher m = pt.matcher(data);
			if(m.find()){
				String number = m.group();
				data = m.replaceFirst(number + " ");
			}
			Page p = new Page(counter, data);
			pages.add(p);
			counter++;
		}
		
		return pages;
	}

}
