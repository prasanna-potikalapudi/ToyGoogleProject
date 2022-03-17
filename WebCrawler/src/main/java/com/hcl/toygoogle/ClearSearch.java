/*
 * 	This is used to clear browsing data
 */

package com.hcl.toygoogle;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.GenericServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

/**
 * Servlet implementation class ClearSearch
 */
@WebServlet("/ClearSearch")
public class ClearSearch extends GenericServlet implements ServletContextListener {
	private static final long serialVersionUID = 1L;
	private Connection con;

	/**
	 * @see GenericServlet#GenericServlet()
	 */
	public ClearSearch() {
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

		// This is used to clear browsing data
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/toyGoogle", "root", "Sai@4884");

			ClearSearch c = new ClearSearch();
			c.contextInitialized(null);

			// creating ServletContext object
			// ServletContext context = getServletContext();
			ServletContext context = getServletContext();
			String url = context.getInitParameter("url");
			// String test=context.getInitParameter("parametername");
			// System.out.println(test);
			String database = context.getInitParameter("Databasename");
			String username = context.getInitParameter("username");
			String password = context.getInitParameter("password");
			// String mydb= context.getInitParameter("parametername");
			System.out.println(password);
			// context.setAttribute("Database", mydb);

			// Getting the value of the initialization parameter and printing it
			String driverName = context.getInitParameter("dname");
			String driverUrl = context.getInitParameter("username");

			String driverPassword = context.getInitParameter("password");

			System.out.println(driverName);
			PreparedStatement ps = con.prepareStatement("delete from searchWordWithDateTime");

			PrintWriter out = response.getWriter();

			int i = ps.executeUpdate();
			System.out.println(ps.executeUpdate());

			out.println("<html>");
			out.println("<head>");
			out.print("<script src='https://unpkg.com/sweetalert/dist/sweetalert.min.js'></script>");
			out.println("</head>");
			out.println("<body>");
			out.println("<script type='text/javascript'>");
			out.println("swal('Toy Google','Successfully Cleared Browsing Data','success')");
			out.println("</script>");
			out.println("</html>");
			out.println("</body>");

			// redirect to search string html page
			RequestDispatcher dispatcher = request.getRequestDispatcher("searchStrings.html");
			dispatcher.include(request, response);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void destroy() {
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		ServletContext context = event.getServletContext();
		String url = context.getInitParameter("url");
		// String test=context.getInitParameter("parametername");
		// System.out.println(test);
		String database = context.getInitParameter("Databasename");
		String username = context.getInitParameter("username");
		String password = context.getInitParameter("password");
		// MyDatabase mydb=new MyDatabase(url+database,username,password);
		// String mydb= context.getInitParameter("parametername");
		System.out.println(password);
	}

}
