package rss.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

import rss.reader.model.RssFeedDefinition;

public class RssFeedDefinitionDAO {
	private final MongoCollection<Document> rssSourceCollection;

	public RssFeedDefinitionDAO(MongoCollection<Document> rssSourceCollection) {
		super();
		this.rssSourceCollection = rssSourceCollection;
	}

	public void updateLastBuildDate(RssFeedDefinition rssFeedDefinition) {
		rssSourceCollection.updateOne(//
				Filters.eq("_id", new ObjectId(rssFeedDefinition.getId())), //
				Updates.set("lastBuildDate", rssFeedDefinition.getLastBuildDate()), //
				new UpdateOptions().upsert(true)//
		);
	}

	public List<RssFeedDefinition> getRssFeedList() {

		List<RssFeedDefinition> result = new ArrayList<>();

		List<Document> rssFeedList = rssSourceCollection.find().into(new ArrayList<Document>());
		for (var feed : rssFeedList) {

			var rssFeedDefinition = RssFeedDefinition.builder()
					.withId(feed.getObjectId("_id").toString())
					.withLastBuildDate(feed.getDate("lastBuildDate"))
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
