package org.zenbaei.quran.service.quran.parser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.zenbaei.quran.BaseTest;
import org.zenbaei.quran.domain.QuranMetadata;

public class QuranParserHelperTest extends BaseTest {

	@Test
	public void testFillSurahNameFromPerviousOne() {
		final String fateha = "fateha";
		final String bakarh = "bakarah";
		final String nesaa = "nesaa";

		final QuranMetadata fatehaMeta = new QuranMetadata(1, 5, fateha);
		final QuranMetadata bakaraMeta = new QuranMetadata(1, 5, bakarh);
		final QuranMetadata emptySurahMeta = new QuranMetadata(6, 10, "");
		final QuranMetadata nessaMeta = new QuranMetadata(1, 5, nesaa);

		final List<QuranMetadata> metadataList = Arrays.asList(fatehaMeta, bakaraMeta,
				emptySurahMeta, nessaMeta);

		final List<QuranMetadata> metas = QuranParserHelper.fillEmptySurahNameFromPreviousOne(metadataList);

		assertThat(metas.get(0).surahName, is(equalTo(fateha)));
		assertThat(metas.get(1).surahName, is(equalTo(bakarh)));
		assertThat(metas.get(2).surahName, is(equalTo(bakarh)));
		assertThat(metas.get(3).surahName, is(equalTo(nesaa)));
	}

}
