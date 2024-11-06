package com.github.ternityclockworks.eurekaarcana.util;
import java.text.NumberFormat;
import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

/**
 * Complete credit to the LangNumberFormat utility class from the Create mod.
 * See <a href="https://github.com/Creators-of-Create/Create/blob/mc1.20.1/dev/src/main/java/com/simibubi/create/foundation/utility/LangNumberFormat.java"> Create LangNumberFormat class</a>
 */
public class LangNumberFormat {

	private NumberFormat format = NumberFormat.getNumberInstance(Locale.ROOT);
	public static LangNumberFormat numberFormat = new LangNumberFormat();

	public NumberFormat get() {
		return format;
	}

	public void update() {
		format = NumberFormat.getInstance(Minecraft.getInstance()
			.getLanguageManager()
			.getJavaLocale());
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(0);
		format.setGroupingUsed(true);
	}

	public static String format(double d) {
		if (Mth.equal(d, 0))
			d = 0;
		return numberFormat.get()
			.format(d)
			.replace("\u00A0", " ");
	}

}