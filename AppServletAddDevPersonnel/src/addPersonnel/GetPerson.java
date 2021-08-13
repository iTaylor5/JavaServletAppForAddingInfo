package addPersonnel;

/*
# Name:			    Ian Taylor
# CourseID: 		SER 422
# Date:			    05/24/2021
# Vers:             1.0.0
# Description:      A servlet providing functionality to retrieve a person from the Data structure
                    storing all the people enter from NewPersonServlet.
*/

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetPerson extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        List<Person> finalFilteredListed = new LinkedList<>();
        PersonsList obj = PersonsList.getInstance();
        PrintWriter out = res.getWriter();

        StringBuilder sb = new StringBuilder("<html><head><title>Add Person</title></head>");

        String query = "";
        // Ensure correct query in GET request.
        if(req.getRequestURL().indexOf("?") > 0){
            query = req.getQueryString();
        }

        //sb.append("query: "+ query);
        String [] correctQueries = {"firstname", "lastname", "language", "day", "favoriteMovies"};
        String [] querySplit = query.split("&");

        boolean correct = false;

        if(query.equals("")){
            correct = true;
        } else {
            for(String s : querySplit){
                sb.append("s: "+ s);
                String [] queryKeys = s.split("=");
                if(Arrays.asList(correctQueries).contains(queryKeys[0])){
                    correct = true;
                    break;
                }
            }
        }

        if(!correct){
            renderErrorResponse(res, 400, out, "Incorrect query key");
        } else {

            String fn = req.getParameter("firstname");
            String ln = req.getParameter("lastname");
            String lang = req.getParameter("language");
            String days = req.getParameter("day");
            String favM = req.getParameter("favoriteMovies");

            String h = req.getHeader("Content-Type");

            List<Person> contFName = new LinkedList<>();

            // if firstname contains the query value for firstname
            if(fn != null){
                for ( Person per : obj.getList()){
                    if(per.getfName().contains(fn)){
                        contFName.add(per);
                    }
                }
            } else {
                contFName = obj.getList();
            }

            // Lets filter on last names
            List<Person> contLName = new LinkedList<>();

            // if lastname contains the query value for lastname
            if(ln != null){
                for ( Person per : contFName){
                    if(per.getlName().contains(ln)){
                        contLName.add(per);
                    }
                }
            }else {
                contLName = contFName;
            }

            // Languages
            List<Person> contLang = new LinkedList<>();
            if(lang != null){
                for ( Person per : contLName){
                    if(per.getpLang().equals(lang))
                        contLang.add(per);
                }
            } else {
                contLang = contLName;
            }

            // Days
            List<Person> contDays = new LinkedList<>();

            if(days != null){
                for ( Person per : contLang){
                    if(per.getAvailability().equals(days)) {
                        contDays.add(per);
                    }
                }
            } else {
                contDays = contLang;
            }

            // Movies
            List<Person> contMovies = new LinkedList<>();

            if(favM != null){
                for ( Person per : contDays){
                    if(per.getFavMovies().equals(favM)) {
                        contMovies.add(per);
                    }
                }
            } else {
                contMovies = contDays;
            }


            // Display Results....
            if (contMovies.size() == 0){
                sb.append("Nothing meets that criteria");
            } else{
                for(Person p : contMovies){
                    sb.append("<br>" + p.getfName() + " " + p.getlName() +
                            "<br>" + Arrays.toString(p.getpLang()) +
                            "<br>" + Arrays.toString(p.getAvailability()) + "<br>" +
                            Arrays.toString((p.getFavMovies())));
                }
            }
            finalFilteredListed = contMovies;

            if(h != null){
                if(h.contains("html")){

                    sb.append("<br><br></body></html>");
                    // assign response headers
                    res.setContentType("text/html");
                    res.setStatus(res.SC_OK);
                    out.print(sb.toString());

                } else if (h.equals("application/json")){
                    JSONObject jsonObj = new JSONObject();
                    JSONArray jsonArray = new JSONArray(finalFilteredListed);
                    jsonObj.append("FilteredResults", jsonArray);
                    out.println(jsonObj.toString());
                }else {
                    out.print("h =" + h + "\nIncorrect Content-Type set in header.");
                }
            } else {
                out.print("No Content-Type set in header. Default used will be....");
                sb.append("<br><br></body></html>");
                // assign response headers
                res.setContentType("text/html");
                res.setStatus(res.SC_OK);
                out.print(sb.toString());
            }
        }
    }

    public void renderErrorResponse(HttpServletResponse response, int statusCode, PrintWriter out, String message){
        response.setStatus(statusCode);
        out.println("<p>The server encountered the following error.</p>");
        out.println("<p>" + message + "</p>");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException	{
        PrintWriter out = res.getWriter();
        //response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "POST not supported by this servlet");
        renderErrorResponse(res, HttpServletResponse.SC_NOT_IMPLEMENTED, out, "POST not supported by this servlet");
    }

    public void doPut(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException	{
        PrintWriter out = res.getWriter();
        renderErrorResponse(res, HttpServletResponse.SC_NOT_IMPLEMENTED, out, "PUT not supported by this servlet");
    }

    public void doPatch(HttpServletResponse res)
            throws ServletException, IOException	{
        PrintWriter out = res.getWriter();
        renderErrorResponse(res, HttpServletResponse.SC_NOT_IMPLEMENTED, out, "PATCH not supported by this servlet");
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException	{
        PrintWriter out = res.getWriter();
        renderErrorResponse(res, HttpServletResponse.SC_NOT_IMPLEMENTED, out, "DELETE not supported by this servlet");
    }
}
