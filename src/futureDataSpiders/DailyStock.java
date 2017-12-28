package futureDataSpiders;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DailyStock {
	volatile static int i = 1;

	public static void main(String[] args) {
		try {
			run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private static void run() throws InterruptedException {

		HttpURLConnection conn = null;
		// String content = "";
		try {
			for (int m = 8; m < 13; m++) {
				for (int d = 1; d < getMaxDayByYearMonth(2017, m) + 1; d++) {
					String month = "0" + m;
					String day = "0" + d;
					if (m > 9) {
						month = "" + m;
					}
					if (d > 9) {
						day = "" + d;
					}
					String urlString = "http://www.shfe.com.cn/data/dailydata/2017" + month + day + "dailystock.dat";
					URL url = new URL(urlString);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(3000);
					conn.setReadTimeout(3000);
					conn.connect();
					if (conn.getResponseCode() == 404) {
						continue;
					}

					InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "utf-8");
					char[] buffer = new char[1024];
					int length = 0;
					String fileName = "C:\\Users\\chuan\\Desktop\\daliystock.txt";
					File file = new File(fileName);
					StringBuilder result = new StringBuilder();
					if (!file.exists()) {
						file.createNewFile();
					}

					while ((length = isr.read(buffer)) != -1) {
						// fw.write(new String(buffer, 0,length));
						// fw.flush();
						result.append(new String(buffer, 0, length));
					}
					JsonObject gson = new JsonParser().parse(result.toString()).getAsJsonObject();

					JsonArray jsonArray = gson.getAsJsonArray("o_cursor");
					System.out.println(jsonArray.size());
					for (int j = 0; j < jsonArray.size(); j++) {
						JsonObject jsonObject = jsonArray.get(j).getAsJsonObject();
						// System.out.println(jsonObject.getAsString().length());
						String name = jsonObject.get("VARNAME").getAsString();
						String total = jsonObject.get("WHABBRNAME").getAsString();
						int number = jsonObject.get("WRTWGHTS").getAsInt();
						int change = jsonObject.get("WRTCHANGE").getAsInt();
						if (total.equals("总计$$Total")) {
							System.out.println(month+"-"+day);
							// System.out.println(name + ": " + number + " " + change);
							File saveFile = new File("C:\\Users\\chuan\\Desktop\\daliystock\\" + name + ".xlsx");
							if (!saveFile.exists()) {
								saveFile.createNewFile();
							}
							FileWriter fw = new FileWriter("C:\\Users\\chuan\\Desktop\\daliystock\\" + name + ".xlsx",
									true);
							fw.write("2017-"+month+"-"+day+"\t"+number + "\t" + change );
							fw.write("\r\n");
							fw.flush();
							fw.close();
						}
					}
					isr.close();
				}
				conn.disconnect();				
			
			}
			// URL url = new URL("http",sb.toString(),80,"/");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

	}

	public static int getMaxDayByYearMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		return calendar.getActualMaximum(Calendar.DATE);
	}
}
