package m2dl.osgi.decorator.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import m2dl.osgi.editor.service.IDecorationService;

public class DecoratorService implements IDecorationService {

	private boolean comment = false;

	@Override
	public String decorate(File file) {
		String content = null;
		if (file.getName().endsWith(".java") || file.getName().endsWith(".css")) {
			String[] keyword = { "for", "return", "if", "else", "try", "catch", "new ", "null", "void ", "final ",
					"assert", "public", "private ", "class ", "import ", "package ", "int ", "implements ", "extends ",
					"while", "static" };
			try {
				InputStream ips = new FileInputStream(file);
				InputStreamReader ipsr = new InputStreamReader(ips);
				BufferedReader br = new BufferedReader(ipsr);
				String ligne;
				content = "<html><head></head><body>";
				while ((ligne = br.readLine()) != null) {
					// CSS
					if (file.getName().endsWith(".css")) {
						ligne = colorName(ligne);
						ligne = colorContent(ligne);
					}

					// Les 2
					ligne = colorComment(ligne);
					content += ligne + "<br />";
				}
				br.close();
				content += "</body></html>";
				if (file.getName().endsWith(".java")) {
					for (int i = 0; i < keyword.length; i++) {
						content = content.replaceAll(keyword[i], ":keyword{~" + keyword[i] + "~}");
					}
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
		return content;
	}

	String colorComment(String ligne) {
		String tag, word = "";
		// Commentaire de type : /*....*/
		Pattern p = Pattern.compile("/\\*([^>]*)\\*/");
		Matcher m = p.matcher(ligne);

		// Commentaire de type //...
		Pattern pSimpleComment = Pattern.compile("//([^>]*)");
		Matcher mSimpleComment = pSimpleComment.matcher(ligne);

		// Commentaire de type : /* .....
		Pattern pStart = Pattern.compile("/\\*([^>]*)");
		Matcher mStart = pStart.matcher(ligne);

		// Commentaire de type : ..... */
		Pattern pEnd = Pattern.compile("([^>]*)\\*/");
		Matcher mEnd = pEnd.matcher(ligne);
		if (m.find())
			word = m.group();
		else if (mStart.find()) {
			comment = true;
			word = mStart.group();
		} else if (mEnd.find()) {
			comment = false;
			word = mEnd.group();
		} else if (mSimpleComment.find())
			word = mSimpleComment.group();
		if (!word.equals(""))
			ligne = ligne.replace(word, ":comment{~" + word + "~}");
		else if (comment)
			ligne = ":comment{~" + ligne + "~}";
		return ligne;
	}

	// Pour CSS
	String colorContent(String ligne) {
		String tag, word;
		Pattern p = Pattern.compile("\\{[^~]([^>]*)\\}");
		Matcher m = p.matcher(ligne);
		if (m.find()) {
			word = m.group();
			tag = word.substring(1, word.length() - 1);
			String[] eachWord = tag.split(";");
			for (int i = 0; i < eachWord.length; i++) {
				String[] test = eachWord[i].split(":");
				for (int j = 0; j < test.length; j++) {

					if (j % 2 == 0) {
						ligne = ligne.replace(test[j] + ":", ":keyword{~" + test[j] + ":~}");
					}
				}
			}
		}
		return ligne;
	}

	// Pour CSS
	String colorName(String ligne) {
		String tag, word;
		Pattern p = Pattern.compile("^([^>]*)\\{");
		Matcher m = p.matcher(ligne);
		if (m.find()) {
			word = m.group();
			tag = word.substring(0, word.length() - 1);
			String[] eachWord = tag.split(" ");
			for (int i = 0; i < eachWord.length; i++) {
				String color = ":keywordBlue{~" + eachWord[i] + "~}";
				if (eachWord[i].startsWith("."))
					color = ":keywordRed{~" + eachWord[i] + "~}";
				ligne = ligne.replaceFirst(eachWord[i], color);
			}
		}
		return ligne;
	}
}
