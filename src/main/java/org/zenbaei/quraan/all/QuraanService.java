package org.zenbaei.quraan.all;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.zenbaei.quraan.Application;
import org.zenbaei.quraan.ayah.Ayah;
import org.zenbaei.quraan.ayah.AyahService;
import org.zenbaei.quraan.page.Page;
import org.zenbaei.quraan.page.PageService;
import org.zenbaei.quraan.surah.Surah;
import org.zenbaei.quraan.surah.SurahService;
import org.zenbaei.quraan.token.Token;
import org.zenbaei.quraan.token.TokenService;

public class QuraanService {

	public static List<Page> pages;
	
	static{
		File file = new File(Application.FILE_PATH);
		FileInputStream in;
		try {
			in = new FileInputStream(file.getAbsolutePath());
			pages = PageService.splitByPages(in);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		assertPagesSize();
	}
	
	public static List<Token> parseKalematAlQuraanWord() {
		List<Token> tokensList = new ArrayList<>();
		
		for(Page p : pages){								
			List<Ayah> ayahs = AyahService.splitByAyah(p);
			ayahs.forEach( a -> {
				List<Token> tokens = TokenService.splitByTokens(a);
				tokensList.addAll(tokens);
			});
		}
		return tokensList;
	}
	
	private static void assertPagesSize() {
		if (pages.size() != 603)
			throw new IllegalStateException("Quraan Pages should be 604 but it is: " + (pages.size() + 1));
	}

	public static List<Surah> parseSurahPages(){
		List<Surah> surahs = new ArrayList<>();
		int surahIndex = 1;
		
		for(int i = 0; i < pages.size(); i ++){
			List<String> surList = SurahService.extractMultipleSurahHeading(pages.get(i).content);

			for(String str : surList){
				Surah s = new Surah(str, surahIndex, i + 1);
				surahs.add(s);
				surahIndex ++;
			}
		}
		
		return surahs;
	}
}
