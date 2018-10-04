package rss.reader.web.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.bson.Document;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import freemarker.template.TemplateException;
import rss.dao.RssDAO;
import rss.dao.RssFeedDefinitionDAO;
import rss.reader.RssItemUpdater;
import rss.reader.downloader.ArticleDownloader;
import rss.reader.model.RssItem;
import rss.reader.nls.PosTaggerAndLemmatizer;
import rss.reader.web.TemplateHtmlGenerator;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class Tester {
	private static Logger logger = LoggerFactory.getLogger(Tester.class);

	public static void main(String[] args) throws IOException {

		TemplateHtmlGenerator templateHtmlGenerator = new TemplateHtmlGenerator();

		ArticleDownloader downloader = new ArticleDownloader(null);

		Spark.staticFileLocation("/static");
		Spark.setPort(81);

		Spark.get(new Route("/") {

			@Override
			public Object handle(final Request request, final Response response) {

				String html = "";

				try {

					var url = request.queryParams("url");
					var selector = request.queryParams("selector");
					var format = "html";

					var content = "";

					html = templateHtmlGenerator.applyJSoupSelectorToURL(url, selector, content, format);

				} catch (Exception e) {
					logger.info("", e);
					return "Error: " + e;

				}
				return html;
			}
		});

		Spark.post(new Route("/") {

			@Override
			public Object handle(final Request request, final Response response) {

				String html = "";

				try {

					var url = request.queryParams("url");
					var selector = request.queryParams("selector");
					var format = request.queryParams("format");

					if (selector == null || selector.trim().length() == 0) {
						selector = "html";
					}

					String downloaded = downloader.getHTMLWithSelector(url);

					var parsed = Jsoup.parse(downloaded).select(selector);

					//System.out.println(parsed);
					
					String content = "";

					switch (format) {
					case "text":

						StringBuilder sb = new StringBuilder();
						for (var element : parsed) {
							sb.append(element.text() + "\r\n");
						}
						content = sb.toString();

						break;
					case "html":
						content = "<pre>" + StringEscapeUtils.escapeHtml4(parsed.html()) + "</pre>";
						break;
					}

					html = templateHtmlGenerator.applyJSoupSelectorToURL(url, selector, content, format);

				} catch (Exception e) {
					logger.info("", e);
					return "Error: " + e;

				}
				return html;
			}
		});
	}
}
