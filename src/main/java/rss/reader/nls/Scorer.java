package rss.reader.nls;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import rss.reader.model.Word;

public class Scorer {

	public static Map<String, Word> getWordMap(List<Word> wordSet) {
		return wordSet
				.stream()
				.collect(Collectors
						.toMap(
								Word::getText, Function.identity()));
	}

	public static Score calculateScore(List<Word> wordSet1, List<Word> wordSet2) {
		var result = new Score();

		var map1 = getWordMap(wordSet1);
		var map2 = getWordMap(wordSet2);

		var key1 = map1.keySet();
		var key2 = map2.keySet();

		key1.retainAll(key2);

		for (var key : key1) {
			Word word1 = map1.get(key);
			Word word2 = map2.get(key);
			int delta = word1.getScore() * word2.getScore();
			result.increaseScore(delta);
			result.addWord(key, word1.getPosTag() + "-" + word2.getPosTag(), delta);
		}
		
		Collections.sort(result.getWordList(), new Comparator<Word>() {

			@Override
			public int compare(Word o1, Word o2) {

				return o2.getScore()-o1.getScore();
			}
		} );

		return result;
	}

}
