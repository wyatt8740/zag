package org.p2c2e.zing;

import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.p2c2e.blorb.Color;

public class Style implements Cloneable {
	public static boolean USE_HINTS = true;
	public static boolean use_antialiasing = true;
	public static TreeMap GRID_STYLES = new TreeMap();
	public static TreeMap BUFFER_STYLES = new TreeMap();
	public static char[] MONO_TEST_ARRAY = { 'm', 'i' };

	public static final int LEFT_FLUSH = 0;
	public static final int RIGHT_FLUSH = 3;
	public static final int CENTERED = 2;
	public static final int LEFT_RIGHT_FLUSH = 1;

	public String name;
	public String family;
	public boolean isOblique;
	public boolean isUnderlined;
	public int size;
	public Float weight;
	public int leftIndent;
	public int rightIndent;
	public int parIndent;
	public int justification;
	public Color textColor;
	public Color backColor;

	HashMap<TextAttribute, Object> map;

	boolean isMonospace;
	boolean isHyperlinked;

	public Style(String name, String fam, int s, Float w, boolean oblique,
			boolean underline, int l, int r, int p, int just, Color t, Color b) {
		this.name = name;
		family = fam;
		size = s;
		weight = w;
		isOblique = oblique;
		isUnderlined = underline;
		leftIndent = l;
		rightIndent = r;
		parIndent = p;
		justification = just;
		textColor = t;
		backColor = b;

		isHyperlinked = false;
	}

	@Override
	public Object clone() {
		Style c;
		try {
			c = (Style) super.clone();
			c.name = name;
			c.family = family;
			c.size = size;
			c.weight = weight;
			c.isOblique = isOblique;
			c.isUnderlined = isUnderlined;
			c.leftIndent = leftIndent;
			c.rightIndent = rightIndent;
			c.parIndent = parIndent;
			c.justification = justification;
			c.textColor = textColor;
			c.backColor = backColor;
			c.isMonospace = isMonospace;

			// don't copy the map, since hinting may change it
			c.map = null;
			// don't copy hyperlink flag
			c.isHyperlinked = false;

			return c;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public boolean isMonospace() {
		return isMonospace;
	}

	public void setMonospace(boolean monospace) {
		this.isMonospace = monospace;
	}

	public boolean isHyperlinked() {
		return isHyperlinked;
	}

	public Map<TextAttribute, Object> getMap() {
		if (map == null)
			createMap();

		return map;
	}

	public void setMap(HashMap<TextAttribute, Object> map) {
		this.map = map;
	}

	public Style getHyperlinked() {
		Style s = (Style) clone();
		s.textColor = new Color(0, 255, 0);
		s.isUnderlined = true;
		s.isHyperlinked = true;
		return s;
	}

	public static boolean usingHints() {
		return USE_HINTS;
	}

	public static void addStyle(Style s, int winType) {
		if (winType == IWindow.TEXT_GRID)
			GRID_STYLES.put(s.name, s);
		else
			BUFFER_STYLES.put(s.name, s);
	}

	public static Style getStyle(String name, int winType) {
		if (winType == IWindow.TEXT_GRID || winType == IGlk.WINTYPE_ALL_TYPES)
			return (Style) GRID_STYLES.get(name);
		else
			return (Style) BUFFER_STYLES.get(name);
	}

	private void createMap() {
		map = new HashMap<TextAttribute, Object>();

		map.put(TextAttribute.FAMILY, family);
		map.put(TextAttribute.POSTURE,
				(isOblique) ? TextAttribute.POSTURE_OBLIQUE
						: TextAttribute.POSTURE_REGULAR);
		if (isUnderlined)
			map.put(TextAttribute.UNDERLINE,
					TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
		map.put(TextAttribute.SIZE, new Float(size));
		map.put(TextAttribute.WEIGHT, weight);
		map.put(TextAttribute.BACKGROUND, new java.awt.Color(
				backColor.getRed(), backColor.getGreen(), backColor.getBlue()));
		map.put(TextAttribute.FOREGROUND, new java.awt.Color(
				textColor.getRed(), textColor.getGreen(), textColor.getBlue()));
	}

}
