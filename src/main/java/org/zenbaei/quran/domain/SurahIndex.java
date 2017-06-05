package org.zenbaei.quran.domain;

/**
 * Represents a surah index entry.
 *
 * @author zenbaei
 *
 */
public class SurahIndex {

	public final String surahName;
	public final int pageNumber;

	public SurahIndex(final String surahName, final int pageNumber) {
		super();
		this.surahName = surahName;
		this.pageNumber = pageNumber;
	}

	@Override
	public String toString() {
		return "SurahIndex [surahName=" + surahName + ", pageNumber=" + pageNumber + "]";
	}
}
