package rss.reader.web;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import rss.reader.model.RssItem;
import rss.reader.model.Word;
import rss.reader.nls.Scorer;

public class TemplateHtmlGenerator {

	private static Logger logger = LoggerFactory.getLogger(TemplateHtmlGenerator.class);

	private static final String RSS_DEBUG_FTL = "rssDebug.ftl";
	private static final String RSS_FTL = "rss.ftl";
	private Configuration cfg;
	private Template rssTemplate;
	private Template rssDebugTemplate;

	public TemplateHtmlGenerator() throws IOException {

		cfg = new Configuration();

		// Where do we load the templates from:
		cfg.setClassForTemplateLoading(TemplateHtmlGenerator.class, "/templates");

		// Some other recommended settings:

		cfg.setDefaultEncoding("UTF-8");
		cfg.setLocale(Locale.US);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		rssTemplate = cfg.getTemplate(RSS_FTL);
		rssDebugTemplate = cfg.getTemplate(RSS_DEBUG_FTL);
	}

	public String getRssItemListHtml(List<RssItem> rssItemList) throws TemplateException, IOException {
		HashMap<String, Object> rootMap = new HashMap<>();
		rootMap.put("rssItemList", rssItemList);
		Writer out = new StringWriter();
		
		//System.out.println(rootMap);
		
		rssTemplate.process(rootMap, out);
		return out.toString();
	}

	public String getRssItemHtml(RssItem rssItem) throws TemplateException, IOException {
		HashMap<String, Object> rootMap = new HashMap<>();
		rootMap.put("rss", rssItem);
		Writer out = new StringWriter();
		rssDebugTemplate.process(rootMap, out);
		return out.toString();
	}

	public String getRssSearchResults(String searchTerm, List<Word> searchWords, List<RssItem> rssItemList)
			throws TemplateException, IOException {
		HashMap<String, Object> rootMap = new HashMap<>();

		List<RssItem> searchResultList = new ArrayList<RssItem>();

		for (var item : rssItemList) {
			var score = Scorer.calculateScore(new HashSet<Word>(searchWords), item.getKeywordSet());
			if (score.getScore() > 0) {
			//	item.setScore(score.getScore());
				// item.setSearchScore(score);
				searchResultList.add(item);
			}
		}

		//Collections.sort(searchResultList);

		rootMap.put("searchTerm", searchTerm);
		rootMap.put("rssItemList", searchResultList);
		Writer out = new StringWriter();
		rssTemplate.process(rootMap, out);
		return out.toString();

	}

	public String findSimilar(Long id, List<RssItem> rssItemList) throws TemplateException, IOException {

		HashMap<String, Object> rootMap = new HashMap<>();

		List<RssItem> searchResultList = new ArrayList<RssItem>();

		logger.info("Id to find = " + id);

		RssItem found = rssItemList.stream().filter(item -> {

			return item.getId().equals(id);
			
		}).findAny().orElse(null);

		var html = "id = " + id + ", not found";

		for (var item : rssItemList) {
			var score = Scorer.calculateScore(found.getKeywordSet(), item.getKeywordSet());
			if (score.getScore() > 0) {
			//	item.setScore(score.getScore());
			//	item.setSearchScore(score);
				searchResultList.add(item);
			}
		}

		//Collections.sort(searchResultList);

		rootMap.put("rssItemList", searchResultList);
		Writer out = new StringWriter();
		rssTemplate.process(rootMap, out);
		return out.toString();

	}

}
