package org.zenbaei.quraan.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zenbaei.quraan.domain.Ayah;
import org.zenbaei.quraan.domain.Token;

public class TokenService {

	public static List<Token> splitByTokens(final Ayah ayah) {
		int i= 0;
		final List<Token> tokens = new ArrayList<>();

		final Pattern p = Pattern.compile("\\s");
		final Matcher m = p.matcher(ayah.sentence);

		while(m.find()){
			String data = ayah.sentence.substring(i, m.start());
			data = data.trim();

			final Token t = new Token(ayah.pageNumber, ayah.enNumber, data);
			tokens.add(t);

			i = m.end();
		}
		return tokens;
	}

	/**
	 * In Original Mushaf word document, every line break is seen as 2 breaks after parse.
	 * @param data
	 * @param tokens
	 * @return
	 */
	public static List<Token> removeSuccessiveLineBreak(final List<Token> tokens) {
		final List<Token> editedTokens = new ArrayList<>();
		for(int i = 0; i < tokens.size() - 1; i++){
			final String currentToken = tokens.get(i).token;
			final String nextToken = tokens.get(i + 1).token;
			if(currentToken.trim().isEmpty() && nextToken.trim().isEmpty()) {
				continue;
			}
			editedTokens.add(tokens.get(i));
		}
		return editedTokens;
	}

	public static void validateNoMissingAyah(final List<Token> tokens){
		for(int i = 0; i < tokens.size() - 1; i++){
				final int current = tokens.get(i).ayahNumber;
				final int next = tokens.get(i + 1).ayahNumber;

				if(next != 1 && next != current || next != current + 1){
					tokens.get(i).print();
					tokens.get(i+1).print();
					throw new IllegalStateException();
				}
		}
	}

	//TODO: not working with stringbuilder
	public static List<Token> replaceEmptyTokenWithLIneBreak(final List<Token> tokens) {
		final List<Token> list = new ArrayList<>();
		tokens.forEach(t -> {
			if(t.token.isEmpty())
				t.token = "\\\\n";
			list.add(t);
		});

		return list;
	}
}