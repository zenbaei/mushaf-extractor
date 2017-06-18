package org.zenbaei.quran.domain;

/**
 * Holds info about a page from mushaf.
 *
 * @author zenbaei
 *
 */
public class QuranPageMetadata {

	public final int fromAyah;
	public final int toAyah;
	public final String surahName;

	public QuranPageMetadata(final int fromAyah, final int toAyah, final String surahName) {
		super();
		this.fromAyah = fromAyah;
		this.toAyah = toAyah;
		this.surahName = surahName;
	}

	@Override
	public String toString() {
		return "QuranMetadata [from=" + fromAyah + ", to=" + toAyah + ", surahName=" + surahName + "]";
	}
}
