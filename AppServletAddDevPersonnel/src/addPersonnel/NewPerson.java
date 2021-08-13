package addPersonnel;
/*
# Name:			    Ian Taylor
# CourseID: 		SER 422
# Date:			    05/24/2021
# Vers:             1.0.0
# Description:      A Handles functionality to add a new person.
*/

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class NewPerson extends HttpServlet{
    public void init(ServletConfig sc) throws ServletException {
        super.init(sc);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        StringBuilder sb = new StringBuilder("<html><head><title>Error</title></head>");
        sb.append("<br><br> 403 Forbidden <br> Get requests not accepted.</body></html>");
        res.setContentType("text/html");
        res.setStatus(403);

        PrintWriter out = res.getWriter();
        out.print(sb.toString());

    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // process headers & process request parameters
        PersonsList obj = PersonsList.getInstance();

        String firstName = req.getParameter("fname");
        String lastName = req.getParameter("lname");
        String [] pLang = req.getParameterValues("planguages");
        String [] days = req.getParameterValues("days");
        String [] movies = req.getParameterValues("movies");

        String scheme = req.getScheme(); // http
        String serverName = req.getServerName(); // localhost
        int serverPort = req.getServerPort(); // 8080
        String contextPath = req.getContextPath(); // /lab1task1

        // Reconstruct original requesting URL
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName).append(":")
                .append(serverPort).append(contextPath);

        // perform processing + assemble response payload

        PrintWriter out = res.getWriter();
        StringBuilder sb = new StringBuilder("<html><head><title>Add Person</title></head>");

        Person newPerson = new Person(firstName, lastName, pLang, days, movies);

        if(firstName == null && lastName == null){
            sb.append("<b>Must have a firstname and a Last to create a new person!</b>");
            sb.append("</body></html>");
            res.setStatus(res.SC_PRECONDITION_FAILED);
        } else if(checkIfPersonExistsAlready(newPerson, obj.getList())){
            sb.append("A record of this individual already exists. " +
                    "Please use a PUT to update the profile.");
            sb.append("</body></html>");
            res.setStatus(res.SC_METHOD_NOT_ALLOWED);
        } else {

            Boolean added = obj.addPerson(newPerson);
            res.setContentType("text/html");

            if(added){
                sb.append("<br>Person has been Added!! <b>");
                sb.append("<a href=" + url + ">Homepage</a>");
                sb.append("<br> List size is: " + obj.listSize());
                sb.append("</body></html>");
                res.setStatus(res.SC_OK);
            }else {
                sb.append("<br> Unable to add person please try again.");
                sb.append("</body></html>");
                res.setStatus(res.SC_NOT_ACCEPTABLE);
            }
        }

        //Creating two cookies
        try {
            Cookie c1 = new Cookie("Lab1cookie_firstName",firstName);
            Cookie c2 = new Cookie("Lab1cookie_lastName",lastName);
            res.addCookie(c1);
            res.addCookie(c2);

        }catch(IllegalArgumentException i){
            sb.append("Error creating a cookie with those user inputs." + i);
        }
        out.print(sb.toString());
    }

    public void doPut(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        PersonsList obj = PersonsList.getInstance();

        Map<String, String> dataMap = getPutReqParam(req,res);

        String firstName = null;
        String lastName = null;
        String [] pLang = new String[1];
        String [] days = new String[1];
        String [] movies= new String[1];

        // String url = req.getRequestURL().toString(); // http://localhost:8080/lab1task1/newperson

        String scheme = req.getScheme(); // http
        String serverName = req.getServerName(); // localhost
        int serverPort = req.getServerPort(); // 8080
        String contextPath = req.getContextPath(); // /lab1task1

        // Reconstruct original requesting URL
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName).append(":")
                .append(serverPort).append(contextPath);

        PrintWriter out = res.getWriter();
        StringBuilder sb = new StringBuilder("<html><head><title>Add Person</title></head>");

        for(String s : dataMap.keySet()){
            String val = dataMap.get(s);
            //sb.append("key : " + s + "=" + val);
            if(s.equals("lname")){
                lastName = val;
            } else if(s.equals("fname")){
                firstName = val;
            } else if(s.equals("pLang")){
                pLang[0] = val;
            } else if(s.equals("movie")){
                movies[0] = val;
            } else if(s.equals("day")){
                days[0] = val;
            }
        }

        Person newPerson = new Person(firstName, lastName, pLang, days, movies);

        if(firstName == null && lastName == null){
            sb.append("<b>Must have a firstname and a lastname to create a new person!</b>");
            sb.append("</body></html>");
            out.println(sb.toString());
            res.setStatus(res.SC_PRECONDITION_FAILED);
        } else if(!checkIfPersonExistsAlready(newPerson, obj.getList())){
            sb.append("A record of this individual needs to exists " +
                    "for you to update it. Please use the POST method to add before editing");
            sb.append("</body></html>");
            out.println(sb.toString());
            res.setStatus(res.SC_METHOD_NOT_ALLOWED);
        } else {

            Boolean added = obj.addPerson(newPerson);
            res.setContentType("text/html");

            if (added) {
                sb.append("<br>Person information has been updated. <b>");
                sb.append("<a href=" + url + ">Homepage</a>");
                sb.append("<br> List size is: " + obj.listSize());
                sb.append("</body></html>");
                out.println(sb.toString());
                res.setStatus(res.SC_OK);
            } else {
                sb.append("<br> Unable to edit the persons profile please try again.");
                sb.append("</body></html>");
                out.println(sb.toString());
                res.setStatus(res.SC_NOT_ACCEPTABLE);
            }
        }
    }

    public boolean checkIfPersonExistsAlready(Person newP, List<Person> pList){

        boolean nameExists = false;

        for (Person p : pList){
            if (p.getfName().equals(newP.getfName()) && p.getlName().equals(newP.getlName())) {
                nameExists = true;
                break;
            }
        }

        return nameExists;
    }

    public Map<String, String> getPutReqParam(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        BufferedReader br = null;
        HashMap<String, String> dataMap = new HashMap<>();
        PrintWriter out = res.getWriter();

        try {
            InputStreamReader reader = new InputStreamReader(req.getInputStream());
            br = new BufferedReader(reader);

            String data = br.readLine();

            for(String keyValue : data.split(" *& *")) {
                String[] pairs = keyValue.split(" *= *", 2);
                dataMap.put(pairs[0], pairs.length == 1 ? "" : pairs[1]);
            }
            return dataMap;
        } catch (IOException ex) {
            out.println("Error parsing parameters for PUT request");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {

                }
            }
            return dataMap;
        }
    }
}
