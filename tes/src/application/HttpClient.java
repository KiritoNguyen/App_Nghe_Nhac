package application;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
	//private static String BASE_URL = "https://bachnx.nyc3.digitaloceanspaces.com/musics/entries.json";
	private static String BASE_URL = "https://api.myjson.com/bins/1b5gux";
	//private static String LYR_URL="https://api.musixmatch.com/ws/1.1/track.search?format=jsonp&callback=callback&quorum_factor=1&apikey=646a669754a9946fee5494bb77678e61&q_track=";
	public String getData() {
		HttpURLConnection con = null ;
		InputStream is = null;
		 
		try {
		con = (HttpURLConnection) (new URL(BASE_URL)).openConnection();
		con.setRequestMethod("GET");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.connect();
		 
		// Đọc các kết quả trả về tại đây
		StringBuffer buffer = new StringBuffer();
		is = con.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while ( (line = br.readLine()) != null )
		buffer.append(line + "\r\n");
		 
		is.close();
		con.disconnect();
		return buffer.toString();
		}
		catch(Throwable t) {
		t.printStackTrace();
		}
		finally {
		try { is.close(); } catch(Throwable t) {}
		try { con.disconnect(); } catch(Throwable t) {}
		}
		return null;
	}

}
