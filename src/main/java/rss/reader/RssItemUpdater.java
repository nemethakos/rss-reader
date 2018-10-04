package rss.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rss.dao.RssDAO;
import rss.dao.RssFeedDefinitionDAO;
import rss.reader.downloader.ArticleDownloader;
import rss.reader.model.ImageDescriptor;
import rss.reader.model.RssFeedDefinition;
import rss.reader.model.RssItem;
import rss.reader.model.RssMedia;
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
		executor.scheduleAtFixedRate(() -> {

			rssFeedDefinitionDAO//
					.getRssFeedList()//
					.stream()//
					.forEach(
							feedDefinition -> executor.schedule(() -> updateFeed(feedDefinition), 1, TimeUnit.SECONDS));
		}, 0, 1, TimeUnit.MINUTES);
	}

	/**
	 * Adds new RSS Items to the feed
	 * 
	 * @param feedDefinition the {@link RssFeedDefinition}
	 */
	private void updateFeed(RssFeedDefinition feedDefinition) {
		try {

			logger.info("Update Feed: " + feedDefinition.getUrl());

			List<RssItem> newRssItemList = new ArrayList<>();

			newRssItemList = RssReader.readUrl(feedDefinition);

			// System.out.println("\r\n\r\noriginal"+newRssItemList);

			newRssItemList = excludeAlreadyExistingItems(newRssItemList);

			// System.out.println("\r\n\r\nExclude:"+newRssItemList);

			newRssItemList//
					.stream()//
					.forEach(//
							rssItem -> updateArticle(rssItem));

			rssDAO.insertRssItemList(feedDefinition, newRssItemList);

		} catch (Throwable e) {
			logger.info("Feed update failed: " + feedDefinition.getUrl(), e);
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

	private void updateArticle(RssItem rssItem) {
		List<Word> tagList;
		try {
			Document doc = downloader.downloadArticle(rssItem.getLink());
			String html = downloader.getHtmlOfArticle(doc, rssItem.getJsoupCssSelector());

			List<ImageDescriptor> images = downloader.getImages(rssItem.getLink(), doc, rssItem.getJsoupCssSelector());
			if (rssItem.getMedia() == null) {
				rssItem.setMedia(selectBestImage(images));
			}
			rssItem//
					.setRssMediaList(//
							images//
									.stream()//
									.map(imageDescriptor -> getRssMediaFromImageDescriptor(imageDescriptor))//
									.collect(Collectors.toList()));
			// }

			tagList = downloader.getTags(html);
			var tags = new ArrayList<Word>(tagList);
			rssItem.setKeywordList(tags);
		} catch (Exception e) {
			logger.error("Error processing Rss item: " + rssItem, e);
		}

	}

	private RssMedia selectBestImage(List<ImageDescriptor> imageDescriptorList) {
		if (imageDescriptorList.size() == 0) {
			return null;
		} else if (imageDescriptorList.size() == 1) {
			return getRssMediaFromImageDescriptor(imageDescriptorList.get(0));
		}
		Collections.sort(imageDescriptorList, (ImageDescriptor a, ImageDescriptor b) -> {
			// System.out.println("Sort: A:" + a + ", B:" + b);
			var sizeA = a.getImageData().getWidth() * a.getImageData().getHeight();
			var sizeB = b.getImageData().getWidth() * b.getImageData().getHeight();
			return sizeB - sizeA;
		});
		var bestImage = imageDescriptorList.get(0);
		return getRssMediaFromImageDescriptor(bestImage);
	}

	private RssMedia getRssMediaFromImageDescriptor(ImageDescriptor image) {
		return RssMedia//
				.builder()//
				.withLink(image.getUrl())//
				.withWidth(image.getImageData().getWidth())//
				.withHeight(image.getImageData().getHeight())//
				.build();
	}

}
