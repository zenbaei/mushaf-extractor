package org.zenbaei.quran.token;

import java.util.List;

import org.zenbaei.quran.domain.Token;

public class TokenDAO {

	static int counter = 1;

	/**
 	 * سورة التين تأتي كلمة "بسم" بزيادة شدة
 	*/
	public static void updateWrongTokens() {
		/*
		HibernateService.tx( session -> {
			String stat = "update mushaf_tokens set token  = (select token from mushaf_tokens where _id = 92496) " +
			"where _id in (92442, 92603)";
			SQLQuery sqlQuery = session.createSQLQuery(stat);
			int result = sqlQuery.executeUpdate();
			assert result == 1;
		});*/
	}

	/**
 	 * رقم 6 لم يتم ادخاله في سورة الناس
 	*/
	public static void insertMissingAyahNumber() {
		final Token token = new Token();
		token._id = 93431;
		token.ayahNumber = 6;
		token.pageNumber = 604;
		token.token = "٦";
	}

	public static List<Token> getByPage(final int i) {
		// TODO Auto-generated method stub
		return null;
	}
}
