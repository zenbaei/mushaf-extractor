package org.zenbaei.quran.all;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zenbaei.quran.domain.Ayah;
import org.zenbaei.quran.domain.Page;
import org.zenbaei.quran.domain.Surah;
import org.zenbaei.quran.domain.Token;
import org.zenbaei.quran.service.SurahService;
import org.zenbaei.quran.service.TokenService;
import org.zenbaei.quran.service.quran.parser.QuranParser;
import org.zenbaei.quran.service.quran.reader.QuranReader;

public class QuranService {

	public static final List<Page> pages = QuranParser.toPages( QuranReader.asString(Constants.QURAN_MODIFIED_DOC_FILE_PATH) );
	private static final Logger LOG = LoggerFactory.getLogger(QuranService.class.getName());

	static{
		assertPagesSize();
	}

	public static List<Token> parseKalematAlQuraanWord() {
		final List<Token> tokensList = new ArrayList<>();

		for(final Page p : pages){
			final List<Ayah> ayahs = QuranParser.toAyahs(p.content, p.number);
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
