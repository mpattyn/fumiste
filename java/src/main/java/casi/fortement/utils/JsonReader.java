package casi.fortement.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {

    private static String readAll(Reader rd) throws IOException {
	StringBuilder sb = new StringBuilder();
	int cp;
	while ((cp = rd.read()) != -1) {
	    sb.append((char) cp);
	}
	return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url, boolean hasProxy) throws IOException,
									       JSONException {
	HttpURLConnection uc;
	if(hasProxy){
	    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
									   "cachemad.insa-rouen.fr", 3128));
	    uc = (HttpURLConnection) new URL(url)
		.openConnection(proxy);
	}
	else{
	    uc = (HttpURLConnection) new URL(url).openConnection();
	}
	uc.connect();
	InputStream is = uc.getInputStream();
	try {
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is,
									 Charset.forName("UTF-8")));
	    String jsonText = readAll(rd);
	    JSONObject json = new JSONObject(jsonText);
	    return json;
	} finally {
	    is.close();
	}
    }
}