package org.zenbaei.quran.domain;

import java.util.Optional;

public class AyahRange {

	public int from;
	public int to;
	public Optional<String> surahName;

	public AyahRange(final int from, final int to, final Optional<String> surahName) {
		super();
		this.from = from;
		this.to = to;
		this.surahName = surahName;
	}

	@Override
	public String toString() {
		return "AyahRange [from=" + from + ", to=" + to + ", surahName=" + surahName.orElse("") + "]";
	}
}
