package org.zenbaei.quraan.domain;

public class Ayah {

	public int pageNumber;
	public String arNumber;
	public int enNumber;
	public String sentence;
	
	public Ayah(int pageNumber, String arNumber, int enNumber, String sentence) {
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
