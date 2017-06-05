package org.zenbaei.quran.service.quran.extractor;

import static org.zenbaei.quran.all.Constants.METADATA_FILE_EXTENSION;
import static org.zenbaei.quran.all.Constants.QURAN_FILE_EXTENSION;
import static org.zenbaei.quran.all.Constants.QURAN_INDEX_FILE_PATH;

import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zenbaei.io.file.writer.FileWriter;
import org.zenbaei.quran.all.Constants;
import org.zenbaei.quran.domain.Page;
import org.zenbaei.quran.domain.QuranMetadata;
import org.zenbaei.quran.service.quran.parser.QuranParser;
import org.zenbaei.quran.service.quran.parser.QuranParserHelper;
import org.zenbaei.quran.service.quran.reader.QuranReader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Extracts from mushaf doc to text file, each file represents a page from the document.
 *
 * @author zenbaei
 *
 */
public class QuranFileExtractorImpl implements QuranExtractor {

	private static final Logger LOG = LoggerFactory.getLogger(QuranFileExtractorImpl.class);
	private static final Gson gson = new Gson();
	private static final QuranFileExtractorImpl QURAN_EXTRACTOR = new QuranFileExtractorImpl();
	private static final List<Page> pages = QuranParser.toPages(
			QuranReader.asString(Constants.QURAN_MODIFIED_DOC_FILE_PATH) );

	private QuranFileExtractorImpl() {
	}

	public static QuranFileExtractorImpl getInstance() {
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
	@Override
	public void extractContentPerQuranPage(final OpenOption openOption) {
		write(QURAN_FILE_EXTENSION, openOption, pg -> pg.content);
	}

	/**
	 * Extracts mushaf page info to a file with extension '.metadata' and page number as file name under page number as folder.
	 *
	 * @param openOption the file creation option
	 *
	 */
	@Override
	public void extractMetadataPerQuranPage(final OpenOption openOption) {
		write(METADATA_FILE_EXTENSION, openOption, pg -> {
			final List<QuranMetadata> metadataList = QuranParser.toMetadata(pg.content);
			final List<QuranMetadata> metas = QuranParserHelper.fillEmptySurahNameFromPreviousOne(metadataList);
			final String metadataListAsJson = gson.toJson(metas);
			LOG.debug(metadataListAsJson);
			return metadataListAsJson;
		});
	}

	@Override
	public void extractQuranIndex(final OpenOption openOption) {
		FileWriter.createDirectory(Constants.QURAN_EXTRACTED_FILES_BASE_PATH);

		final StringBuilder strBld = new StringBuilder();
		QuranParser.quranIndex(pages)
			.forEach(pair -> {
				final JsonObject jsonObj = new JsonObject();
				jsonObj.addProperty("key", pair.getKey());
				jsonObj.addProperty("value", pair.getValue());
				final String json = gson.toJson(jsonObj);
				strBld.append(json).append(System.lineSeparator());
			});

		FileWriter.write(QURAN_INDEX_FILE_PATH, strBld.toString(), openOption);
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
