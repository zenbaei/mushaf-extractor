package org.zenbaei.quran.domain;

public class AyahRange {

	public int from;
	public int to;
	public String surahName;

	public AyahRange(final int from, final int to, final String surahName) {
		super();
		this.from = from;
		this.to = to;
		this.surahName = surahName;
	}

	@Override
	public String toString() {
		return "AyahRange [from=" + from + ", to=" + to + ", surahName=" + surahName + "]";
	}
}
