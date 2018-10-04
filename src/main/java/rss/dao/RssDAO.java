package rss.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Sorts;

import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

import rss.reader.model.RssFeedDefinition;
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
	 * @param feedDefinition 
	 * 
	 * @param rssItem the {@link RssItem}
	 * @return
	 *         <li>{@code true} when the item was not found in the DB and was
	 *         inserted,
	 *         <li>{@code false} if the item was not inserted.
	 */
	public void insertRssItemList(RssFeedDefinition feedDefinition, List<RssItem> rssItemList) {
		if (rssItemList.size() > 0) {
			System.out.println("\r\nInserting " + rssItemList.size() + " Documents to " + feedDefinition.getUrl());
			rssCollection.insertMany(getDocumentListForRssItemList(rssItemList));
		}
	}

	private List<Document> getDocumentListForRssItemList(List<RssItem> rssItemList) {
		List<Document> result = new ArrayList<>();

		for (var rssItem : rssItemList) {
			result.add(getDocument(rssItem));
		}

		return result;
	}

	// private void updateRssItemWithSearchWords(String id, )

	private Document getDocument(RssItem rssItem) {
		Document doc = new Document("guid", rssItem.getGuid());
		doc.append("description", rssItem.getDescription());
		doc.append("categoryList", rssItem.getCategoryList());
		doc.append("keywordSet", getDocumentFromKeywordList(rssItem.getKeywordList()));
		doc.append("link", rssItem.getLink());
		doc.append("media", getDocument(rssItem.getMedia()));
		doc.append("providerImage", rssItem.getProviderImage());
		doc.append("providerName", rssItem.getProviderName());
		doc.append("publicationDate", rssItem.getPublicationDate());
		doc.append("rssMediaList", getDocument(rssItem.getRssMediaList()));
		doc.append("stringifiedEntry", rssItem.getStringifiedEntry());
		doc.append("title", rssItem.getTitle());
		return doc;
	}

	private List<Document> getDocument(List<RssMedia> rssMediaList) {
		return rssMediaList//
				.stream()//
				.map(media -> getDocument(media))//
				.collect(Collectors.toList());
	}

	private List<Document> getDocumentFromKeywordList(List<Word> keywordList) {
		return keywordList.stream().map(word -> {//
			return new Document("text", word.getText())//
					.append("posTag", word.getPosTag())//
					.append("score", word.getScore());
		}).collect(Collectors.toList());

	}

	private List<Document> getDocumentListForRssMediaList(List<RssMedia> rssMediaList) {
		return rssMediaList//
				.stream()//
				.filter(Objects::nonNull)//
				.map(rssMedia -> getDocument(rssMedia))//
				.collect(Collectors.toList());
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

		return wordList//
				.stream()//
				.map(word -> getDocument(word))//
				.collect(Collectors.toList());
	}

	public List<String> existsInDB(Set<String> guidList) {

		Document filter = new Document("guid", new Document("$in", guidList));

		FindIterable<Document> foundRssItems = rssCollection//
				.find(filter)//
				.projection(//
						fields(include("guid"), excludeId()))//
				.batchSize(guidList.size());//

		MongoCursor<Document> iterator = foundRssItems.iterator();
		while (iterator.hasNext()) {
			var doc = iterator.next();
			var guid = doc.getString("guid");
			guidList.remove(guid);
		}

		return new ArrayList<String>(guidList);
	}

	public List<RssItem> getLastRssItemList(int number) {
		List<RssItem> result = new ArrayList<>();
		MongoCursor<Document> cursor = //
				rssCollection//
						.find()//
						.sort(Sorts.orderBy(Sorts.descending("publicationDate")))//
						.limit(number)//
						.iterator();
		while (cursor.hasNext()) {
			Document d = cursor.next();
			result.add(getRssItemFromDocument(d));
		}

		return result;
	}

	protected RssItem getRssItemFromDocument(Document document) {
		return RssItem.builder()//
				.withId(document.getObjectId("_id").toString())//
				.withCategoryList(getCategoryListFromDocument(document))//
				.withDescription(document.getString("description"))//
				.withGuid(document.getString("guid"))//
				.withKeywordList(getKeywordSetFromDocument(document))//
				.withLink(document.getString("link"))//
				.withMedia(getRssMediaFromDocument((Document) document.get("media")))//
				.withProviderImage(document.getString("providerImage"))//
				.withProviderName(document.getString("providerName"))//
				.withPublicationDate(document.getDate("publicationDate"))//
				.withTitle(document.getString("title"))//
				.withStringifiedEntry(document.getString("stringifiedEntry"))//
				.withRssMediaList(getRssMediaListFromDocument(document))//
				.build();

	}

	@SuppressWarnings("unchecked")
	private List<RssMedia> getRssMediaListFromDocument(Document document) {
		List<RssMedia> result = new ArrayList<>();
		if (document != null) {
			List<Document> rssMediaList = (List<Document>) document.get("rssMediaList");
			if (rssMediaList != null) {
				result = rssMediaList//
						.stream()//
						.filter(Objects::nonNull)//
						.map(doc -> getRssMediaFromDocument(doc))//
						.collect(Collectors.toList());
			}
		}
		return result;
	}

	private RssMedia getRssMediaFromDocument(Document d) {
		return RssMedia.builder()//
				.withWidth(d.getInteger("width"))//
				.withHeight(d.getInteger("height"))//
				.withLink(d.getString("link"))//
				.build();
	}

	@SuppressWarnings("unchecked")
	private List<String> getCategoryListFromDocument(Document d) {
		return (List<String>) d.get("categoryList");
	}

	@SuppressWarnings("unchecked")
	protected List<Word> getKeywordSetFromDocument(Document document) {
		return ((List<Document>) document.get("keywordSet"))//
				.stream()//
				.map(doc -> new Word(//
						doc.getString("text"), //
						doc.getString("posTag"), //
						doc.getInteger("score")))
				.collect(//
						Collectors.toList());

	}

	public RssItem findById(String id) {
		var doc = rssCollection.find(new Document("_id", new ObjectId(id))).first();
		if (doc != null) {
			return getRssItemFromDocument(doc);
		} else {
			return null;
		}
	}

	public List<RssItem> findByKeywords(List<Word> searchWords, int maxNumberOfResults) {
		List<String> keywordSet = searchWords//
				.stream()//
				.map(word -> word.getText())//
				.collect(Collectors.toList());

		return rssCollection//
				.find(in("keywordSet.text", keywordSet))//
				.sort(Sorts.orderBy(Sorts.descending("publicationDate")))//
				.limit(maxNumberOfResults)//
				.map(doc -> getRssItemFromDocument(doc))//
				.into(new ArrayList<RssItem>());
	}

}
