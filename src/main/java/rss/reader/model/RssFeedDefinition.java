package rss.reader.model;

import javax.annotation.Generated;

/**
 * Defines the URL, update interval, a source name (e.g.: CNN), an icon URL for
 * the source and a JSOUP CSS selector for selecting the article body from the
 * HTML page.
 *
 */
public class RssFeedDefinition {
	private String jsoupCssSelector;
	private String sourceImageUrl;
	private String sourceName;
	private int updateIntervalMinutes = 60;
	private String url;
	private boolean enabled = true;
	private String descriptionProcessingStrategy = "default";

	public boolean isEnabled() {
		return enabled;
	}

	public String getDescriptionProcessingStrategy() {
		return descriptionProcessingStrategy;
	}

	@Generated("SparkTools")
	private RssFeedDefinition(Builder builder) {
		this.jsoupCssSelector = builder.jsoupCssSelector;
		this.sourceImageUrl = builder.sourceImageUrl;
		this.sourceName = builder.sourceName;
		this.updateIntervalMinutes = builder.updateIntervalMinutes;
		this.url = builder.url;
	}

	public static RssFeedDefinition from(String url, int updateIntervalMinutes, String sourceName,
			String sourceImageUrl) {
		return new RssFeedDefinition("body", sourceImageUrl, sourceName, updateIntervalMinutes, url);
	}
	
	public static RssFeedDefinition from(String url, int updateIntervalMinutes, String sourceName,
			String sourceImageUrl, String jsoupCssSelector) {
		return new RssFeedDefinition(jsoupCssSelector, sourceImageUrl, sourceName, updateIntervalMinutes, url);
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

	public String getJsoupCssSelector() {
		return jsoupCssSelector;
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

	public void setJsoupCssSelector(String jsoupCssSelector) {
		this.jsoupCssSelector = jsoupCssSelector;
	}

	@Override
	public String toString() {
		return "RssFeedDefinition [url=" + url + ", updateIntervalMinutes=" + updateIntervalMinutes + ", sourceName="
				+ sourceName + "]";
	}

	/**
	 * Creates builder to build {@link RssFeedDefinition}.
	 * @return created builder
	 */
	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder to build {@link RssFeedDefinition}.
	 */
	@Generated("SparkTools")
	public static final class Builder {
		private String jsoupCssSelector;
		private String sourceImageUrl;
		private String sourceName;
		private int updateIntervalMinutes;
		private String url;

		private Builder() {
		}

		public Builder withJsoupCssSelector(String jsoupCssSelector) {
			this.jsoupCssSelector = jsoupCssSelector;
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

		public RssFeedDefinition build() {
			return new RssFeedDefinition(this);
		}
	}

}
