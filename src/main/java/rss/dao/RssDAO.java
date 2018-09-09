package rss.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import rss.reader.model.RssItem;
import rss.reader.model.RssMedia;
import rss.reader.model.Word;
import rss.reader.nls.Score;

public class RssDAO {
	private final MongoCollection<Document> rssCollection;

	public RssDAO(MongoCollection<Document> rssCollection) {
		super();
		this.rssCollection = rssCollection;
	}

	/**
	 * Inserts the {@link RssItem}, if that does not exists yet in the DB based on
	 * {@link RssItem#getGuid()}
	 * 
	 * @param rssItem the {@link RssItem}
	 * @return
	 *         <li>{@code true} when the item was not found in the DB and was
	 *         inserted,
	 *         <li>{@code false} if the item was not inserted.
	 */
	public boolean insertRssItemList(List<RssItem> rssItemList) {

		rssCollection.insertMany(getDocumentListForRssItemList(rssItemList));

		return false;
	}

	private List<Document> getDocumentListForRssItemList(List<RssItem> rssItemList) {
		List<Document> result = new ArrayList<>();

		for (var rssItem : rssItemList) {
			result.add(getDocument(rssItem));
		}

		return result;
	}
	
	//private void updateRssItemWithSearchWords(String id, )

	private Document getDocument(RssItem rssItem) {
		Document doc = new Document("guid", rssItem.getGuid());
		doc.append("description", rssItem.getDescription());
		doc.append("jsoupCssSelector", rssItem.getJsoupCssSelector());
		doc.append("link", rssItem.getLink());
		doc.append("media", getDocument(rssItem.getMedia()));
		doc.append("providerImage", rssItem.getProviderImage());
		doc.append("providerName", rssItem.getProviderName());
		doc.append("publicationDate", rssItem.getPublicationDate());
		doc.append("rssMediaList", getDocumentListForRssMediaList(rssItem.getRssMediaList()));
		doc.append("stringifiedEntry", rssItem.getStringifiedEntry());
		doc.append("title", rssItem.getTitle());
		return doc;
	}

	private List<Document> getDocumentListForRssMediaList(List<RssMedia> rssMediaList) {
		List<Document> result = new ArrayList<Document>();

		for (var rssMedia : rssMediaList) {
			result.add(getDocument(rssMedia));
		}

		return result;
	}

	private Document getDocument(RssMedia rssMedia) {
		Document doc = new Document();
		if (rssMedia != null) {
			doc = new Document("link", rssMedia.getLink());
			doc.append("height", rssMedia.getHeight());
			doc.append("width", rssMedia.getWidth());
			doc.append("heightWidthRatio", rssMedia.getHeightWidthRatio());
		}
		return doc;
	}

	private Document getDocument(Score searchScore) {
		Document doc = new Document();
		if (searchScore != null) {
			doc = new Document("score", searchScore.getScore());
			doc.append("wordList", getDocumentList(searchScore.getWordList()));
		}
		return doc;
	}

	private Document getDocument(Word word) {
		Document doc = new Document("text", word.getText());
		doc.append("posTag", word.getPosTag());
		doc.append("score", word.getScore());
		return doc;
	}

	private List<Document> getDocumentList(List<Word> wordList) {

		List<Document> result = new ArrayList<>();

		for (var word : wordList) {
			result.add(getDocument(word));
		}

		return result;
	}

	/**
	 * Adds the {@link List} of {@link Word}s to the {@link RssItem} in the DB.
	 * Returns the {@link RssItem} containing the keywords.
	 * 
	 * @param rssItem  the {@link RssItem}
	 * @param keywords the {@link List} of {@link Word}s
	 * @return the updated {@link RssItem}
	 */
	public RssItem addKeywords(RssItem rssItem, List<Word> keywords) {
		return rssItem;
	}

}
