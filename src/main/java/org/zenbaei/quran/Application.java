package org.zenbaei.quran;

import java.io.IOException;
import java.util.List;

import org.zenbaei.quran.domain.Token;
import org.zenbaei.quran.token.TokenDAO;

public class Application {


	public static void populateDb(final List<Token> tokens) throws IOException {

		// TokenDAO.insert(tokens);

		// SurahDAO.insert(surahs);

		TokenDAO.updateWrongTokens();

		TokenDAO.insertMissingAyahNumber();

		System.out.println("---------done----------");
	}


}
