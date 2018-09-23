package rss.dao;

import java.util.Date;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

class RssFeedDefinitionDAOTest {
	final MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
	final MongoDatabase rssDatabase = mongoClient.getDatabase("rss");
	final MongoCollection<Document> rssSourceCollection = rssDatabase.getCollection("rssFeeds");
	final MongoCollection<Document> rssItemCollection = rssDatabase.getCollection("rssItems");

	RssFeedDefinitionDAO rssFeedDefinitionDAO = new RssFeedDefinitionDAO(rssSourceCollection);
	RssDAO rssDAO = new RssDAO(rssItemCollection);

	@Test
	void testRssFeedDefinitionDAO() {
		// fail("Not yet implemented");
	}

	@Test
	void testUpdateLastBuildDate() {
		Date lastBuildDate = new Date();
		var list = rssFeedDefinitionDAO.getRssFeedList();
		for (var feed : list) {

			feed.setLastBuildDate(lastBuildDate);
			rssFeedDefinitionDAO.updateLastBuildDate(feed);
		}
	}

	@Test
	void testGetRssFeedList() {
		// fail("Not yet implemented");
		System.out.println(rssFeedDefinitionDAO.getRssFeedList());
	}

}
