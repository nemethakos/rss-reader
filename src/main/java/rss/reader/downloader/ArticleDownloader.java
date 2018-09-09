package rss.reader.downloader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rss.reader.model.Word;
import rss.reader.nls.PosTaggerAndLemmatizer;

public class ArticleDownloader {
	static Logger logger = LoggerFactory.getLogger(ArticleDownloader.class);
	PosTaggerAndLemmatizer tagger = null;
	OkHttpClient client = new OkHttpClient();
	
	public ArticleDownloader(PosTaggerAndLemmatizer tagger) {
		super();
		Objects.nonNull(tagger);
		this.tagger = tagger;
	}

	public List<String> getKeywords(String text) {
		List<String> result = new ArrayList<>();

		String[] words = text.split("\\s+");

		var removeDuplicatesSet = new HashSet<String>(Arrays.asList(words));
		
		result.addAll(removeDuplicatesSet);

		result.sort(String::compareToIgnoreCase);

		return result;
	}

	public String elementsToText(Elements elements) {
		StringBuilder sb = new StringBuilder();
		for (var element : elements) {
			sb.append(element.text() + "\r\n");
		}
		return sb.toString();
	}
	
	public String getTextFromHtmlPageUsingJsoupCssSelector(String url, String jsoupCssSelector) throws IOException {

		logger.info("Downloading: '" + url + "', selector:'" + jsoupCssSelector+"'");
		


		Request request = new Request.Builder().url(url).build();

		Response response = client.newCall(request).execute();
		var html = response.body().string();
		
		Document doc = Jsoup.parse(html);

		Elements selected = doc.select(jsoupCssSelector);
		
		var text = elementsToText(selected);
		if (text.trim().length()==0) {
			text = elementsToText(doc.select("body"));
		}
		
		return text;

	}
	
	public List<Word> getTags(String url, String jsoup) throws IOException {
		var text = getTextFromHtmlPageUsingJsoupCssSelector(url,jsoup);
		return tagger.parse(text);
	}

}
