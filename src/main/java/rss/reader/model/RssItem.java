package rss.reader.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Generated;

import rss.reader.nls.Score;

public class RssItem implements Comparable<RssItem> {

	private List<String> categoryList = new ArrayList<>();
	private String description;
	private String guid;
	private Long id;
	private String jsoupCssSelector;
	private Set<Word> keywordSet = null;
	private String link;
	private RssMedia media;
	private String providerImage;
	private String providerName;
	private Date publicationDate;
	private List<RssMedia> rssMediaList = new ArrayList<>();
	private Long score;
	private Score searchScore;
	private String stringifiedEntry;
	private String title;

	/**
	 * Builder to build {@link RssItem}.
	 */
	@Generated("SparkTools")
	public static final class Builder {
		private List<String> categoryList = Collections.emptyList();
		private String description;
		private String guid;
		private Long id;
		private String jsoupCssSelector;
		private Set<Word> keywordSet = Collections.emptySet();
		private String link;
		private RssMedia media;
		private String providerImage;
		private String providerName;
		private Date publicationDate;
		private List<RssMedia> rssMediaList = Collections.emptyList();
		private Long score;
		private Score searchScore;
		private String stringifiedEntry;
		private String title;

		private Builder() {
		}

		public RssItem build() {
			return new RssItem(this);
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

		public Builder withId(Long id) {
			this.id = id;
			return this;
		}

		public Builder withJsoupCssSelector(String jsoupCssSelector) {
			this.jsoupCssSelector = jsoupCssSelector;
			return this;
		}

		public Builder withKeywordSet(Set<Word> keywordSet) {
			this.keywordSet = keywordSet;
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

		public Builder withScore(Long score) {
			this.score = score;
			return this;
		}

		public Builder withSearchScore(Score searchScore) {
			this.searchScore = searchScore;
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
	}
	/**
	 * Creates builder to build {@link RssItem}.
	 * @return created builder
	 */
	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}
	@Generated("SparkTools")
	private RssItem(Builder builder) {
		this.categoryList = builder.categoryList;
		this.description = builder.description;
		this.guid = builder.guid;
		this.id = builder.id;
		this.jsoupCssSelector = builder.jsoupCssSelector;
		this.keywordSet = builder.keywordSet;
		this.searchScore = builder.searchScore;
		this.link = builder.link;
		this.media = builder.media;
		this.providerImage = builder.providerImage;
		this.providerName = builder.providerName;
		this.publicationDate = builder.publicationDate;
		this.rssMediaList = builder.rssMediaList;
		this.score = builder.score;
		this.stringifiedEntry = builder.stringifiedEntry;
		this.title = builder.title;
	}

	@Override
	public int compareTo(RssItem o) {
		if (o != null) {

			return (int) (o.getScore() - this.getScore());
		}
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RssItem other = (RssItem) obj;
		if (guid == null) {
			if (other.guid != null)
				return false;
		} else if (!guid.equals(other.guid))
			return false;
		return true;
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

	public Long getId() {
		return id;
	}

	public String getJsoupCssSelector() {
		return jsoupCssSelector;
	}

	public Set<Word> getKeywordSet() {
		return keywordSet;
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

	public Long getScore() {
		return score;
	}

	public Score getSearchScore() {
		return searchScore;
	}

	public String getStringifiedEntry() {
		return stringifiedEntry;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((guid == null) ? 0 : guid.hashCode());
		return result;
	}

	public boolean hasKeywords() {
		return keywordSet != null;
	}

	public void setKeywordSet(Set<Word> keywordSet) {
		this.keywordSet = keywordSet;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public void setSearchScore(Score searchScore) {
		this.searchScore = searchScore;
	}

	@Override
	public String toString() {
		return "RssItem [categoryList=" + categoryList + ", description=" + description + ", guid=" + guid + ", id="
				+ id + ", jsoupCssSelector=" + jsoupCssSelector + ", link=" + link + ", media=" + media
				+ ", providerImage=" + providerImage + ", providerName=" + providerName + ", publicationDate="
				+ publicationDate + ", rssMediaList=" + rssMediaList + ", score=" + score + ", title=" + title + "]";
	}
}
