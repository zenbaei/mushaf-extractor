package org.zenbaei.quran.util;

import org.junit.Test;
import org.zenbaei.quran.util.ArabicUtils;

public class ArabicUtilsTest {


	@Test
	public void testNormalizeEnhanced() {
		 final String test = "وَاللَّهُ أَعْلَمُ بِمَا يُوعُونَ";
	     System.out.println(ArabicUtils.normalizeEnhanced(test));
	}
}
