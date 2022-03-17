/*
 * This is used to show the results of search strings
 * */

package com.hcl.toygoogle;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.GenericServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

/**
 * Servlet implementation class SearchStringResultsServlet
 */
@WebServlet("/SearchStringResultsServlet")
public class SearchStringResultsServlet extends GenericServlet {
	private static final long serialVersionUID = 1L;
	private Connection con;

	/**
	 * @see GenericServlet#GenericServlet()
	 */
	public SearchStringResultsServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() {
	}

	/**
	 * @see Servlet#service(ServletRequest request, ServletResponse response)
	 */
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int count = 0;

		String search = request.getParameter("search");
		System.out.println("search Word: " + search);
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/toyGoogle", "root", "Sai@4884");
			System.out.println(con);

			Statement statement = con.createStatement();
			System.out.println(statement);

			ResultSet rs = statement.executeQuery("select distinct * from searchWordWithDateTime");

			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<head>");
			out.println("<link rel=\"stylesheet\"\r\n"
					+ "	href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\" />");
			out.println("<link rel=\"stylesheet\" href=\"searchTable.css\" />\r\n" + "");
			out.println("</head>");
			out.println("<body style='background-color:#FDE6E6;'>");
			out.println("<table id='customers'>");
			out.println("<tr><th>Date & Time</th><th>Word</th></tr>");
			while (rs.next()) {
				out.println("<tr>");

				out.println("<td>" + rs.getString(1) + "</td><td>" + rs.getString(2) + "</td>");
				out.println("</tr>");

				count++;
			}
			out.println("</table>");

			if (count == 0) {
				out.print("<script>");
				out.println("document.getElementById('customers').innerHTML = ''");
				out.println("</script>");

				out.println("<p style='font-weight: bold;\r\n"
						+ "  text-align: center;top:60px; '><i class=\"fa fa-search\"></i>&nbsp&nbsp No Search Result Found!!!"
						+ "</p>");
				out.println("<span style='font-size:100px;margin-left: 45%;'>&#128577;</span>");
			}
			out.println("</body>");
			out.println("</html>");

			// Navigate to search string html page
			RequestDispatcher dispatcher = request.getRequestDispatcher("searchStrings.html");
			dispatcher.include(request, response);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void destroy() {
	}

}
