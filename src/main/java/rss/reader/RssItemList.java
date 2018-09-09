package rss.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rss.dao.RssDAO;
import rss.dao.RssFeedDefinitionDAO;
import rss.reader.downloader.ArticleDownloader;
import rss.reader.model.RssFeedDefinition;
import rss.reader.model.RssItem;
import rss.reader.model.Word;

public class RssItemList {
	Logger logger = LoggerFactory.getLogger(RssItemList.class);
	private ArticleDownloader downloader;
	private RssFeedDefinitionDAO rssFeedDefinitionDAO;
	private RssDAO rssDAO;

	public RssItemList(ArticleDownloader downloader, RssFeedDefinitionDAO rssFeedDefinitionDAO, RssDAO rssDAO) {
		super();
		this.downloader = downloader;
		this.rssFeedDefinitionDAO = rssFeedDefinitionDAO;
		this.rssDAO = rssDAO;

		feedDefinitionList = this.rssFeedDefinitionDAO.getRssFeedList();
	}

	List<RssItem> rssItemList = Collections.synchronizedList(new ArrayList<RssItem>());

	List<RssFeedDefinition> feedDefinitionList;

	ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

	ExecutorService keywordUpdater = Executors.newFixedThreadPool(10);

	public List<RssItem> getRssItemList() {
		return new ArrayList<RssItem>(rssItemList);
	}

	public void initFeeds() {

		for (RssFeedDefinition feedDefinition : feedDefinitionList) {
			logger.info("Schedule Feed: " + feedDefinition);
			executor.scheduleAtFixedRate(() -> {
				updateFeed(feedDefinition);
			}, 0, feedDefinition.getUpdateIntervalMinutes(), TimeUnit.MINUTES);

		}

	}

	/*
	 * public void updateKeywordsForRssItem(RssItem rssItem) { try {
	 * 
	 * List<Word> keywords = downloader.getTags(rssItem.getLink(),
	 * rssItem.getJsoupCssSelector());
	 * 
	 * rssItem.setKeywordSet(keywords);
	 * 
	 * } catch (IOException e) {
	 * 
	 * e.printStackTrace();
	 * 
	 * logger.error("Error while creating keywords for " + rssItem.toString(), e); }
	 * }
	 */

	public List<RssItem> getFeedsSyncronously() {

		for (RssFeedDefinition feedDefinition : feedDefinitionList) {
			logger.info("Schedule Feed: " + feedDefinition);

			updateFeed(feedDefinition);

		}

		return getRssItemList();
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



			newRssItemList.removeAll(rssItemList);

			rssDAO.insertRssItemList(newRssItemList);

			
			pushNewRssItemsToKeywordUpdater(newRssItemList);

			rssItemList.addAll(newRssItemList);

			rssItemList.sort(new Comparator<RssItem>() {

				@Override
				public int compare(RssItem o1, RssItem o2) {
					if (o1.getPublicationDate() != null && o2.getPublicationDate() != null) {
						return o2.getPublicationDate().compareTo(o1.getPublicationDate());
					} else {
						return 0;
					}

				}
			});

		} catch (Throwable e) {
			logger.info("Feed update failed: " + feedDefinition.toString(), e);
			e.printStackTrace();
		}
	}

	private void pushNewRssItemsToKeywordUpdater(List<RssItem> newRssItemList) {
		for (var rssItem : newRssItemList) {
			keywordUpdater.execute(updateKeyWord(rssItem));
		}

	}

	private Runnable updateKeyWord(final RssItem rssItem) {
		return () -> {
			logger.info("Updating: " + rssItem.getLink());
			try {

				List<Word> tagList = downloader.getTags(rssItem.getLink(), rssItem.getJsoupCssSelector());

				var tags = new HashSet<Word>(tagList);

				rssItem.setKeywordSet(tags);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Cannot update: " + rssItem, e);
			}
		};
	}

}
