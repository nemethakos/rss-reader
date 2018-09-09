package rss.reader.nls;

import java.util.ArrayList;
import java.util.List;

import rss.reader.model.Word;

public class Score {
	private long score;
	private List<Word> wordList = new ArrayList<Word>();

	public void increaseScore(long delta) {
		score += delta;
	}

	public long getScore() {
		return score;
	}

	public List<Word> getWordList() {
		return wordList;
	}

	public void addWord(String text, String posType, long score) {
		wordList.add(new Word(text, posType, (int) score));
	}
}