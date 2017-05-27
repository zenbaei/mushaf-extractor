package org.zenbaei.quran.surah;

import java.util.List;

import org.zenbaei.quran.all.HibernateService;
import org.zenbaei.quran.domain.Surah;

public class SurahDAO {

	public static void insert(List<Surah> surahs){
		HibernateService.tx( session -> {			
			for(Surah s : surahs){
				session.save(s);
			}
		});
	}
}
