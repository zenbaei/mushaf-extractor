package org.zenbaei.core.file.writer;

import java.nio.file.OpenOption;

/**
 * Adding custom types to {@code OpenOption}
 *
 * @author zenbaei
 *
 */
public enum CustomOpenOption implements OpenOption {

	/** Sign out deleting file first before creation */
	OVERRIDE;

}
