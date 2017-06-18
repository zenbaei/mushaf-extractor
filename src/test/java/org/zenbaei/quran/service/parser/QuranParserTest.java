package org.zenbaei.quran.service.parser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.zenbaei.quran.BaseTest;
import org.zenbaei.quran.all.Constants;
import org.zenbaei.quran.domain.Page;
import org.zenbaei.quran.domain.QuranPageMetadata;
import org.zenbaei.quran.domain.SurahIndex;
import org.zenbaei.quran.service.parser.QuranParser;
import org.zenbaei.quran.service.reader.QuranReader;
import org.zenbaei.quran.util.ArabicUtils;

public class QuranParserTest extends BaseTest {

	private static final List<Page> PAGES = QuranParser.toPages(
			QuranReader.asString(Constants.QURAN_MODIFIED_DOC_FILE_PATH) );

	@Test
	public void test_ayah_range_from_fateha_page_should_be_1_to_7() {
		final Page fatehaPage = PAGES.get(1 - 1);
		final List<QuranPageMetadata> ayahs = QuranParser.toMetadata(fatehaPage.content);
		assertThat(ayahs.isEmpty(), is(false));
		assertThat(ayahs.get(0).fromAyah, is(1));
		assertThat(ayahs.get(0).toAyah, is(7));
		final String surahName = ArabicUtils.removeTashkil(ayahs.get(0).surahName);
		assertThat(surahName, is("الفاتحة"));
	}

	@Test
	public void test_ayah_range_from_page_with_multiple_surah() {
		final Page ekhlasFalaqNasPage = PAGES.get(604 - 1);
		final List<QuranPageMetadata> ayahRanges = QuranParser.toMetadata(ekhlasFalaqNasPage.content);
		assertThat(ayahRanges.size(), is(3));

		final String exptectedSurahName1 = ArabicUtils.removeTashkil(ayahRanges.get(0).surahName);
		final String exptectedSurahName2 = ArabicUtils.removeTashkil(ayahRanges.get(1).surahName);
		final String exptectedSurahName3 = ArabicUtils.removeTashkil(ayahRanges.get(2).surahName);
		assertThat(exptectedSurahName1, is("الإخلاص"));
		assertThat(exptectedSurahName2, is("الفلق"));
		assertThat(exptectedSurahName3, is("الناس"));

		assertThat(ayahRanges.get(0).fromAyah, is(1));
		assertThat(ayahRanges.get(0).toAyah, is(4));

		assertThat(ayahRanges.get(1).fromAyah, is(1));
		assertThat(ayahRanges.get(1).toAyah, is(5));

		assertThat(ayahRanges.get(2).fromAyah, is(1));
		assertThat(ayahRanges.get(2).toAyah, is(6));
	}

	@Test
	public void test_ayah_range_from_page_with_no_surah() {
		final Page alBakarahPage = PAGES.get(3 - 1);
		final List<QuranPageMetadata> ayahRanges = QuranParser.toMetadata(alBakarahPage.content);
		assertThat(ayahRanges.size(), is(1));

		assertThat(ayahRanges.get(0).fromAyah, is(6));
		assertThat(ayahRanges.get(0).toAyah, is(16));
		assertThat(ayahRanges.get(0).surahName.isEmpty(), is(true));
	}

	@Test
	public void test_ayah_range_from_page_with_surah_as_a_word_inside_should_be_ignored_1() {
		final Page alNourPage = PAGES.get(350 - 1);
		final List<QuranPageMetadata> ayahRanges = QuranParser.toMetadata(alNourPage.content);
		assertThat(ayahRanges.size(), is(1));

		assertThat(ayahRanges.get(0).fromAyah, is(1));
		assertThat(ayahRanges.get(0).toAyah, is(10));
		assertThat(ayahRanges.get(0).surahName.isEmpty(), is(true));
	}

	@Test
	public void test_ayah_range_from_page_with_surah_as_a_word_inside_should_be_ignored_2() {
		final Page mohamedPage = PAGES.get(509 - 1);
		final List<QuranPageMetadata> ayahRanges = QuranParser.toMetadata(mohamedPage.content);
		assertThat(ayahRanges.size(), is(1));

		assertThat(ayahRanges.get(0).fromAyah, is(20));
		assertThat(ayahRanges.get(0).toAyah, is(29));
		assertThat(ayahRanges.get(0).surahName.isEmpty(), is(true));
	}

	@Test
	public void test_ayah_range_from_page_with_surah_as_last_line_should_have_zero_value_but_surah_name_should_be_present() {
		final Page alNourPage = PAGES.get(349 - 1);
		final List<QuranPageMetadata> ayahRanges = QuranParser.toMetadata(alNourPage.content);
		assertThat(ayahRanges.size(), is(2));

		assertThat(ayahRanges.get(0).fromAyah, is(105));
		assertThat(ayahRanges.get(0).toAyah, is(118));
		assertThat(ayahRanges.get(0).surahName.isEmpty(), is(true));

		assertThat(ayahRanges.get(1).fromAyah, is(0));
		assertThat(ayahRanges.get(1).toAyah, is(0));
		assertThat(ayahRanges.get(1).surahName.isEmpty(), is(false));

		final String exptectedSurahName1 = ArabicUtils.removeTashkil(ayahRanges.get(1).surahName);
		assertThat(exptectedSurahName1, is(equalTo("النور")));
	}

	@Test
	public void test_quranIndex_size_should_be_114() {
		final List<SurahIndex> qIdx = QuranParser.toSurahIndex(PAGES);
		assertThat(qIdx.size(), is(Constants.QURAN_TOTAL_SURAH_COUNT));
	}

	@Test
	public void test_quranIndex_for_surah_alone_on_page() {
		final List<SurahIndex> qIdx = QuranParser.toSurahIndex(PAGES);
		final String expectedSurahName = ArabicUtils.removeTashkil(qIdx.get(1).surahName);
		assertThat(expectedSurahName, is(equalTo("البقرة")));
		assertThat(qIdx.get(1).pageNumber, is(2));
	}

	@Test
	public void test_quranIndex_for_surah_with_others_on_page() {
		final List<SurahIndex> qIdx = QuranParser.toSurahIndex(PAGES);

		final SurahIndex alNasIndex = qIdx.get(Constants.QURAN_TOTAL_SURAH_COUNT - 1);
		final String expectedSurahName = ArabicUtils.removeTashkil(alNasIndex.surahName);
		assertThat(expectedSurahName, is(equalTo("الناس")));
		assertThat(alNasIndex.pageNumber, is(Constants.QURAN_TOTAL_PAGE_COUNT));

		final SurahIndex alFalaqIndex = qIdx.get(Constants.QURAN_TOTAL_SURAH_COUNT - 2);
		final String alFalaqSurahName = ArabicUtils.removeTashkil(alFalaqIndex.surahName);
		assertThat(alFalaqSurahName, is(equalTo("الفلق")));
		assertThat(alFalaqIndex.pageNumber, is(Constants.QURAN_TOTAL_PAGE_COUNT));
	}

	@Test
	public void test_quranIndex_for_surah_as_last_line_on_page() {
		final List<SurahIndex> qIdx = QuranParser.toSurahIndex(PAGES);
		final SurahIndex alNoorIndex = qIdx.get(24 - 1);

		final String expectedSurahName = ArabicUtils.removeTashkil(alNoorIndex.surahName);
		assertThat(expectedSurahName, is(equalTo("النور")));
		assertThat(alNoorIndex.pageNumber, is(350));
	}

}
