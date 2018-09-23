package rss.reader.model;

import javax.annotation.Generated;

/**
 * URL and dimension for an icon used for RSS Item
 */
public class RssMedia {
	private Integer height;
	private String link;
	private Integer width;

	/**
	 * Builder to build {@link RssMedia}.
	 */
	@Generated("SparkTools")
	public static final class Builder {
		private Integer height;
		private String link;
		private Integer width;
		
		private Builder() {
		}

		public RssMedia build() {
			return new RssMedia(this);
		}

		public Builder withHeight(Integer height) {
			this.height = height;
			return this;
		}

		public Builder withLink(String link) {
			this.link = link;
			return this;
		}

		public Builder withWidth(Integer width) {
			this.width = width;
			return this;
		}
	}

	/**
	 * Creates builder to build {@link RssMedia}.
	 * 
	 * @return created builder
	 */
	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	@Generated("SparkTools")
	private RssMedia(Builder builder) {
		this.link = builder.link;
		this.width = builder.width;
		this.height = builder.height;
	}

	public Integer getHeight() {
		return height;
	}

	public float getHeightWidthRatio() {
		if (hasDimension()) {
			return (float)width / (float)height;
		} else {
			return 0;
		}
	}

	public String getLink() {
		return link;
	}

	public Integer getWidth() {
		return width;
	}

	public boolean hasDimension() {
		return width != null && height != null;
	}

	@Override
	public String toString() {
		return "RssMedia [link=" + link + ", width=" + width + ", height=" + height + "]";
	}
}
