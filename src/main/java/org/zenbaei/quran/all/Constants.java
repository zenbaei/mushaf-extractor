package org.zenbaei.quran.all;

public class Constants {

	/** Modified version of mushaf */
	public static final String QURAN_MODIFIED_DOC_FILE_PATH = "src/main/resources/mushaf/mushaf-edited.doc";

	// public static final String QURAN_ORIGINAL_DOC_FILE_PATH = "src/main/resources/mushaf/UthmanicHafs1Ver09.doc";

	public static final String QURAN_EXTRACTED_FILES_BASE_PATH = "src/main/resources/data/mushaf/";

	public static final int QURAN_TOTAL_PAGE_COUNT = 604;

	public static final int QURAN_TOTAL_SURAH_COUNT = 114;

	public static final String QURAN_FILE_EXTENSION = ".content";

	public static final String METADATA_FILE_EXTENSION = ".metadata";

	public static final String QURAN_INDEX_FILE_NAME = "quran.index";

	public static final String QURAN_INDEX_FILE_PATH = Constants.QURAN_EXTRACTED_FILES_BASE_PATH + QURAN_INDEX_FILE_NAME;

	private Constants() {}

}
