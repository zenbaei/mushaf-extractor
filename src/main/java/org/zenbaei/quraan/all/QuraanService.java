package org.zenbaei.quraan.all;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private final Logger logger = LoggerFactory.getLogger(QuraanService.class.getName());

	static{
		final File file = new File(Application.FILE_PATH);
		FileInputStream in;
		try {
			in = new FileInputStream(file.getAbsolutePath());
			pages = PageService.splitByPages(in);
		} catch (final Exception e) {
			throw new IllegalStateException(e);
		}
		assertPagesSize();
	}

	public static List<Token> parseKalematAlQuraanWord() {
		final List<Token> tokensList = new ArrayList<>();

		for(final Page p : pages){
			final List<Ayah> ayahs = AyahService.splitByAyah(p);
			ayahs.forEach( a -> {
				final List<Token> tokens = TokenService.splitByTokens(a);
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
		final List<Surah> surahs = new ArrayList<>();
		int surahIndex = 1;

		for(int i = 0; i < pages.size(); i ++){
			final List<String> surList = SurahService.extractMultipleSurahHeading(pages.get(i).content);

			for(final String str : surList){
				final Surah s = new Surah(str, surahIndex, i + 1);
				surahs.add(s);
				surahIndex ++;
			}
		}

		return surahs;
	}
}
