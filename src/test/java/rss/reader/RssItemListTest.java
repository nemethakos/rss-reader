package rss.reader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import rss.dao.RssFeedDefinitionDAO;
import rss.reader.downloader.ArticleDownloader;
import rss.reader.model.RssItem;
import rss.reader.nls.PosTaggerAndLemmatizer;
import rss.reader.web.FileHtmlGenerator;

class RssItemListTest {

	@Test
	void test() throws IOException, URISyntaxException {
		final MongoClient mongoClient =  MongoClients.create("mongodb://localhost:27017");
		final MongoDatabase rssDatabase = mongoClient.getDatabase("rss");
		final MongoCollection<Document> rssSourceCollection = rssDatabase.getCollection("rssFeeds");

		RssFeedDefinitionDAO rssFeedDefinitionDAO = new RssFeedDefinitionDAO(rssSourceCollection);
		RssItemList rssItemList = new RssItemList(new ArticleDownloader(PosTaggerAndLemmatizer.getInstance()), rssFeedDefinitionDAO, null);

		List<RssItem> userFeed = rssItemList.getFeedsSyncronously();

		FileHtmlGenerator.writeHtml("c:/doc/rss/index.html", userFeed);
	}

}
