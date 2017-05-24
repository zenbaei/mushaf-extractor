package org.zenbaei.quraan.surah;

import java.util.List;

import org.zenbaei.quraan.all.HibernateService;
import org.zenbaei.quraan.domain.Surah;

public class SurahDAO {

	public static void insert(List<Surah> surahs){
		HibernateService.tx( session -> {			
			for(Surah s : surahs){
				session.save(s);
			}
		});
	}
}
