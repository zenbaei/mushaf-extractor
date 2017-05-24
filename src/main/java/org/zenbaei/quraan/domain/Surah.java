package org.zenbaei.quraan.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="MUSHAF_SURAHS")
public class Surah {

	@Id
	public int id;
	public String surah;
	public int pageNumber;
	
	public Surah() {}
	
	public Surah(String surah, int id, int pageNumber) {
		this.surah = surah;
		this.id = id;
		this.pageNumber = pageNumber;
	}

	public void print(){
		System.out.println(toString());
	}
	
	@Override
	public String toString() {
		return "Surah [surah=" + surah + ", id=" + id + ", pageNumber=" + pageNumber + "]";
	}
}
