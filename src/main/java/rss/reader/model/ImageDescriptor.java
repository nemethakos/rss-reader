package rss.reader.model;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class ImageDescriptor {
	private String url;
	private BufferedImage imageData;

	public ImageDescriptor(String url, BufferedImage imageData) {
		super();
		Objects.requireNonNull(url);
		Objects.requireNonNull(imageData);
		this.url = url;
		this.imageData = imageData;
	}

	public String getUrl() {
		return url;
	}

	public BufferedImage getImageData() {
		return imageData;
	}

	@Override
	public String toString() {
		return "ImageDescriptor [url=" + url + ", imageData=" + imageData + "]";
	}
	
}