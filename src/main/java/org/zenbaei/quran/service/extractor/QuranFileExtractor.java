package org.zenbaei.quran.service.extractor;

import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.zenbaei.core.file.writer.FileWriter;
import org.zenbaei.quran.all.Constants;
import org.zenbaei.quran.domain.Page;
import org.zenbaei.quran.domain.QuranPageMetadata;
import org.zenbaei.quran.domain.SurahIndex;
import org.zenbaei.quran.service.parser.QuranParser;
import org.zenbaei.quran.service.parser.QuranParserHelper;
import org.zenbaei.quran.service.reader.QuranReader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Extracts from mushaf doc to text file, each file represents a page from the document.
 *
 * @author zenbaei
 *
 */
public class QuranFileExtractor {

	private static final Gson gson = new Gson();
	private static final QuranFileExtractor QURAN_EXTRACTOR = new QuranFileExtractor();
	private static final List<Page> pages = QuranParser.toPages(
			QuranReader.asString(Constants.QURAN_MODIFIED_DOC_FILE_PATH) );
	private String LAST_SURAH_NAME;

	private QuranFileExtractor() {
	}

	public static QuranFileExtractor getInstance() {
		return QURAN_EXTRACTOR;
	}

	/**
	 * Extracts files from mushaf doc under resources data directory.
	 *
	 * It creates a directory with the page number then a file with page number and '.page'
	 * as extension.
	 *
	 * @param openOption
	 *
	 * @see StandardOpenOption
	 */
	public void extractContentPerQuranPage(final OpenOption openOption) {
		write(Constants.QURAN_FILE_EXTENSION, openOption, pg -> {
			String content = QuranParser.switchArabicNumbers(pg.content);
			return content.trim();
		});
	}

	/**
	 * Extracts mushaf page info to a file with extension '.metadata' and page number as file name under page number as folder.
	 *
	 * @param openOption the file creation option
	 *
	 */
	public void extractMetadataPerQuranPage(final OpenOption openOption) {
		final Map<String, Integer> surahIndexMap = QuranParserHelper.toSurahIndexMap( QuranParser.toSurahIndex(pages) );
		write(Constants.METADATA_FILE_EXTENSION, openOption, pg -> {
			final List<QuranPageMetadata> metadataList = QuranParser.toMetadata(pg.content);
			LAST_SURAH_NAME = QuranParser.getLastSurahName(metadataList).orElse(LAST_SURAH_NAME);
			final List<QuranPageMetadata> metas = QuranParserHelper.fillEmptySurahNameFromLastOne(metadataList, LAST_SURAH_NAME);
			final List<JsonObject> list = new ArrayList<>();
			metas.forEach(metadata -> {
				final JsonObject obj = new JsonObject();
				obj.addProperty("fromAyah", metadata.fromAyah);
				obj.addProperty("toAyah", metadata.toAyah);
				obj.addProperty("surahNumber", surahIndexMap.get(metadata.surahName));
				list.add(obj);
			});
			return gson.toJson(list);
		});
	}

	public void extractQuranIndex(final OpenOption openOption) {
		FileWriter.createDirectory(Constants.QURAN_EXTRACTED_FILES_BASE_PATH);
		final List<SurahIndex> surahIndexes = QuranParser.toSurahIndex(pages);
		FileWriter.write(Constants.QURAN_INDEX_FILE_PATH, gson.toJson(surahIndexes), openOption);
	}

	private void write(final String fileExtension, final OpenOption openOption, final Function<Page, String> function) {
		pages.forEach(pg -> {
			final String dir = Constants.QURAN_EXTRACTED_FILES_BASE_PATH + pg.number + "/";
			FileWriter.createDirectory(dir);
			final String filePath = dir + pg.number + fileExtension;
			FileWriter.write(filePath, function.apply(pg), openOption);
		});
	}


}
