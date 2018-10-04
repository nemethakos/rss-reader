package rss.reader.downloader;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import rss.reader.nls.PosTaggerAndLemmatizer;

class ArticleDownloaderTest {

	ArticleDownloader DOWNLOADER = new ArticleDownloader( PosTaggerAndLemmatizer.getInstance());

	@Test
	void urlEscapeTest() {
		String original = "http://a57.foxnews.com/www.foxnews.com/content/dam/fox-news/images/2018/09/29/270/152/kailyn pollard cropped.jpg?ve=1&tl=1";
		original.replace(" ", "+");
	}
	
	@Test
	void testDownload() throws IOException {
/*
		System.out.println(DOWNLOADER.getTextFromHtmlPageUsingJsoupCssSelector(
				"https://edition.cnn.com/2018/08/29/uk/holiday-hunger-uk-intl/index.html", "div.pg-special-article__body"));
	*/
	}

	@Test
	void test() throws IOException {

		//"
/*
		System.out.println(
				DOWNLOADER.getTags("https://edition.cnn.com/2018/08/29/uk/holiday-hunger-uk-intl/index.html", "div.pg-special-article__body"));
*/
/*		
		System.out.println(
				DOWNLOADER.getTags("https://money.cnn.com/2018/08/27/technology/elon-musk-tesla-challenges/index.html",
						"div.pg-side-of-rail"));
*/
		// https://money.cnn.com/2018/08/28/technology/elon-musk-new-york-times-interview-tears/index.html
		/*
		 * System.out.println(getTags(
		 * "https://money.cnn.com/2018/08/28/technology/elon-musk-new-york-times-interview-tears/index.html",
		 * "div.pg-side-of-rail"));
		 */
		/*
		 * System.out.println(getTags(
		 * "https://edition.cnn.com/2018/08/29/uk/holiday-hunger-uk-intl/index.html",
		 * "div.pg-side-of-rail"));
		 */
		/*
		 * System.out.println(getTags(
		 * "https://edition.cnn.com/2018/08/29/health/brazilian-butt-lift-partner/index.html",
		 * "div.pg-side-of-rail"));
		 */
		//
		/*
		 * System.out.println(getKeywordsFromHtmlPageUsingJsoupCssSelector(
		 * "https://www.bbc.com/news/world-europe-45319658", ".story-body__inner p"));
		 */
		/*
		 * System.out.println(getKeywordsFromHtmlPageUsingJsoupCssSelector(
		 * "https://edition.cnn.com/2018/08/27/motorsport/belgian-grand-prix-crash-charles-leclerc-halo-fernando-alonso-spt-intl/index.html",
		 * "div.pg-side-of-rail"));
		 */
	}

}
