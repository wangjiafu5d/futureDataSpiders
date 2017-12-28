package futureDataSpiders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public class Spiders {
	public static void main(String[] args) throws IOException {
		// String path = "C:\\Users\\chuan\\Downloads\\phantomjs\\bin\\";
		long start = System.currentTimeMillis();
		System.setProperty("phantomjs.binary.path", "C:/Users/chuan/Downloads/phantomjs/bin/");
		File file = new File("C:\\Users\\chuan\\Downloads\\phantomjs\\bin\\phantomjs.exe");
		System.setProperty("phantomjs.binary.path", file.getAbsolutePath());
		WebDriver driver = new PhantomJSDriver();

		for (int m = 12; m > 0; m--) {
			int monthDays = getMaxDayByYearMonth(2017, m);
			for (int j = monthDays; j > 0; j--) {
				String date = "";
				String month = "0" + m;
				String day = "0" + j;
				if (m > 9) {
					month = "" + m;
				}
				if (j > 9) {
					day = "" + j;
				}
				date = 2017 + "-" + month + "-" + day;
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
					if (isChinese(s.charAt(0)) && s.length() > 25) {
						String fileName = s.split(" ")[0];
						String filePath = "C:\\Users\\chuan\\desktop\\futureData\\" + fileName + ".xlsx";
						File dataFile = new File(filePath);
						if (!dataFile.exists()) {
							dataFile.createNewFile();
						}
						FileOutputStream fos = new FileOutputStream(filePath, true);
						String[] ele = s.split(" ");
						for (String string : ele) {
							fos.write((string + "\t").getBytes());
						}
						fos.write(date.getBytes());
						fos.write("\r\n".getBytes());
						fos.flush();
						fos.close();
					}
				}
			}
		}
		driver.quit();
		long end = System.currentTimeMillis();
		System.out.println("程序总共用时：" + (end - start) / 1000.0 + " 秒");
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

	public static int getMaxDayByYearMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		return calendar.getActualMaximum(Calendar.DATE);
	}
}
