package org.zenbaei.quran.domain;

/**
 * Holds page number and content.
 *
 * @author zenbaei
 *
 */
public class Page {
	public int number;
	public String content;

	public Page(final int number, final String content) {
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
