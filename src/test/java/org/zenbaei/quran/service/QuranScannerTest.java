package org.zenbaei.quran.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.zenbaei.quran.BaseTest;
import org.zenbaei.quran.all.Constants;
import org.zenbaei.quran.domain.AyahRange;
import org.zenbaei.quran.domain.Page;
import org.zenbaei.quran.util.ArabicUtils;

public class QuranScannerTest extends BaseTest {

	@Test
	public void test_ayah_range_from_fateha_page_should_be_1_to_7() {
		final Page fatehaPage = pages.get(1 - 1);
		final List<AyahRange> ayahs = QuranScanner.toAyahRanges(fatehaPage.content);
		assertThat(ayahs.isEmpty(), is(false));
		assertThat(ayahs.get(0).from, is(1));
		assertThat(ayahs.get(0).to, is(7));
		final String surahName = ArabicUtils.normalizeEnhanced(ayahs.get(0).surahName);
		assertThat(surahName, is("الفاتحة"));
	}

	@Test
	public void test_ayah_range_from_page_with_multiple_surah() {
		final Page ekhlasFalaqNasPage = pages.get(604 - 1);
		final List<AyahRange> ayahRanges = QuranScanner.toAyahRanges(ekhlasFalaqNasPage.content);
		assertThat(ayahRanges.size(), is(3));

		final String exptectedSurahName1 = ArabicUtils.normalizeEnhanced(ayahRanges.get(0).surahName);
		final String exptectedSurahName2 = ArabicUtils.normalizeEnhanced(ayahRanges.get(1).surahName);
		final String exptectedSurahName3 = ArabicUtils.normalizeEnhanced(ayahRanges.get(2).surahName);
		assertThat(exptectedSurahName1, is("الإخلاص"));
		assertThat(exptectedSurahName2, is("الفلق"));
		assertThat(exptectedSurahName3, is("الناس"));

		assertThat(ayahRanges.get(0).from, is(1));
		assertThat(ayahRanges.get(0).to, is(4));

		assertThat(ayahRanges.get(1).from, is(1));
		assertThat(ayahRanges.get(1).to, is(5));

		assertThat(ayahRanges.get(2).from, is(1));
		assertThat(ayahRanges.get(2).to, is(6));
	}

	@Test
	public void test_ayah_range_from_page_with_no_surah() {
		final Page alBakarahPage = pages.get(3 - 1);
		final List<AyahRange> ayahRanges = QuranScanner.toAyahRanges(alBakarahPage.content);
		assertThat(ayahRanges.size(), is(1));

		assertThat(ayahRanges.get(0).from, is(6));
		assertThat(ayahRanges.get(0).to, is(16));
		assertThat(ayahRanges.get(0).surahName.isEmpty(), is(true));
	}

	@Test
	public void test_ayah_range_from_page_with_surah_as_a_word_inside_should_be_ignored_1() {
		final Page alNourPage = pages.get(350 - 1);
		final List<AyahRange> ayahRanges = QuranScanner.toAyahRanges(alNourPage.content);
		assertThat(ayahRanges.size(), is(1));

		assertThat(ayahRanges.get(0).from, is(1));
		assertThat(ayahRanges.get(0).to, is(10));
		assertThat(ayahRanges.get(0).surahName.isEmpty(), is(true));
	}

	@Test
	public void test_ayah_range_from_page_with_surah_as_a_word_inside_should_be_ignored_2() {
		final Page mohamedPage = pages.get(509 - 1);
		final List<AyahRange> ayahRanges = QuranScanner.toAyahRanges(mohamedPage.content);
		assertThat(ayahRanges.size(), is(1));

		assertThat(ayahRanges.get(0).from, is(20));
		assertThat(ayahRanges.get(0).to, is(29));
		assertThat(ayahRanges.get(0).surahName.isEmpty(), is(true));
	}

	@Test
	public void test_ayah_range_from_page_with_surah_as_last_line_should_have_zero_value_but_surah_name_should_be_present() {
		final Page alNourPage = pages.get(349 - 1);
		final List<AyahRange> ayahRanges = QuranScanner.toAyahRanges(alNourPage.content);
		assertThat(ayahRanges.size(), is(2));

		assertThat(ayahRanges.get(0).from, is(105));
		assertThat(ayahRanges.get(0).to, is(118));
		assertThat(ayahRanges.get(0).surahName.isEmpty(), is(true));

		assertThat(ayahRanges.get(1).from, is(0));
		assertThat(ayahRanges.get(1).to, is(0));
		assertThat(ayahRanges.get(1).surahName.isEmpty(), is(false));

		final String exptectedSurahName1 = ArabicUtils.normalizeEnhanced(ayahRanges.get(1).surahName);
		assertThat(exptectedSurahName1, is(equalTo("النور")));
	}

	@Test
	public void test_surah_count_should_be_114() {
		final List<String> surahNames = new ArrayList<>();
		for (int i = 0; i < pages.size(); i++) {
			final Page pg = pages.get(i);
			final List<AyahRange> ranges = QuranScanner.toAyahRanges(pg.content);
			ranges.stream()
				.filter(ay -> !ay.surahName.isEmpty())
				.forEach(ay -> surahNames.add(ay.surahName));
		}
		LOG.debug(surahNames.toString());
		assertThat(surahNames.size(), is(Constants.QURAN_TOTAL_SURAH_NUMBER));
	}

}
