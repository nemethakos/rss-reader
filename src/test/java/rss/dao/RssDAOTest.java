package rss.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

class RssDAOTest {
	final MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
	final MongoDatabase rssDatabase = mongoClient.getDatabase("rss");
	final MongoCollection<Document> rssSourceCollection = rssDatabase.getCollection("rssFeeds");
	final MongoCollection<Document> rssItems = rssDatabase.getCollection("rssItems");

	RssFeedDefinitionDAO rssFeedDefinitionDAO = new RssFeedDefinitionDAO(rssSourceCollection);
	RssDAO rssDAO = new RssDAO(rssItems);
	
	@Test
	void testInsertRssItemList() {
		//fail("Not yet implemented");
	}

	//@Test
	void testExistsInDB() {
		Set<String> guidList = new HashSet<String>();
		
		guidList.add("http://www.foxnews.com/us/2018/09/15/police-capital-murder-charges-likely-in-texas-cops-death.html");
		guidList.add("should be returned");
		
		var found = rssDAO.existsInDB(guidList );
		
		System.out.println(found);
	}
	
	@SuppressWarnings("unchecked")
	//@Test
	void testDBQuery() {
		var doc  = rssItems.find(new Document("_id", new ObjectId("5b9d8c43218df70b688e6881"))).first();
		var catList = (List<String>)doc.get("categoryList");
		System.out.println(catList);
	}
	
	//@Test
	void testDocumentToRssItem() {
		var doc  = rssItems.find(new Document("_id", new ObjectId("5b9d8c43218df70b688e6881"))).first();
		System.out.println(doc);
		var rssItem = rssDAO.getRssItemFromDocument(doc);
		System.out.println(rssItem);
	}
	
	@Test
	void testLastNDocuments() {
		System.out.println(rssDAO.getLastRssItemList(10));
	}

}
