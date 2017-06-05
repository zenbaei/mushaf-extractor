package org.zenbaei.quran.service.quran.extractor;

import java.nio.file.OpenOption;

public interface QuranExtractor {

	void extractContentPerQuranPage(final OpenOption openOption);

	void extractMetadataPerQuranPage(final OpenOption openOption);

	void extractQuranIndex(final OpenOption openOption);
}
