package rss.reader.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Generated;
import java.util.Collections;

/**
 * Defines the URL, update interval, a source name (e.g.: CNN), an icon URL for
 * the source and a JSOUP CSS selector for selecting the article body from the
 * HTML page.
 *
 */
public class RssFeedDefinition {
	
	private List<String> categoryList = new ArrayList<String>();

	private String descriptionProcessingStrategy = "default";
	private boolean enabled = true;
	private String id;
	private String jsoupCssSelector;
	private Date lastBuildDate;
	private String sourceImageUrl;
	private String sourceName;
	private int updateIntervalMinutes = 60;
	private String url;
	/**
	 * Builder to build {@link RssFeedDefinition}.
	 */
	@Generated("SparkTools")
	public static final class Builder {
		private List<String> categoryList = Collections.emptyList();
		private String descriptionProcessingStrategy;
		private boolean enabled;
		private String id;
		private String jsoupCssSelector;
		private Date lastBuildDate;
		private String sourceImageUrl;
		private String sourceName;
		private int updateIntervalMinutes;
		private String url;

		private Builder() {
		}

		public RssFeedDefinition build() {
			return new RssFeedDefinition(this);
		}

		public Builder withCategoryList(List<String> categoryList) {
			this.categoryList = categoryList;
			return this;
		}

		public Builder withDescriptionProcessingStrategy(String descriptionProcessingStrategy) {
			this.descriptionProcessingStrategy = descriptionProcessingStrategy;
			return this;
		}

		public Builder withEnabled(boolean enabled) {
			this.enabled = enabled;
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

		public Builder withLastBuildDate(Date lastBuildDate) {
			this.lastBuildDate = lastBuildDate;
			return this;
		}

		public Builder withSourceImageUrl(String sourceImageUrl) {
			this.sourceImageUrl = sourceImageUrl;
			return this;
		}

		public Builder withSourceName(String sourceName) {
			this.sourceName = sourceName;
			return this;
		}

		public Builder withUpdateIntervalMinutes(int updateIntervalMinutes) {
			this.updateIntervalMinutes = updateIntervalMinutes;
			return this;
		}

		public Builder withUrl(String url) {
			this.url = url;
			return this;
		}
	}

	/**
	 * Creates builder to build {@link RssFeedDefinition}.
	 * @return created builder
	 */
	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	public static RssFeedDefinition from(String url, int updateIntervalMinutes, String sourceName,
			String sourceImageUrl) {
		return new RssFeedDefinition("body", sourceImageUrl, sourceName, updateIntervalMinutes, url);
	}

	public static RssFeedDefinition from(String url, int updateIntervalMinutes, String sourceName,
			String sourceImageUrl, String jsoupCssSelector) {
		return new RssFeedDefinition(jsoupCssSelector, sourceImageUrl, sourceName, updateIntervalMinutes, url);
	}

	@Generated("SparkTools")
	private RssFeedDefinition(Builder builder) {
		this.categoryList = builder.categoryList;
		this.descriptionProcessingStrategy = builder.descriptionProcessingStrategy;
		this.enabled = builder.enabled;
		this.id = builder.id;
		this.jsoupCssSelector = builder.jsoupCssSelector;
		this.lastBuildDate = builder.lastBuildDate;
		this.sourceImageUrl = builder.sourceImageUrl;
		this.sourceName = builder.sourceName;
		this.updateIntervalMinutes = builder.updateIntervalMinutes;
		this.url = builder.url;
	}

	public RssFeedDefinition(String jsoupCssSelector, String sourceImageUrl, String sourceName,
			int updateIntervalMinutes, String url) {
		super();
		this.jsoupCssSelector = jsoupCssSelector;
		this.sourceImageUrl = sourceImageUrl;
		this.sourceName = sourceName;
		this.updateIntervalMinutes = updateIntervalMinutes;
		this.url = url;
	}

	public List<String> getCategoryList() {
		return categoryList;
	}

	public String getDescriptionProcessingStrategy() {
		return descriptionProcessingStrategy;
	}

	public String getId() {
		return id;
	}

	public String getJsoupCssSelector() {
		return jsoupCssSelector;
	}

	public Date getLastBuildDate() {
		return lastBuildDate;
	}

	public String getSourceImageUrl() {
		return sourceImageUrl;
	}

	public String getSourceName() {
		return sourceName;
	}

	public int getUpdateIntervalMinutes() {
		return updateIntervalMinutes;
	}

	public String getUrl() {
		return url;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setJsoupCssSelector(String jsoupCssSelector) {
		this.jsoupCssSelector = jsoupCssSelector;
	}

	public void setLastBuildDate(Date lastBuildDate) {
		this.lastBuildDate = lastBuildDate;
	}

	@Override
	public String toString() {
		StringBuilder builder2 = new StringBuilder();
		builder2.append("RssFeedDefinition [categoryList=").append(categoryList)
				.append(", descriptionProcessingStrategy=").append(descriptionProcessingStrategy).append(", enabled=")
				.append(enabled).append(", id=").append(id).append(", jsoupCssSelector=").append(jsoupCssSelector)
				.append(", lastBuildDate=").append(lastBuildDate).append(", sourceName=").append(sourceName)
				.append(", updateIntervalMinutes=").append(updateIntervalMinutes).append(", url=").append(url)
				.append("]");
		return builder2.toString();
	}

	
}
