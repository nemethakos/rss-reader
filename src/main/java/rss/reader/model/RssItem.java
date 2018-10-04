package rss.reader.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Generated;

/**
 * Represents an entry from RSS Feed
 */
public class RssItem {

	/**
	 * List of categories
	 */
	private List<String> categoryList = new ArrayList<>();
	
	@Override
	public String toString() {
		StringBuilder builder2 = new StringBuilder();
		builder2.append("RssItem [categoryList=").append(categoryList).append(", description=").append(description)
				.append(", guid=").append(guid).append(", id=").append(id).append(", jsoupCssSelector=")
				.append(jsoupCssSelector).append(", keywordList=").append(keywordList).append(", link=").append(link)
				.append(", media=").append(media).append(", providerImage=").append(providerImage)
				.append(", providerName=").append(providerName).append(", publicationDate=").append(publicationDate)
				.append(", rssMediaList=").append(rssMediaList).append(", stringifiedEntry=").append(stringifiedEntry)
				.append(", title=").append(title).append("]");
		return builder2.toString();
	}

	/**
	 * Description of RSS entry
	 */
	private String description;
	
	/**
	 * GUID of RSS entry
	 */
	private String guid;
	
	/**
	 * MongoDB objectId
	 */
	private String id;
	
	public List<Word> getKeywordList() {
		return keywordList;
	}

	public void setKeywordList(List<Word> keywordList) {
		this.keywordList = keywordList;
	}

	private String jsoupCssSelector;
	
	/**
	 * Set of keywords extracted from the article body
	 */
	private List<Word> keywordList = new ArrayList<>();
	
	/**
	 * Link to the article
	 */
	private String link;
	
	/**
	 * Selected image for artickle
	 */
	private RssMedia media;
	
	/**
	 * Link to the image of the provider
	 */
	private String providerImage;
	
	/**
	 * Name of the provider (e.g.: BBC)
	 */
	private String providerName;
	
	/**
	 * Publication date from RSS entry
	 */
	private Date publicationDate;
	
	/**
	 * Not saved to DB
	 */
	private List<RssMedia> rssMediaList;
	
	/**
	 * Textual representation for debugging
	 */
	private String stringifiedEntry;

	/**
	 * Title of RSS entry
	 */
	private String title;

	@Generated("SparkTools")
	private RssItem(Builder builder) {
		this.categoryList = builder.categoryList;
		this.description = builder.description;
		this.guid = builder.guid;
		this.id = builder.id;
		this.jsoupCssSelector = builder.jsoupCssSelector;
		this.keywordList = builder.keywordList;
		this.link = builder.link;
		this.media = builder.media;
		this.providerImage = builder.providerImage;
		this.providerName = builder.providerName;
		this.publicationDate = builder.publicationDate;
		this.rssMediaList = builder.rssMediaList;
		this.stringifiedEntry = builder.stringifiedEntry;
		this.title = builder.title;
	}

	public List<String> getCategoryList() {
		return categoryList;
	}
	public String getDescription() {
		return description;
	}

	public String getGuid() {
		return guid;
	}
	
	public String getId() {
		return id;
	}
	
	public String getLink() {
		return link;
	}
	public RssMedia getMedia() {
		return media;
	}
	public String getProviderImage() {
		return providerImage;
	}
	public String getProviderName() {
		return providerName;
	}
	public Date getPublicationDate() {
		return publicationDate;
	}
	public List<RssMedia> getRssMediaList() {
		return rssMediaList;
	}
	public String getStringifiedEntry() {
		return stringifiedEntry;
	}
	public String getTitle() {
		return title;
	}
	public void setCategoryList(List<String> categoryList) {
		this.categoryList = categoryList;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	public void setLink(String link) {
		this.link = link;
	}
	

	public void setMedia(RssMedia media) {
		this.media = media;
	}
	public void setProviderImage(String providerImage) {
		this.providerImage = providerImage;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}
	public void setRssMediaList(List<RssMedia> rssMediaList) {
		this.rssMediaList = rssMediaList;
	}
	public void setStringifiedEntry(String stringifiedEntry) {
		this.stringifiedEntry = stringifiedEntry;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getJsoupCssSelector() {
		return jsoupCssSelector;
	}

	public void setJsoupCssSelector(String jsoupCssSelector) {
		this.jsoupCssSelector = jsoupCssSelector;
	}

	/**
	 * Creates builder to build {@link RssItem}.
	 * @return created builder
	 */
	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder to build {@link RssItem}.
	 */
	@Generated("SparkTools")
	public static final class Builder {
		private List<String> categoryList = Collections.emptyList();
		private String description;
		private String guid;
		private String id;
		private String jsoupCssSelector;
		private List<Word> keywordList = Collections.emptyList();
		private String link;
		private RssMedia media;
		private String providerImage;
		private String providerName;
		private Date publicationDate;
		private List<RssMedia> rssMediaList = Collections.emptyList();
		private String stringifiedEntry;
		private String title;

		private Builder() {
		}

		public Builder withCategoryList(List<String> categoryList) {
			this.categoryList = categoryList;
			return this;
		}

		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder withGuid(String guid) {
			this.guid = guid;
			return this;
		}

		public Builder withId(String id) {
			this.id = id;
			return this;
		}

		public Builder withJsoupCssSelector(String jsoupCssSelector) {
			this.jsoupCssSelector = jsoupCssSelector;
			return this;
		}

		public Builder withKeywordList(List<Word> keywordList) {
			this.keywordList = keywordList;
			return this;
		}

		public Builder withLink(String link) {
			this.link = link;
			return this;
		}

		public Builder withMedia(RssMedia media) {
			this.media = media;
			return this;
		}

		public Builder withProviderImage(String providerImage) {
			this.providerImage = providerImage;
			return this;
		}

		public Builder withProviderName(String providerName) {
			this.providerName = providerName;
			return this;
		}

		public Builder withPublicationDate(Date publicationDate) {
			this.publicationDate = publicationDate;
			return this;
		}

		public Builder withRssMediaList(List<RssMedia> rssMediaList) {
			this.rssMediaList = rssMediaList;
			return this;
		}

		public Builder withStringifiedEntry(String stringifiedEntry) {
			this.stringifiedEntry = stringifiedEntry;
			return this;
		}

		public Builder withTitle(String title) {
			this.title = title;
			return this;
		}

		public RssItem build() {
			return new RssItem(this);
		}
	}

}
