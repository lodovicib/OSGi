package m2dl.osgi.coloration.css.service.impl;

import m2dl.osgi.editor.service.IColorService;

public class CSSColorationService implements IColorService {

	@Override
	public String colorize(String content) {
		content = colorizeGeneric(content, "keyword", "#A9A9F5", true);
		content = colorizeGeneric(content, "keywordBlue", "blue", false);
		content = colorizeGeneric(content, "keywordRed", "red", false);
		content = colorizeGeneric(content, "comment", "#31B404", false);
		return content;
	}

	private String colorizeGeneric(String content, String key, String color, boolean bold) {
		String start = "<font color=\"" + color + "\">";
		String end = "</font>";
		if (bold) {
			start = start + "<b>";
			end = "</b>" + end;
		}
		String[] eachWord = content.split(":" + key + "\\{~");
		for (int i = 0; i < eachWord.length; i++) {
			String[] eachWord2 = eachWord[i].split("~\\}");
			for (int j = 0; j < eachWord2.length; j++) {
				content = content.replace(":" + key + "{~" + eachWord2[j] + "~}", start + eachWord2[j] + end);
			}
		}
		return content;
	}

	@Override
	public String getType() {
		System.out.println("CSS");
		return "CSS";
	}

}
