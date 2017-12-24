package futureDataSpiders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public class Spiders {
	public static void main(String[] args) throws IOException {
		// String path = "C:\\Users\\chuan\\Downloads\\phantomjs\\bin\\";
		System.setProperty("phantomjs.binary.path", "C:/Users/chuan/Downloads/phantomjs/bin/");
		File file = new File("C:\\Users\\chuan\\Downloads\\phantomjs\\bin\\phantomjs.exe");
		System.setProperty("phantomjs.binary.path", file.getAbsolutePath());
		WebDriver driver = new PhantomJSDriver();
		for (int j = 0; j < 30; j++) {
			String date = "2017-9-" + (30 - j);
			String url = "http://www.100ppi.com/sf/day-" + date + ".html";
			// String exec = path+"phantomjs.exe "+path+"code.js " + url;
			driver.get(url);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Document doc = Jsoup.parse(driver.getPageSource());
			Elements trs = doc.getElementById("fdata").select("tr");
			for (int i = 0; i < trs.size(); i++) {
				String s = trs.get(i).text();
				char[] test = s.toCharArray();
				if (isChinese(test[0]) && test.length > 25) {
					String fileName = s.split(" ")[0];
					String file2 = "C:\\Users\\chuan\\desktop\\futureData\\" + fileName + ".txt";
					FileOutputStream fos = new FileOutputStream(file2, true);
					fos.write((s + " " + date).getBytes());
					fos.write("\r\n".getBytes());
					fos.flush();
					fos.close();
				}
			}
		}
		driver.quit();

	}

	private static final boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}
}
