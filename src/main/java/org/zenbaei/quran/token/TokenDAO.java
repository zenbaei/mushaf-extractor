package org.zenbaei.quran.token;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.zenbaei.quran.all.HibernateService;
import org.zenbaei.quran.domain.Token;

public class TokenDAO {

	static int counter = 1;
	
	public static void insert(List<Token> tokens){
		HibernateService.tx( session -> {			
			for(Token t : tokens){
				t._id = counter;
				session.save(t);
				counter ++;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public static List<Token> getByPage(int pageNumber) {
		List<Token> tokens = new ArrayList<>();
		HibernateService.tx( session -> {
			Query query = session.createQuery("from MUSHAF_TOKENS where pageNumber = :pageNumber");
			query.setParameter("pageNumber", pageNumber);
			List<Token> list = query.list();
			tokens.addAll(list);
		});
		return tokens;
	}
	
	public static void insert(Token token){
		HibernateService.tx( session -> {
			session.save(token);
		});
	}
	
	/**
 	 * سورة التين تأتي كلمة "بسم" بزيادة شدة 
 	*/
	public static void updateWrongTokens() {
		HibernateService.tx( session -> {
			String stat = "update mushaf_tokens set token  = (select token from mushaf_tokens where _id = 92496) " +
			"where _id in (92442, 92603)";
			SQLQuery sqlQuery = session.createSQLQuery(stat);
			int result = sqlQuery.executeUpdate();
			assert result == 1;
		});
	}

	/**
 	 * رقم 6 لم يتم ادخاله في سورة الناس	
 	*/
	public static void insertMissingAyahNumber() {
		Token token = new Token();
		token._id = 93431;
		token.ayahNumber = 6;
		token.pageNumber = 604;
		token.token = "٦";
		TokenDAO.insert(token);		
	}
}
