package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import com.meetingroom.servlet.JsonDateValueProcessor;

import oracle.pool.database.DBConnect;

public class getMapTest extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public getMapTest() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

         doPost(request,response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		ArrayList result=new ArrayList();
		
		String	sql="select trim(c.region) region,count(*) total from customer c where c.region is not " +
				"null group by trim(c.region) order by total desc";
		
		try {
			DBConnect db=new DBConnect();
			ResultSet res=null;
			
		   res=	db.executeQuery(sql);
		   
		   while(res.next()){
			   mapElement mp=new mapElement();
			   mp.setName(res.getString("region"));
			   mp.setValue(res.getInt("total"));
			   result.add(mp);
			   
			    
		   }
		   
		   db.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//JsonConfig是net.sf.json.JsonConfig中的,这个为固定写法    
	       JsonConfig jsonConfig = new JsonConfig();    
	        jsonConfig.registerJsonValueProcessor(Date.class , new JsonDateValueProcessor());    

		
	        //将List转化为JSON
	        JSONArray json=JSONArray.fromObject(result,jsonConfig);
	        //设置编码
	        response.setCharacterEncoding("utf-8");
	        //写入到前台
	        response.getWriter().write(json.toString());
	     
		
			
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
