/**
 * 
 */
package nl.cyberworkz.roboflightmonitor;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Utility methods for Schiphol API
 * 
 * 
 * @author haiko
 *
 */
public class SchipholAPIUtils {
	
	private static Logger LOG = LoggerFactory.getLogger(SchipholAPIUtils.class);
	
	public static HashMap<String, String> getPagingLinks(String linksObject) {
		HashMap<String, String> linkMap = new HashMap<>();

		String[] links = linksObject.split(",");
		for (int i = 0; i < links.length; i++) {

			// get key first
			String[] keyValue = links[i].split(";");
			String keyRaw = keyValue[1].split("=")[1].trim();

			String key = keyRaw.substring(1, keyRaw.length() - 1);

			// get value
			String valueRaw = keyValue[0].trim();
			String value = null;
			try {
				value = URLDecoder.decode(valueRaw.substring(1, valueRaw.length() - 1), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				LOG.error(e.getMessage());
			}

			linkMap.put(key, value);
		}

		return linkMap;
	}

}
