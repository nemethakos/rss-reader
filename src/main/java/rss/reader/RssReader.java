package rss.reader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rometools.modules.mediarss.MediaEntryModule;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import rss.reader.model.RssFeedDefinition;
import rss.reader.model.RssItem;
import rss.reader.model.RssMedia;
import rss.reader.nls.PosTaggerAndLemmatizer;

public class RssReader {

	private static final int SENTENCE_NUMBER_FOR_DIGEST = 3;

	private static AtomicLong idGenerator = new AtomicLong(0);

	static Logger logger = LoggerFactory.getLogger(RssReader.class);

	static Pattern pattern = Pattern.compile("<(\\w+)( +.+)*>((.*))</\\1>");
	static Matcher matcher = pattern.matcher("<as testAttr='5'> TEST</as>");

	private static boolean containsHTML(String text) {
		if (text != null && text.length() > 0 && pattern.matcher(text).find()) {
			// for (int i = 0; i < matcher.groupCount(); i++) {
			// System.out.println(i + ":" + matcher.group(i));
			// }
			return true;
		}
		return false;
	}

	private static String removeHTMLFromText(String text) {
		String result = "No Description";

		if (text != null) {
			if (containsHTML(text)) {
				Document doc = Jsoup.parse(text);
				var p = doc.select("p").first();
				if (p != null) {
					result = p.text();
				} else {
					var generic = doc.text();
					var sentences = PosTaggerAndLemmatizer.getInstance().getSentences(generic);

					//System.out.println(sentences);
					
					var toIndex = sentences.size() >= (SENTENCE_NUMBER_FOR_DIGEST - 1)
							? (SENTENCE_NUMBER_FOR_DIGEST - 1)
							: sentences.size();

					result = sentences.subList(0, toIndex).stream().collect(Collectors.joining(" "));
				}

			} else {
				result = text;
			}
		}

		return result;
	}

	public static List<RssItem> readUrl(RssFeedDefinition feedDefinition)
			throws IllegalArgumentException, FeedException, IOException {

		var result = new ArrayList<RssItem>();
		URL feedUrl = new URL(feedDefinition.getUrl());

		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = null;
		try {
			feed = input.build(new XmlReader(feedUrl));
		} catch (Throwable e) {
			logger.error("Error processing feed: " + feedUrl.toString(), e);
		}

		//logger.info(feed.toString());

		var entries = feed.getEntries();

		String providerImage = feedDefinition.getSourceImageUrl();
		String providerName = feedDefinition.getSourceName();

		if (feed.getImage() != null) {
			if (providerImage == null) {
				providerImage = feed.getImage().getUrl();
			}
			if (providerName == null) {
				providerName = feed.getImage().getTitle();
			}

			// logger.info(providerName);
		}

		for (var entry : entries) {
			
			var categoryList = entry//
					.getCategories()//
					.stream()//
					.map(category->category.getName())//
					.collect(Collectors.toList());
			
			if (categoryList.size()==0) {
				categoryList.addAll(feedDefinition.getCategoryList());
			}
			
			//System.out.println("-------------------------------------------------------\r\n"+providerName+": "+categoryList+"\r\n");
			
			
			var syndContent = entry//
					.getContents()//
					.stream()//
					.filter(content -> content.getValue() != null)//
					.findFirst()//
					.orElse(null);
			String textSyndContent = null;
			if (syndContent != null) {
				textSyndContent = syndContent.getValue();

			}
			var stringified = entry.toString();

			var desc = entry.getDescription();
			String description = null;
			if (desc != null) {
				description = desc.getValue();

			}
			if (description == null || description.length() == 0 && textSyndContent != null) {
				description = textSyndContent;
			}

			var title = entry.getTitle();
			var link = entry.getLink();
			var guid = entry.getUri();

			List<RssMedia> rssMediaList = new ArrayList<RssMedia>();

			MediaEntryModule mediaEntryModule = (MediaEntryModule) entry.getModule(MediaEntryModule.URI);
			if (mediaEntryModule != null) {
				var rssMediaContents = mediaEntryModule.getMediaContents();
				if (rssMediaContents != null) {
					for (var mc : rssMediaContents) {
						var mediaURL = mc.getReference();
						String imageURL = null;
						if (mediaURL != null) {
							imageURL = mediaURL.toString();
						}
						var width = mc.getWidth();
						var height = mc.getHeight();
						if (imageURL != null) {
							rssMediaList.add(RssMedia//
									.builder()//
									.withLink(imageURL)//
									.withWidth(width)//
									.withHeight(height)//
									.build());//
						}
					}
				}

				var metadata = mediaEntryModule.getMetadata();
				if (metadata != null) {
					var thumbnailList = metadata.getThumbnail();
					for (var thumbnail : thumbnailList) {
						var thumbnailLink = thumbnail.getUrl();
						if (thumbnailLink != null) {
							rssMediaList.add(//
									RssMedia.builder()//
											.withLink(thumbnailLink.toString())//
											.withWidth(thumbnail.getWidth())//
											.withHeight(thumbnail.getHeight())//
											.build());
						}
					}

				}

				var groups = mediaEntryModule.getMediaGroups();
				for (var group : groups) {
					var mediaContents = group.getContents();
					for (var mediaContent : mediaContents) {

						var ref = mediaContent.getReference();
						String mediaURL = null;
						var width = mediaContent.getWidth();
						var height = mediaContent.getHeight();
						if (ref != null) {
							mediaURL = ref.toString();
							rssMediaList.add(//
									RssMedia.builder()//
											.withLink(mediaURL)//
											.withWidth(width)//
											.withHeight(height)//
											.build());

						}
					}

				}
			}

			var pubDate = entry.getPublishedDate();

			RssMedia media = selectBestMedia(rssMediaList);

			var item = RssItem.builder()//
					//
					.withDescription(removeHTMLFromText(description))//
					.withLink(link)//
					.withPublicationDate(pubDate)//
					.withRssMediaList(rssMediaList)//
					.withMedia(media)//
					.withTitle(title)//
					.withProviderImage(providerImage)//
					.withProviderName(providerName)//
					.withGuid(guid)//
					.withStringifiedEntry(stringified)//
					.withJsoupCssSelector(feedDefinition.getJsoupCssSelector())//
					.withCategoryList(categoryList)//
					.build();

			result.add(item);
		}

		return result;
	}

	private static RssMedia selectBestMedia(List<RssMedia> rssMediaList) {
		RssMedia result = null;
		if (rssMediaList != null && rssMediaList.size() > 0) {
			rssMediaList.sort(new Comparator<RssMedia>() {

				@Override
				public int compare(RssMedia o1, RssMedia o2) {
					if (!o2.hasDimension()) {
						return 1;
					}
					if (!o1.hasDimension()) {
						return -1;
					}
					if (o1.hasDimension() && o2.hasDimension()) {
						int heightWidthRatioDifference = Math
								.round(o2.getHeightWidthRatio() - o1.getHeightWidthRatio());
						if (heightWidthRatioDifference == 0) {
							return o2.getWidth() - o1.getWidth();
						} else {
							return heightWidthRatioDifference;
						}
					}
					return 0;
				}

			});
			result = rssMediaList.get(0);
		}
		return result;
	}

}
