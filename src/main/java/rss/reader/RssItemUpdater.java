package rss.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rss.dao.RssDAO;
import rss.dao.RssFeedDefinitionDAO;
import rss.reader.downloader.ArticleDownloader;
import rss.reader.model.RssFeedDefinition;
import rss.reader.model.RssItem;
import rss.reader.model.Word;

public class RssItemUpdater {
	Logger logger = LoggerFactory.getLogger(RssItemUpdater.class);
	private ArticleDownloader downloader;
	private RssFeedDefinitionDAO rssFeedDefinitionDAO;
	private RssDAO rssDAO;
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

	public RssItemUpdater(ArticleDownloader downloader, RssFeedDefinitionDAO rssFeedDefinitionDAO, RssDAO rssDAO) {
		super();
		this.downloader = downloader;
		this.rssFeedDefinitionDAO = rssFeedDefinitionDAO;
		this.rssDAO = rssDAO;
	}

	public void initFeeds() {
		rssFeedDefinitionDAO//
				.getRssFeedList()//
				.stream()//
				.forEach(//
						feedDefinition -> executor.scheduleAtFixedRate(//
								() -> updateFeed(feedDefinition), //
								0, //
								feedDefinition.getUpdateIntervalMinutes(), //
								TimeUnit.MINUTES));
	}

	/**
	 * Adds new RSS Items to the feed
	 * 
	 * @param feedDefinition the {@link RssFeedDefinition}
	 */
	private void updateFeed(RssFeedDefinition feedDefinition) {
		try {

			logger.info("Update Feed: " + feedDefinition);

			List<RssItem> newRssItemList = new ArrayList<>();

			newRssItemList = RssReader.readUrl(feedDefinition);

			//System.out.println("\r\n\r\noriginal"+newRssItemList);
			
			newRssItemList = excludeAlreadyExistingItems(newRssItemList);

			//System.out.println("\r\n\r\nExclude:"+newRssItemList);

			
			newRssItemList//
					.stream()//
					.forEach(//
							rssItem -> updateKeyWord(rssItem));

			rssDAO.insertRssItemList(newRssItemList);

		} catch (Throwable e) {
			logger.info("Feed update failed: " + feedDefinition.toString(), e);
			e.printStackTrace();
		}
	}

	/**
	 * Removes {@link RssItem}s from {@code newRssItemList} which are already exists
	 * in the DB
	 * 
	 * @param newRssItemList the list of items to
	 */
	private List<RssItem> excludeAlreadyExistingItems(List<RssItem> newRssItemList) {

		Map<String, RssItem> map = newRssItemList//
				.stream()//
				.collect(//
						Collectors.toMap(//
								RssItem::getGuid, Function.identity()));

		Set<String> rssItemSet = newRssItemList//
				.stream()//
				.map(//
						item -> item.getGuid())//
				.collect(//
						Collectors.toSet());

		List<String> newGuidList = rssDAO.existsInDB(rssItemSet);

		var result = newGuidList//
				.stream()//
				.map(//
						guid -> map.get(guid))//
				.collect(Collectors.toList());

		return result;
	}

	private void updateKeyWord(RssItem rssItem) {
		List<Word> tagList;
		try {
			tagList = downloader.getTags(rssItem.getLink(), rssItem.getJsoupCssSelector());
			var tags = new HashSet<Word>(tagList);
			rssItem.setKeywordSet(tags);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
