package rss.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import rss.reader.model.RssFeedDefinition;

public class RssFeedDefinitionDAO {
	private final MongoCollection<Document> rssSourceCollection;

	public RssFeedDefinitionDAO(MongoCollection<Document> rssSourceCollection) {
		super();
		this.rssSourceCollection = rssSourceCollection;
	}

	public List<RssFeedDefinition> getRssFeedList() {

		List<RssFeedDefinition> result = new ArrayList<>();
		
		List<Document> rssFeedList = rssSourceCollection.find().into(new ArrayList<Document>());
		for (var feed:rssFeedList) {

			var rssFeedDefinition = RssFeedDefinition.builder()
			.withJsoupCssSelector(feed.getString("jsoupCssSelector"))
			.withSourceImageUrl(feed.getString("sourceImageUrl"))
			.withSourceName(feed.getString("sourceName"))
			.withUpdateIntervalMinutes(feed.getInteger("updateIntervalMinutes"))
			.withUrl(feed.getString("url"))
			.build();		
			
			result.add(rssFeedDefinition);

		}


		return result;
	}

}