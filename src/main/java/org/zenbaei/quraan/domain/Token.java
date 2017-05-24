package org.zenbaei.quraan.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="MUSHAF_TOKENS")
public class Token {
	@Id
	public int _id;
	public int pageNumber;
	public int ayahNumber;
	public String token;
	public String tokenAbstracted;
	public String tafsir;
	
	public Token() {}
	
	public Token(int pageNumber, int ayahNumber, String token) {
		this.pageNumber = pageNumber;
		this.ayahNumber = ayahNumber;
		this.token = token;
	}
	
	public void print(){
		System.out.println(toString());
	}

	@Override
	public String toString() {
		return "Token [pageNumber=" + pageNumber + ", ayahNumber=" + ayahNumber + ", token=" + token
				+ ", tokenAbstracted=" + tokenAbstracted + "]";
	}
}
