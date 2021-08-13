package addPersonnel;

/*
# Name:			    Ian Taylor
# CourseID: 		SER 422
# Date:			    05/24/2021
# Vers:             1.0.0
# Description:      A servlet providing a Form page to add users. Once Tomcat is running access home page at
                    http://localhost:8080/lab1task1/
                    Retreive added users @
                    http://localhost:8080/lab1task1/getpersons

*/

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class WelcomeServlet extends HttpServlet {

    private static String _filename = null;
    private PersonsList obj;

    public void init(ServletConfig config) throws ServletException {
        // if you forget this your getServletContext() will get a NPE!
        super.init(config);
        _filename = config.getInitParameter("personsList");
        if (_filename == null || _filename.length() == 0) {
            throw new ServletException();
        }
        obj = PersonsList.getInstance();
        obj.initFileName(_filename);
        //System.out.println("Loaded init param phonebook with value " + _filename);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String fname="";
        String lname="";
        PrintWriter out = res.getWriter();
        StringBuilder sb =
                new StringBuilder("<html><head><title>Welcome Servlet</title></head><style type=\"text/css\">\n" +
                        "        html {\n" +
                        "            height: 100%;\n" +
                        "        }\n" +
                        "\n" +
                        "        body {\n" +
                        "            max-width: max-content;\n"+
                        "            margin: auto;\n"+
                        "            padding: 20px;\n"+
                        "            padding-top: 100px;\n"+
                        "            width: 60%;\n" +
                        "            height: 50%;\n" +
                        "            background-color: #45ADA8;\n"+
                        "        }\n" +
                        "        div {\n"+
                        "            border: 1px solid;\n" +
                        "            padding: 20px;\n"+
                        "            background-color: #355C7D;"+
                        "        }\n" +
                        "    </style><body><div classname=\"box\">");

        List<Person> array = obj.getList();

        Cookie[] cookies = req.getCookies();

        if(cookies != null){
            for (Cookie c : cookies){
                if(c.getName().equals("Lab1cookie_firstName")){
                    fname = c.getValue();
                } else if (c.getName().equals("Lab1cookie_lastName")){
                    lname = c.getValue();
                }
            }
        }


        sb.append("<h3>Please fill out the following form: </h3>\n" +
                "    <p>\n" +
                "        <form method=\"post\" action=\"newperson\">\n" +
                "            <label>First Name: </label>\n" +
                "            <input type=\"text\" name=\"fname\" placeholder=\" \" value=\""+fname+" \"/>\n" +
                "            <br/><br/>\n" +
                "\n" +
                "            <label>Last Name: </label>\n" +
                "            <input type=\"text\" name=\"lname\" placeholder=\" \" value=\""+lname+"\"/>\n" +
                "            <br/><br/>\n" +
                "\n" +
                "            <label>Programming languages you're proficient in</label>\n" +
                "            <input type=\"checkbox\" name=\"planguages\" value=\"Java\">Java</input>\n" +
                "            <input type=\"checkbox\" name=\"planguages\" value=\"Javascript\">Javascript</input>\n" +
                "            <input type=\"checkbox\" name=\"planguages\" value=\"Python\">Python</input>\n" +
                "            <input type=\"checkbox\" name=\"planguages\" value=\"C\">C</input>\n" +
                "            <input type=\"checkbox\" name=\"planguages\" value=\"C++\">C++</input>\n" +
                "            <input type=\"checkbox\" name=\"planguages\" value=\"C#\">C#</input>\n" +
                "            <br/><br/>\n" +
                "\n" +
                "            <label>When are you available?</label>\n" +
                "            <input type=\"checkbox\" name=\"days\" value=\"monday\">Monday</input>\n" +
                "            <input type=\"checkbox\" name=\"days\" value=\"tuesday\">Tuesday</input>\n" +
                "            <input type=\"checkbox\" name=\"days\" value=\"wednesday\">Wednesday</input>\n" +
                "            <input type=\"checkbox\" name=\"days\" value=\"thursday\">Thursday</input>\n" +
                "            <input type=\"checkbox\" name=\"days\" value=\"friday\">Friday</input>\n" +
                "            <input type=\"checkbox\" name=\"days\" value=\"saturday\">Saturday</input>\n" +
                "            <input type=\"checkbox\" name=\"days\" value=\"sunday\">Sunday</input>\n" +
                "            <br/><br/>\n" +
                "\n" +
                "            <label>Favorite Role:</label>\n" +
                "            <input type=\"checkbox\" name=\"movies\" value=\"Front-end\">Front-end</input>\n" +
                "            <input type=\"checkbox\" name=\"movies\" value=\"Back-end\">Back-end</input>\n" +
                "            <input type=\"checkbox\" name=\"movies\" value=\"DevOps\">DevOps</input>\n" +
                "            <input type=\"checkbox\" name=\"movies\" value=\"Database Admin\">Database Admin</input>\n" +
                "            <input type=\"checkbox\" name=\"movies\" value=\"Full Stack\">Full Stack</input>\n" +
                "            <br/><br/>\n" +
                "\n" +
                "            <input type=\"Submit\" value=\"Submit\" />\n" +
                "        </form>\n" +
                "    </p>");
        sb.append("</div></body></html>");
        res.setContentType("text/html");
        res.setStatus(res.SC_OK);

        out.print(sb.toString());

    }
}
