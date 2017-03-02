package scrabble.util;

import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;

public class CleanForCharset {
	final private static String REPLACEMENT = null;;
	final private static String DESTCHARSET = "ISO-8859-1";

	Charset charset;
	CharsetEncoder encoder;
	CharsetDecoder decoder;

	CleanForCharset() {
		this(DESTCHARSET, REPLACEMENT);
	}

	static CleanForCharset instance;
	static {
		instance = new CleanForCharset();
	}

	static public synchronized String cleanString(final CharSequence str) {
		final CharBuffer cb = instance.clean(str);
		final char[] chars = new char[cb.limit()];
		int charPtr = 0;
		boolean whiteSpace = true;
		for (int i = 0; i < cb.limit(); i++) {
			if (Character.isWhitespace(cb.charAt(i))) {
				if (!whiteSpace) {
					chars[charPtr++] = ' ';
					whiteSpace = true;
				}
				continue;
			}
			whiteSpace = false;
			chars[charPtr++] = cb.charAt(i);
		}
		return String.copyValueOf(chars, 0, charPtr);
	}

	CleanForCharset(final String charSetName, final String replacement) {
		charset = Charset.forName(charSetName);
		encoder = charset.newEncoder();
		decoder = charset.newDecoder();
		if (replacement == null) {
			encoder.onMalformedInput(CodingErrorAction.IGNORE);
			encoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
			decoder.onMalformedInput(CodingErrorAction.IGNORE);
			decoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
		} else {
			encoder.onMalformedInput(CodingErrorAction.REPLACE);
			encoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
			decoder.onMalformedInput(CodingErrorAction.REPLACE);
			decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
			try {
				encoder.replaceWith(REPLACEMENT.getBytes(encoder.charset().name()));
			} catch (final UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			decoder.replaceWith(REPLACEMENT);
		}
	}

	CharBuffer clean(final CharSequence str) {
		try {
			return decoder.decode(encoder.encode(CharBuffer.wrap(str)));
		} catch (final CharacterCodingException e) {
			return CharBuffer.wrap(str);
		}
	}
}
