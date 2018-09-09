package rss.reader.nls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import rss.reader.model.Word;

public class PosTaggerAndLemmatizer {
	
	private static PosTaggerAndLemmatizer INSTANCE; 
	
	public static PosTaggerAndLemmatizer getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PosTaggerAndLemmatizer();
		}
		return INSTANCE;
	}

	String EXCLUDE_POS_TAGS = "DT|IN|TO|CC|-LRB-|-RRB-|.|,|``|''|PRP|PRP$|CD|POS|:|WRB|EX|WP|WP$|WDT|MD|RBR|JJR|#";
	String EXCLUDE_WORDS = "have|be|do|say|get|such|%|©|go";

	StanfordCoreNLP pipeline = null;

	private PosTaggerAndLemmatizer() {
		var start = System.currentTimeMillis();
		// set up pipeline properties
		Properties props = new Properties();
		// set the list of annotators to run
		// props.setProperty("annotators",
		// "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote");
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
		// set a property for an annotator, in this case the coref annotator is being
		// set to use the neural algorithm
		// props.setProperty("coref.algorithm", "neural");
		// build pipeline
		pipeline = new StanfordCoreNLP(props);
		var end = System.currentTimeMillis();
		System.out.println("Startup time: " + (end - start) / 1000. + " sec");
	}

	public List<String> getSentences(String text) {
		
		List<String> result = new ArrayList<>();

		Annotation document = new Annotation(text);
		// run all Annotators on this text
		this.pipeline.annotate(document);

		// Iterate over all of the sentences found
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		
		for (CoreMap sentence: sentences) {
			result.add(sentence.toString());
		}
		
		return result;
	}

	public List<Word> lemmatize(String documentText) {

		List<Word> lemmas = new LinkedList<Word>();
		// Create an empty Annotation just with the given text
		Annotation document = new Annotation(documentText);

		CoreDocument cdocument = new CoreDocument(documentText);
		this.pipeline.annotate(cdocument);

		// run all Annotators on this text
		this.pipeline.annotate(document);

		// Iterate over all of the sentences found
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		int i = 0;
		for (CoreMap sentence : sentences) {

			CoreSentence csentence = cdocument.sentences().get(i++);

			int j = 0;
			// Iterate over all tokens in a sentence
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {

				var posTag = csentence.posTags().get(j++);

				// Retrieve and add the lemma for each word into the
				// list of lemmas
				String lemma = token.get(LemmaAnnotation.class).toLowerCase();

				Word word = new Word(lemma, posTag);

				if (!EXCLUDE_POS_TAGS.contains(word.getPosTag()) && !EXCLUDE_WORDS.contains(word.getText())) {
					lemmas.add(word);
				}

			}
		}

		lemmas = removeRepeatingOccurences(lemmas);

		return lemmas;
	}

	private List<Word> removeRepeatingOccurences(List<Word> lemmas) {
		HashMap<String, Word> map = new HashMap<>();
		for (var lemma : lemmas) {
			Word exists = map.get(lemma.getText());
			if (exists != null) {
				exists.increaseScore();
			} else {
				map.put(lemma.getText(), lemma);
			}
		}
		return new ArrayList<Word>(map.values());
	}

	public List<Word> parse(String text) {
		var lemmas = lemmatize(text);

		Collections.sort(lemmas, new Comparator<Word>() {

			@Override
			public int compare(Word o1, Word o2) {

				return o2.getScore() - o1.getScore();
			}
		});
		return lemmas;
	}

}