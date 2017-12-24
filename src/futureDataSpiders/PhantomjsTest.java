package futureDataSpiders;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PhantomjsTest {
	public static void main(String[] args) {
		Runtime rt = Runtime.getRuntime();
		String path = "C:\\Users\\chuan\\Downloads\\phantomjs\\bin\\";
		String url = "https://www.douyu.com/lilithxy";
		String exec = path+"phantomjs.exe "+path+"code.js " + url;
		Process p = null;
		try {
			p = rt.exec(exec);
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream is = p.getInputStream();
		StringBuilder sb = new StringBuilder();
		int len = -1;
		byte[] buffer = new byte[1024];
		try {
			while ((len = is.read(buffer))!=-1) {
				sb = sb.append(new String(buffer, 0, len));				
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		System.out.println(sb.toString());
		try {
			is.close();
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		File file = new File("C:\\Users\\chuan\\desktop\\jsl.html");
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
}
