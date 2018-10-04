package rss.reader.web.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
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

public class AppSpark {
	private static Logger logger = LoggerFactory.getLogger(AppSpark.class);

	public static void main(String[] args) throws IOException {
		final MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
		final MongoDatabase rssDatabase = mongoClient.getDatabase("rss");
		final MongoCollection<Document> rssSourceCollection = rssDatabase.getCollection("rssFeeds");
		final MongoCollection<Document> rssItemCollection = rssDatabase.getCollection("rssItems");
		rssItemCollection.drop();

		final RssFeedDefinitionDAO rssFeedDefinitionDAO = new RssFeedDefinitionDAO(rssSourceCollection);
		final RssDAO rssDAO = new RssDAO(rssItemCollection);

		TemplateHtmlGenerator templateHtmlGenerator = new TemplateHtmlGenerator();

		PosTaggerAndLemmatizer tagger = PosTaggerAndLemmatizer.getInstance();
		ArticleDownloader downloader = new ArticleDownloader(tagger);

		RssItemUpdater userFeed = new RssItemUpdater(downloader, rssFeedDefinitionDAO, rssDAO);

		userFeed.initFeeds();

		Spark.staticFileLocation("/static");
		Spark.setPort(80);

		Spark.get(new Route("/") {

			@Override
			public Object handle(Request request, Response response) {
				var lastItems = rssDAO.getLastRssItemList(100);
				var html = "";
				try {
					html = templateHtmlGenerator.getRssItemListHtml(lastItems);
				} catch (TemplateException | IOException e) {
					html = e.toString();
				}
				return html;
			}

		});
		Spark.get(new Route("/debug/:id") {

			@Override
			public Object handle(Request request, Response response) {
				String id = request.params(":id");
				logger.info("id to find = " + id);

				String html = "id: " + id + " not found!";
				RssItem rssItem = rssDAO.findById(id);

				if (rssItem != null) {

					try {
						html = templateHtmlGenerator.getRssItemHtml(rssItem);

					} catch (IOException | TemplateException e) {
						html = e.toString();
						logger.info(e.toString());
					}
				}
				return html;
			}
		});
		
		Spark.get(new Route("/find-similar/:id") {

			@Override
			public Object handle(Request request, Response response) {

				Long id = Long.valueOf(request.params(":id"));

				String html = "";
/*
				try {
					// html = templateHtmlGenerator.findSimilar(id, userFeed.getRssItemList());

				} catch (IOException | TemplateException e) {
					html = e.toString();
					logger.info(e.toString());
				}*/
				return html;
			}
		});
		  
		 /* Spark.get(new Route("/item/:id") {
		  
		  @Override public Object handle(Request request, Response response) { Long id
		  = Long.valueOf(request.params(":id")); logger.info("Id to find = " + id);
		 * RssItem found = userFeed.getRssItemList().stream().filter(item -> { //
		 * logger.info(""+item.getId()); return item.getId().equals(id);
		 * }).findAny().orElse(null); var html = "id = " + id + ", not found"; if (found
		 * != null) {
		 * 
		 * try { html = templateHtmlGenerator.getRssItemHtml(found);
		 * 
		 * } catch (IOException | TemplateException e) { html = e.toString();
		 * logger.info(e.toString()); } } return html; } });
		 * 
		 * Spark.get(new Route("/") {
		 * 
		 * @Override public Object handle(final Request request, final Response
		 * response) { String html = ""; try { html =
		 * templateHtmlGenerator.getRssItemListHtml(userFeed.getRssItemList().subList(1,
		 * 10)); } catch (IOException | TemplateException e) { logger.info("", e);
		 * return "Error reading user feed" + e;
		 * 
		 * } return html; } });
		 */ 
		Spark.post(new Route("/search") {

			@Override
			public Object handle(final Request request, final Response response) {
				String html = "";
				List<RssItem> rssItemList = new ArrayList<>();
				
				try {

					var searchTerm = request.queryParams("searchTerm");

					if (searchTerm == null || searchTerm.length() == 0) {
						response.redirect("/");
					}
					else {
						var searchWords = tagger.parse(searchTerm.toLowerCase());
						rssItemList = rssDAO.findByKeywords(searchWords, 100);
					}
					
					html = templateHtmlGenerator.getRssItemListHtml(rssItemList);
				} catch (IOException | TemplateException e) {
					logger.info("", e);
					return "Error reading user feed" + e;

				}
				return html;
			}
		});
		
	}
}
