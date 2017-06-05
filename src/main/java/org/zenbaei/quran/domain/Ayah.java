package org.zenbaei.quran.domain;

public class Ayah {

	public final int pageNumber;
	public final String arNumber;
	public final int enNumber;
	public final String sentence;

	public Ayah(final int pageNumber, final String arNumber, final int enNumber, final String sentence) {
		this.pageNumber = pageNumber;
		this.arNumber = arNumber;
		this.enNumber = enNumber;
		this.sentence = sentence;
	}

	public void print(){
		System.out.println(toString());
	}

	@Override
	public String toString() {
		return "Ayah [arNumber=" + arNumber + ", enNumber=" + enNumber + ", sentence=" + sentence + "]";
	}
}
