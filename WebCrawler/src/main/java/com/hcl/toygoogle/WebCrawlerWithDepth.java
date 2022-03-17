/*
 * This class contains all Required methods for implementation
  */

package com.hcl.toygoogle;

//import classes available in jsoup  
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

public class WebCrawlerWithDepth {

	// initialize MAX_DEPTH variable with final value
	private static final int MAX_DEPTH = 4;

	// create set that will store links
	HashSet<String> urlLinks;
	private String start_url;
	StringTokenizer st;
	ArrayList<String> token = new ArrayList<String>();;
	String[] commonWords = { "is", "are", "on", "the", "of", "with", "can", "why", "and", "at", "they", "there", "then",
			"other", "these", "them", "their", "has", "have", "off", "where", "was", "which", "it", "its", "to", "into",
			"this", "isnt", "his" };
	private Connection con;

	// initialize set using constructor
	public WebCrawlerWithDepth() {
		urlLinks = new HashSet<>();
	}

	// This method is used to remove common words like are,is etc.
	public boolean removeCommonWords(String str) {
		// System.out.println("remove Method");
		int repeat = 0;
		for (int i = 0; i < commonWords.length; i++) {
			// System.out.println("Enter for loop");
			if (commonWords[i].toLowerCase().equals(str.toLowerCase())) {
				repeat++;
			}
		}
		if (repeat != 0) {
			// System.out.println("common word is present");
			return false;
		}
		return true;
	}

	// create method that finds all the page link in the given URL
	public void getPageLinks(String URL, int depth) { // 1 => index =>de=>0

		// we use the conditional statement to check whether we have already crawled the
		// URL or not.
		// we also check whether the depth reaches to MAX_DEPTH or not
		if ((!urlLinks.contains(URL) && (depth < MAX_DEPTH)
				&& !URL.equals("http://localhost:8080/WebCrawler/MainPage.html"))) {
			System.out.println(">> Depth: " + depth + " [" + URL + "]");
			// use try catch block for recursive process
			try {
				// if the URL is not present in the set, we add it to the set
				urlLinks.add(URL);
				// fetch the HTML code of the given URL by using the connect() and get() method
				// and store the result in Document
				Document doc = Jsoup.connect(URL).get();

				// we use the select() method to parse the HTML code for extracting links of
				// other URLs and store them into Elements
				Elements availableLinksOnPage = doc.select("a[href]");
				// increase depth
				depth++;
				// for each extracted URL, we repeat above process
				for (Element page : availableLinksOnPage) {

					// call getPageLinks() method and pass the extracted URL to it as an argument
					getPageLinks(page.attr("abs:href"), depth);
				}
			}
			// handle exception
			catch (IOException e) {
				// print exception messages
				System.err.println("For '" + URL + "': " + e.getMessage());
			}
		}
	}

	// create a method to get html document
	public String getHTML(String url) {

		URL u;
		try {
			u = new URL(url);
			URLConnection con = u.openConnection();
			con.setRequestProperty("User-Agent", "WebCrawler/1.0");
			con.setRequestProperty("Accept-charset", "UTF-8");

			InputStream is = con.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			String line;
			String html = "";

			while ((line = reader.readLine()) != null) {
				html += line;
			}

			return html;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// To tokenize html document
	public void start(String url) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/toyGoogle", "root", "Sai@4884");
			System.out.println(con);
			String html = getHTML(url);
			System.out.println(html);

			Document doc = Jsoup.parse(html);
			Elements text = doc.select("p");
			for (Element element : text) {
				st = new StringTokenizer(element.text(), " ");
				while (st.hasMoreTokens()) {
					String temp = st.nextToken().toLowerCase();
					if (removeCommonWords(temp)) {
						PreparedStatement ps = con.prepareStatement("insert into storeUrlWord values(?,?)");
						ps.setString(1, url);
						ps.setString(2, temp.replaceAll("[^a-zA-Z0-9]", ""));
						int i = ps.executeUpdate();
						System.out.println(ps.executeUpdate());
					}

				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
