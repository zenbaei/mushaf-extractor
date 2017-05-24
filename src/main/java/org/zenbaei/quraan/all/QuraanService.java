package org.zenbaei.quraan.all;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zenbaei.quraan.domain.Ayah;
import org.zenbaei.quraan.domain.Page;
import org.zenbaei.quraan.domain.Surah;
import org.zenbaei.quraan.domain.Token;
import org.zenbaei.quraan.service.QuraanReader;
import org.zenbaei.quraan.service.QuraanScanner;
import org.zenbaei.quraan.service.SurahService;
import org.zenbaei.quraan.service.TokenService;

public class QuraanService {

	public static final List<Page> pages = QuraanScanner.toPages( QuraanReader.asString(Constants.MODIFIED_MUSHAF_FILE_PATH) );
	private static final Logger LOG = LoggerFactory.getLogger(QuraanService.class.getName());

	static{
		assertPagesSize();
	}

	public static List<Token> parseKalematAlQuraanWord() {
		final List<Token> tokensList = new ArrayList<>();

		for(final Page p : pages){
			final List<Ayah> ayahs = QuraanScanner.toAyahs(p);
			ayahs.forEach( a -> {
				final List<Token> tokens = TokenService.splitByTokens(a);
				tokensList.addAll(tokens);
			});
		}
		return tokensList;
	}

	private static void assertPagesSize() {
		if (pages.size() != 604)
			throw new IllegalStateException("Quraan Pages should be 604 but it is: " + pages.size());
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
