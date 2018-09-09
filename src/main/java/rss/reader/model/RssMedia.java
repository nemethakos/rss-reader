package rss.reader.model;

import javax.annotation.Generated;

/**
 * URL and dimension for an icon used for RSS Item
 */
public class RssMedia {
	String link;
	Integer width;
	Integer height;

	public boolean hasDimension() {
		return width != null && height != null;
	}

	public float getHeightWidthRatio() {
		if (hasDimension()) {
			return width / height;
		} else {
			return 0;
		}
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

	public String getLink() {
		return link;
	}

	public Integer getWidth() {
		return width;
	}

	@Override
	public String toString() {
		return "RssMedia [link=" + link + ", width=" + width + ", height=" + height + "]";
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

	/**
	 * Builder to build {@link RssMedia}.
	 */
	@Generated("SparkTools")
	public static final class Builder {
		@Override
		public String toString() {
			return "Builder [link=" + link + ", width=" + width + ", height=" + height + "]";
		}

		private String link;
		private Integer width;
		private Integer height;

		private Builder() {
		}

		public Builder withLink(String link) {
			this.link = link;
			return this;
		}

		public Builder withWidth(Integer width) {
			this.width = width;
			return this;
		}

		public Builder withHeight(Integer height) {
			this.height = height;
			return this;
		}

		public RssMedia build() {
			return new RssMedia(this);
		}
	}
}
