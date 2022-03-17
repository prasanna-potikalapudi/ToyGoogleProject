/*
// * This servlet is used to fetch results based on string
// * */
package com.hcl.toygoogle;

import java.io.IOException;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.GenericServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Servlet implementation class ResultServlet
 */
@WebServlet("/ResultServlet")
public class ResultServlet extends GenericServlet {
	private static final long serialVersionUID = 1L;
	private Connection con;
	Date date = new Date();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	String strDate = formatter.format(date);
	int count = 0;

	/**
	 * @see GenericServlet#GenericServlet()
	 */
	public ResultServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() {
		System.out.println("coming to init:");
		// This is used to clear data

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/toyGoogle", "root", "Sai@4884");

			PreparedStatement ps = con.prepareStatement("delete from storeUrlWord");

			int i = ps.executeUpdate();
			System.out.println(ps.executeUpdate());

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		// create instance of the WebCrawlerExampleWithDepth class
		WebCrawlerWithDepth obj = new WebCrawlerWithDepth();

		// pick a URL from the frontier and call the getPageLinks()method and pass 0 as
		// starting depth
		obj.getPageLinks("http://localhost:8080/WebCrawler/index.html", 0);
		// obj.getPageLinks("https://www.mindwatering.com/MW.nsf", 0);

		// initialize arrayList
		ArrayList<String> splitToken = null;

		Iterator<String> itr = obj.urlLinks.iterator();

		while (itr.hasNext()) {
			obj.start(itr.next());
		}
	}

	/**
	 * @see Servlet#service(ServletRequest request, ServletResponse response)
	 */
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {

		// get value of search
		String search = request.getParameter("search");

		// set what type content
		response.setContentType("text/html");

		// use printwriter to display output

		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=\"stylesheet\" href=\"css/styles.css\" />\r\n" + "");
		out.println("<link rel=\"stylesheet\"\r\n"
				+ "	href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\" />");
		out.println(
				"<script src='https://kit.fontawesome.com/a076d05399.js' crossorigin='anonymous'></script>\r\n" + "");

		out.println("</head>");

		out.println("<body style='background-color:#FDE6E6;'>");
		out.println(" <header>" + "     <div class='topnav'>"
				+ "			<a style='text-decoration: none' href='MainPage.html'><i class='fa fa-home'></i> Home</a>"
				+ "<p style='margin-top:0;'><i class='fa fa-search'></i>&nbsp" + search + "</p></div>" + "</header>");

		// Using Database connection to store search string value
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/toyGoogle", "root", "Sai@4884");
			System.out.println(con);

			PreparedStatement ps1 = con
					.prepareStatement("select distinct * from storeUrlWord where storeWord Like '%" + search + "%'");

			ResultSet rs = ps1.executeQuery();

			out.println("<table id='customers'>");
			while (rs.next()) {
				out.println("<tr id='myList" + count + "'>");

				out.println(
						"<td><a onclick=\"myFunction()\" style='text-decoration: none;\r\n" + "    color: #5A5A54;\r\n"
								+ "    font-weight: bold;' href='" + rs.getString(1) + "'>" + rs.getString(1) + "</a>"
								+ "</td><td style='font-weight: bold;'>" + rs.getString(2) + "</td>");
				out.println("<td>");
				out.println("<button id='clear" + count + "' onclick=\"document.getElementById('myList" + count
						+ "').remove();" + "document.getElementById('clear" + count + "').remove();\""
						+ " style='background-color: transparent;\r\n" + "    border: none'>"
						+ "<i class='fas fa-times'></i></button>\r\n");
				out.println("</td>");
				out.println("</tr>");

				count++;
			}
			out.println("</table>");

			System.out.println("count: " + count);
			if (count == 0) {
				out.println(
						"<i class=\"fa fa-search\" style='margin-left: 48%;'></i>&nbsp No search found!!!&#128577;");

			}

			PreparedStatement ps = con.prepareStatement("insert into searchWordWithDateTime values(?,?)");
			ps.setString(1, strDate);
			ps.setString(2, search);
			int i = ps.executeUpdate();
			System.out.println(ps.executeUpdate());

			out.println("<script>");
			out.println("function myFunction() {" + "var a = '" + search.toLowerCase() + "';"
					+ "localStorage.setItem('myValue', a);" + "}");
			out.println("</script>");
			out.println("</body>");
			out.println("</html>");

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void destroy() {
	}

}
