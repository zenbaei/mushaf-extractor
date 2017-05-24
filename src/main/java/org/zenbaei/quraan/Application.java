package org.zenbaei.quraan;

import java.io.IOException;
import java.util.List;

import org.zenbaei.quraan.all.HibernateService;
import org.zenbaei.quraan.all.QuraanService;
import org.zenbaei.quraan.domain.Surah;
import org.zenbaei.quraan.domain.Token;
import org.zenbaei.quraan.service.TokenService;
import org.zenbaei.quraan.surah.SurahDAO;
import org.zenbaei.quraan.token.TokenDAO;

public class Application {

	public static void main(final String[] args) throws IOException {

		HibernateService.configureSessionFactory();

		//List<Token> tokens = runMushafSppliter();

		//populateDb(tokens);
	}

	public static List<Token> runMushafSppliter() throws IOException {
		List<Token> tokens = QuraanService.parseKalematAlQuraanWord();
		tokens = TokenService.removeSuccessiveLineBreak(tokens);

		tokens.forEach(Token::print);

		return tokens;
	}

	public static void populateDb(final List<Token> tokens) throws IOException {
		//سورة ال عمران تاتي بدون عمران
		final List<Surah> surahs = QuraanService.parseSurahPages();
		surahs.get(2).surah = "سُورَةُ آلِ عِمۡرَانَ";

		TokenDAO.insert(tokens);

		SurahDAO.insert(surahs);

		TokenDAO.updateWrongTokens();

		TokenDAO.insertMissingAyahNumber();

		System.out.println("---------done----------");
	}


}
