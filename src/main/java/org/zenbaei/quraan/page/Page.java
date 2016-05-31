package org.zenbaei.quraan.page;

public class Page {
	public int number;
	public String content;
	
	public Page(int number, String content) {
		this.number = number;
		this.content = content;
	}

	public void print(){
		System.out.println(toString());
	}
	
	@Override
	public String toString() {
		return "Page [number=" + number + ", content=" + content + "]";
	}
}
