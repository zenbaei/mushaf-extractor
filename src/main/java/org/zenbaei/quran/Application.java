package org.zenbaei.quran;

import java.io.IOException;
import java.util.List;

import org.zenbaei.quran.all.HibernateService;
import org.zenbaei.quran.all.QuranService;
import org.zenbaei.quran.domain.Surah;
import org.zenbaei.quran.domain.Token;
import org.zenbaei.quran.service.TokenService;
import org.zenbaei.quran.surah.SurahDAO;
import org.zenbaei.quran.token.TokenDAO;

public class Application {

	public static void main(final String[] args) throws IOException {

		HibernateService.configureSessionFactory();

		//List<Token> tokens = runMushafSppliter();

		//populateDb(tokens);
	}

	public static List<Token> runMushafSppliter() throws IOException {
		List<Token> tokens = QuranService.parseKalematAlQuraanWord();
		tokens = TokenService.removeSuccessiveLineBreak(tokens);

		tokens.forEach(Token::print);

		return tokens;
	}

	public static void populateDb(final List<Token> tokens) throws IOException {
		//سورة ال عمران تاتي بدون عمران
		final List<Surah> surahs = QuranService.parseSurahPages();
		surahs.get(2).surah = "سُورَةُ آلِ عِمۡرَانَ";

		TokenDAO.insert(tokens);

		SurahDAO.insert(surahs);

		TokenDAO.updateWrongTokens();

		TokenDAO.insertMissingAyahNumber();

		System.out.println("---------done----------");
	}


}
