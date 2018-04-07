package org.zenbaei.quran.service.parser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.zenbaei.quran.BaseTest;
import org.zenbaei.quran.domain.QuranPageMetadata;
import org.zenbaei.quran.domain.SurahIndex;
import org.zenbaei.quran.service.parser.QuranParserHelper;

public class QuranParserHelperTest extends BaseTest {

	@Test
	public void testFillSurahNameFromPerviousOne() {
		final String fateha = "fateha";
		final String bakarh = "bakarah";
		final String nesaa = "nesaa";

		final QuranPageMetadata fatehaMeta = new QuranPageMetadata(1, 5, fateha);
		final QuranPageMetadata bakaraMeta = new QuranPageMetadata(1, 5, bakarh);
		final QuranPageMetadata emptySurahMeta = new QuranPageMetadata(6, 10, "");
		final QuranPageMetadata nessaMeta = new QuranPageMetadata(1, 5, nesaa);

		final List<QuranPageMetadata> metadataList = Arrays.asList(fatehaMeta, bakaraMeta,
				emptySurahMeta, nessaMeta);

		final List<QuranPageMetadata> metas = QuranParserHelper.fillEmptySurahNameFromPreviousOne(metadataList);

		assertThat(metas.get(0).surahName, is(equalTo(fateha)));
		assertThat(metas.get(1).surahName, is(equalTo(bakarh)));
		assertThat(metas.get(2).surahName, is(equalTo(bakarh)));
		assertThat(metas.get(3).surahName, is(equalTo(nesaa)));
	}

	@Test
	public void testConvertSurahIndexToSurahOrder() {
		final SurahIndex idx1 = new SurahIndex("a", 200);
		final SurahIndex idx2 = new SurahIndex("b", 400);
		final List<SurahIndex> surahIndexes = Arrays.asList(idx1, idx2);

		final Map<String, Integer> map = QuranParserHelper.toSurahIndexMap(surahIndexes);
		assertThat(map.get("a"), is(equalTo(1)));
		assertThat(map.get("b"), is(equalTo(2)));
	}

}
