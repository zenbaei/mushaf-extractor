package org.zenbaei.quraan.ayah;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zenbaei.quraan.page.Page;
import org.zenbaei.quraan.surah.SurahService;
import org.zenbaei.quraan.utils.NumberUtils;

public class AyahService {
	
	public static List<Ayah> splitByAyah(Page page) {
		String suffix = "\\S*\\s";
		String regex = formRegexWithArabicNumbers(suffix);
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(page.content);
		
		List<Ayah> ayahs = new ArrayList<>();
		
		int i = 0;
		
		while(m.find()){
			String ayahNumber = m.group().trim();
			String ayah = page.content.substring(i, m.end());
			ayah.trim();
			
			int enNumber = NumberUtils.convertFromArabicToEnglish(ayahNumber);
			
			assertNotMissingAyahWithinSamePage(enNumber, ayahs);
			
			Ayah ay = new Ayah(page.number, ayahNumber, enNumber, ayah);
			i = m.end();
			ayahs.add(ay);
		}
		
		
		String surahHeading = "";
		
		if(!ayahs.isEmpty())
			surahHeading = getSurahHeadingBenethLastAyahIfExists(page.content, ayahs);
		
		if(!surahHeading.trim().isEmpty()){
			Ayah lastAyah = ayahs.get(ayahs.size() -1 );
			lastAyah.sentence += "\r\n";
			Ayah a = new Ayah(page.number, lastAyah.arNumber, lastAyah.enNumber, surahHeading);
			ayahs.add(a);
		}
		
		return ayahs;
	}
	
	private static String getSurahHeadingBenethLastAyahIfExists(String block, List<Ayah> ayahs) {
		Ayah lastAyah = ayahs.get(ayahs.size() -1 );
		int index = block.indexOf(lastAyah.sentence);
		String lastLine = block.substring(index);
		
		if(lastLine.trim().isEmpty())
			return "";
		
		return SurahService.extractSurahHeading(lastLine);
	}

	private static void assertNotMissingAyahWithinSamePage(int currentNumber, List<Ayah> ayahs) {
		if(!ayahs.isEmpty() && currentNumber > 1){
			Ayah a = ayahs.get(ayahs.size() - 1);
			if(a.enNumber != currentNumber - 1){
				a.print();
				throw new IllegalStateException("Current number is: " + currentNumber);
			}
				
		}
	}

	public static String formRegexWithArabicNumbers(String suffix) {
		// 1632 - 1641
		String regex = null;
		
		for(int i = 1632; i <= 1641; i++){
			if(regex != null)
				regex += "|";
			regex += (char) i +  suffix;
		}
		return regex;
	}
	
	

}
