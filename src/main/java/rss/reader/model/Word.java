package rss.reader.model;

/**
 * <p>Encapsulates all information related to a single world (lemma) of the text:
 * 
 * <li>text: <i>the word</i> 
 * <li>POS Tag: <a href="http://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html">http://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html</a>
 * <li>score: <i>numerical score for the word</i> 
 */
public class Word implements Comparable<Word> {

	/**
	 * Score associated with the {@link Word}. This can be number of occurences or
	 * other computed number.
	 */
	private int score = 1;

	/**
	 * Stanford CoreNLP POS tag (e.g.: NN - noun)
	 * 
	 * @link <a href=
	 *       "http://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html">http://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html</a>
	 */
	private String posTag;

	/**
	 * The text of the word
	 */
	private String text;

	
	public Word(String text, String posTag) {
		this.text = text;
		this.posTag = posTag;
	}

	public Word(String text, String posTag, int score) {
		this(text, posTag);
		this.score = score;
	}

	@Override
	public int compareTo(Word o) {
		if (o != null && o.text != null) {
			var textCompare = this.text.compareTo(o.text);
			if (textCompare == 0) {
				return this.score - o.score;
			} else {
				return textCompare;
			}
		}
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (posTag == null) {
			if (other.posTag != null)
				return false;
		} else if (!posTag.equals(other.posTag))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

	public int getScore() {
		return score;
	}

	public String getPosTag() {
		return posTag;
	}

	public String getText() {
		return text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((posTag == null) ? 0 : posTag.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	public void increaseScore() {
		this.score++;
	}

	@Override
	public String toString() {
		var str = text + "(" + posTag + ")";
		if (this.score > 1) {
			str += ":" + score;
		}
		return str;
	}

}