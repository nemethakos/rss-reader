package archive;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rss.reader.model.RssItem;
import rss.reader.model.RssMedia;

public class FileHtmlGenerator {

	private static Logger logger = LoggerFactory.getLogger(FileHtmlGenerator.class);

	private static String getResourceAsString(String name) throws URISyntaxException, IOException {
		System.out.println("name: " + name);
		Path path = Paths.get(FileHtmlGenerator.class.getResource("/static/" + name).toURI());

		StringBuilder data = new StringBuilder();
		Stream<String> lines = Files.lines(path);
		lines.forEach(line -> data.append(line).append("\n"));
		lines.close();

		return data.toString();

	}

	public static String getHtml(List<RssItem> rssItemList, boolean showStringifiedText) throws URISyntaxException, IOException {

		StringWriter fw = new StringWriter();
		fw.write("<html><head>" + "<style>" + getResourceAsString("style.css") + "</style></head><body>");

		if (rssItemList.isEmpty()) {
			fw.write("<h1>Please wait, user feed is being fetched...</h1>");
		} else {
			for (var rssItem : rssItemList) {
				writeRss(fw, rssItem, showStringifiedText);
				logger.info("writing " + rssItem.getTitle());
			}
		}

		fw.write("</body>");

		return fw.toString();
	}

	public static void writeHtml(String fileName, List<RssItem> rssItemList) throws IOException, URISyntaxException {
		File f = new File(fileName);
		if (f.exists()) {
			f.delete();
		}
		f.createNewFile();
		try (FileWriter fw = new FileWriter(fileName)) {
			fw.write("<html><head>" + "<style>" + getResourceAsString("style.css") + "</style></head><body>");

			for (var rssItem : rssItemList) {
				writeRss(fw, rssItem);
				logger.info("writing " + rssItem.getTitle());
			}

			fw.write("</body>");

		} catch (IOException ioe) {
			logger.info("Error writing '" + fileName + "'", ioe);
		}
	}

	private static void writeRss(FileWriter fw, RssItem rssItem) throws IOException {

		fw.write("<div class='rssItem'>\r\n");
/*		if (!rssItem.getRssMediaList().isEmpty()) {
			// for (var rssMedia : rssItem.getRssMediaList()) {
			writeImage(fw, rssItem.getRssMediaList().get(0));
			// }
		}*/
		fw.write("<div class='title'><a href='" + rssItem.getLink() + "'>" + rssItem.getTitle() + "</a></div>\r\n");
		if (rssItem.getDescription() != null) {
			fw.write("<div class='description'>" + rssItem.getDescription() + "</div>\r\n");
		}

		if (rssItem.getProviderImage() != null || rssItem.getProviderName() != null) {
			fw.write("<div class='provider'>");

			String titleString = "";
			if (rssItem.getProviderName() != null) {
				titleString = " title='" + rssItem.getProviderName() + "' ";
			}

			if (rssItem.getProviderImage() != null) {
				fw.write("<img class='providerImage' src='" + rssItem.getProviderImage() + "' " + titleString + ">");
			} else if (rssItem.getProviderName() != null) {
				fw.write("<span class='providerNameWithoutImage'>" + rssItem.getProviderName() + "</span>");
			}

			fw.write("</div>");
		}

		fw.write("<div class='publicationDate'>" + rssItem.getPublicationDate().toString() + "</div>\r\n");
		fw.write("</div>\r\n");
	}

	private static void writeImage(FileWriter fw, RssMedia rssMedia) throws IOException {
		fw.write("<img class='media' src='" + rssMedia.getLink() + "' _width='" + rssMedia.getWidth() + "' _height='"
				+ rssMedia.getHeight() + "'>\r\n");
	}

	private static void writeRss(StringWriter fw, RssItem rssItem, boolean showStringified) {

		fw.write("<div class='rssItem'>\r\n");
/*		if (!rssItem.getRssMediaList().isEmpty()) {
			// for (var rssMedia : rssItem.getRssMediaList()) {
			writeImage(fw, rssItem.getRssMediaList().get(0));
			// }
		}*/
		fw.write("<div class='title'><a href='" + rssItem.getLink() + "'>" + rssItem.getTitle() + "</a></div>\r\n");
		if (rssItem.getDescription() != null) {
			fw.write("<div class='description'>" + rssItem.getDescription() + "</div>\r\n");
		}

		String providerImageUrl = rssItem.getProviderImage();
		String providerName = rssItem.getProviderName();

		if (providerImageUrl != null || providerName != null) {
			fw.write("<div class='provider'>");

			String titleString = "";
			if (providerName != null) {
				titleString = " title='" + providerName + "' ";
			}

			if (providerImageUrl != null) {
				fw.write("<img class='providerImage' src='" + providerImageUrl + "' " + titleString + ">");
			} else if (providerName != null) {
				fw.write("<span class='providerNameWithoutImage'>" + providerName + "</span>");
			}

			fw.write("</div>");
		}

		fw.write("<div class='publicationDate'>" + rssItem.getPublicationDate().toString() + "</div>\r\n");
		if (showStringified) {
			fw.write("<xmp class='stringified'>" + rssItem.getStringifiedEntry() + "</xmp>");
		}
		else {
			var url = "/item/"+rssItem.getGuid();
			fw.write("<a href='"+url+"'>"+url+"</a>");
		}
		fw.write("</div>\r\n");
	}

	private static void writeImage(StringWriter fw, RssMedia rssMedia) {
		fw.write("<img class='media' src='" + rssMedia.getLink() + "' _width='" + rssMedia.getWidth() + "' _height='"
				+ rssMedia.getHeight() + "'>\r\n");
	}

}
