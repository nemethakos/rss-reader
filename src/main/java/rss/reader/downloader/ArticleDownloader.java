package rss.reader.downloader;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.maxschuster.dataurl.DataUrl;
import eu.maxschuster.dataurl.DataUrlSerializer;
import eu.maxschuster.dataurl.IDataUrlSerializer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rss.reader.model.ImageDescriptor;
import rss.reader.model.Word;
import rss.reader.nls.PosTaggerAndLemmatizer;

public class ArticleDownloader {
	static Logger logger = LoggerFactory.getLogger(ArticleDownloader.class);
	PosTaggerAndLemmatizer tagger = null;
	OkHttpClient client = new OkHttpClient();

	public ArticleDownloader(PosTaggerAndLemmatizer tagger) {
		super();
		Objects.nonNull(tagger);
		this.tagger = tagger;
	}

	public List<String> getKeywords(String text) {
		List<String> result = new ArrayList<>();

		String[] words = text.split("\\s+");

		var removeDuplicatesSet = new HashSet<String>(Arrays.asList(words));

		result.addAll(removeDuplicatesSet);

		result.sort(String::compareToIgnoreCase);

		return result;
	}

	public String elementsToText(Elements elements) {
		StringBuilder sb = new StringBuilder();
		for (var element : elements) {
			sb.append(element.text() + "\r\n");
		}
		return sb.toString();
	}

	public List<ImageDescriptor> getImages(String pageURL, Document doc, String jsoupSelector) {

		Elements selected = doc.select(jsoupSelector).select("img");

		return selected//
				.stream()//
				.map(img -> img.attr("src"))//
				.filter(Objects::nonNull)//
				.filter(imageURL -> imageURL.trim().length() > 0)//
				.map(imageURL -> normalize(pageURL, imageURL))//
				.filter(Objects::nonNull)//
				// .filter(imageURL -> !imageURL.startsWith("http://data:image/svg+xml;"))
				// .filter(imageURL -> !imageURL.startsWith("http://data:image/gif;"))//
				// .filter(imageURL -> !imageURL.endsWith("svg"))//
				// .filter(imageURL -> !imageURL.endsWith("gif"))//
				.map(img -> loadImage(pageURL, img))//
				.filter(Objects::nonNull)//
				.collect(Collectors.toList());
	}

	private ImageDescriptor loadImage(String pageURL, String imageURLString) {
		URL url = null;
		// System.out.println("Load: *'" + imageURLString + "'*");
		if (imageURLString.startsWith("data:")) {
			IDataUrlSerializer serializer = new DataUrlSerializer();
			try {

				DataUrl unserialized = null;
				unserialized = serializer.unserialize(imageURLString);
				byte[] imageData = unserialized.getData();
				return new ImageDescriptor(imageURLString, ImageIO.read(new ByteArrayInputStream(imageData)));
			} catch (Exception e) {
				logger.error("Can't decode Data URI: '" + pageURL + "|" + imageURLString + "'");
			}
			return null;
		} else {
			try {

				url = new URL(imageURLString.replace(" ", "+"));

				BufferedImage imageData = ImageIO.read(url);
				if (imageData != null) {
					return new ImageDescriptor(imageURLString, imageData);
				} else {
					return null;
				}
			} catch (Exception e) {
				logger.error("Can't load image from URL: '" + url + "<-" + imageURLString + "', exception: " + e);
				imageURLString.chars().filter(Character::isWhitespace).forEach(chr -> {
					System.out.println("White space: " + Integer.toHexString(chr));
				});
				e.printStackTrace();
			}
		}
		return null;
	}

	private String normalize(String pageURLString, String url) {
		if (url.length() > 0) {

			String normalized = "";
			try {

				normalized = Jsoup.parse(url).text();

				URL pageURL = new URL(pageURLString);

				if (normalized.startsWith("data:")) {
					return normalized;
				} else if (normalized.startsWith("//")) {
					normalized = pageURL.getProtocol() + ":" + normalized;
				} else if (normalized.startsWith("/")) {
					normalized = pageURL.getProtocol() + "://" + pageURL.getHost() + normalized;
				} else if (!normalized.startsWith("http://") && !normalized.startsWith("https://")) {
					normalized = pageURLString + "/" + normalized;
				}

				//System.out.println("'" + normalized + "' <- '" + pageURLString + "' + " + url);

				return normalized;
			} catch (Exception e) {
				System.out.println("*************\r\n" + pageURLString + "->" + normalized);
				logger.error("Bad Page URL: " + pageURLString + ", exception: " + e);
			}
		}
		return "No normalization for page url: '" + pageURLString + "' and url: '" + url + "'";
	}

	private int getInt(String intStr) {
		int result = -1;
		if (intStr != null && intStr.length() > 0) {
			try {
				result = Integer.valueOf(intStr);
			} catch (NumberFormatException nfe) {
				logger.error("Wrong number for string: " + intStr);
			}
		}
		return result;
	}

	public Document downloadArticle(String url) throws IOException {

		// logger.info("Downloading: '" + url + "', selector:'" + jsoupCssSelector +
		// "'");

		Request request = new Request.Builder().url(url).build();

		Response response = client.newCall(request).execute();
		var html = response.body().string();

		Document doc = Jsoup.parse(html);

		return doc;
	}

	public String getTextFromHtmlPageUsingJsoupCssSelector(Document doc, String jsoupCssSelector) throws IOException {

		Elements selected = doc.select(jsoupCssSelector);

		var text = elementsToText(selected);
		if (text.trim().length() == 0) {
			text = elementsToText(doc.select("body"));
		}

		return text;

	}

	public String getHTMLWithSelector(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();
		var html = response.body().string();
		return html;
	}

	public String getHtmlOfArticle(Document doc, String jsoup) throws IOException {
		return getTextFromHtmlPageUsingJsoupCssSelector(doc, jsoup);
	}

	public List<Word> getTags(String parsedHtml) throws IOException {
		return tagger.parse(parsedHtml);
	}

}
