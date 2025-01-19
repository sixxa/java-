package p2;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class SintXP2 extends HttpServlet {
    private String letter;
    private int year;
    private DataModel dataModel;

    @Override
    public void init() throws ServletException {
        try {
            String examFilePath = getServletContext().getRealPath("/WEB-INF/exam.xml");
            File examFile = new File(examFilePath);

            if (!examFile.exists()) {
                throw new FileNotFoundException("File not found: " + examFilePath);
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(examFile);
            doc.getDocumentElement().normalize();

            Element exElement = (Element) doc.getElementsByTagName("ex").item(0);
            letter = exElement.getAttribute("letter");
            year = Integer.parseInt(exElement.getElementsByTagName("year").item(0).getTextContent());

            String dataFilePath = getServletContext().getRealPath("/WEB-INF/mml.xml");
            dataModel = new DataModel(dataFilePath);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error initializing servlet: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pphase = request.getParameter("pphase");

        String actorName = request.getParameter("actor");

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        String clientIP = request.getRemoteAddr();
        String browserInfo = request.getHeader("User-Agent");
        out.println("<p><strong>Your IP Address:</strong> " + clientIP + "</p>");
        out.println("<p><strong>Your Browser:</strong> " + browserInfo + "</p>");

        try {
            switch (pphase) {
                case "0": // Phase 0: Language Selector
                    displayLanguageSelector(out, letter, year);
                    break;
                case "1":
                    displayHomeScreen(out);
                    break;
                case "2":
                    displayActors(out, request);
                    break;
                case "3":
                    displayMovies(out);
                    break;
                default:
                    out.println("<h1>Invalid Phase</h1>");
            }
        } catch (Exception e) {
            out.println("<h1>Error: " + e.getMessage() + "</h1>");
        }
    }

    private void displayLanguageSelector(PrintWriter out, String letter, int year) {
        out.println("<html>");
        out.println("<head><title>Select Language</title></head>");
        out.println("<body>");
        out.println("<h1>The Letter: " + letter + "</h1>");
        out.println("<h1>The Year: " + year + "</h1>");
        out.println("<h1>Select a Language</h1>");
        out.println("<form action=\"\" method=\"GET\">");
        out.println("<input type=\"hidden\" name=\"pphase\" value=\"1\">");
        out.println("<select name=\"language\">");
        out.println("<option value=\"English\">English</option>");
        out.println("<option value=\"Spanish\">Spanish</option>");
        out.println("<option value=\"French\">French</option>");
        out.println("</select>");
        out.println("<button type=\"submit\">Submit</button>");
        out.println("</form>");
        out.println("</body></html>");
    }

    private void displayHomeScreen(PrintWriter out) {
        out.println("<html><head><title>Home</title></head><body>");
        out.println("<h1>Home Screen</h1>");
        out.println("<p>Letter: " + letter + "</p>");
        out.println("<p>Year: " + year + "</p>");
        out.println("<a href=\"?pphase=2\">Actors</a><br>");
        out.println("<a href=\"?pphase=3\">Movies</a>");
        out.println("</body></html>");
    }

    private void displayActors(PrintWriter out, HttpServletRequest request) {
        String actorName = request.getParameter("actor");

        if (actorName != null && !actorName.isEmpty()) {
            ArrayList<Movie> movies = dataModel.getMoviesByActor(actorName);

            out.println("<html><head><title>Movies by " + actorName + "</title></head><body>");
            out.println("<h1>Movies Starring: " + actorName + "</h1>");
            out.println("<ul>");
            for (Movie movie : movies) {
                out.println("<li>" + movie.getTitle() + " (" + movie.getYear() + ")</li>");
            }
            out.println("</ul>");
            out.println("<a href=\"?pphase=2\">Back to Actors</a>");
            out.println("</body></html>");
        } else {
            ArrayList<Cast> actors = dataModel.getCastByLetter(letter);
            LinkedHashSet<String> uniqueActors = new LinkedHashSet<>();

            for (Cast actor : actors) {
                uniqueActors.add(actor.getName() + " (" + actor.getRole() + ")");
            }

            out.println("<html><head><title>Actors</title></head><body>");
            out.println("<h1>Actors/Actresses Starting with '" + letter + "'</h1>");
            out.println("<ul>");
            for (String actor : uniqueActors) {
                String actorNameOnly = actor.split(" \\(")[0]; // Extract only the actor's name
                out.println("<li><a href=\"?pphase=2&actor=" + actorNameOnly + "\">" + actor + "</a></li>");
            }
            out.println("</ul>");
            out.println("<a href=\"?pphase=1\">Back</a>");
            out.println("</body></html>");
        }
    }


    private void displayMovies(PrintWriter out) {
        ArrayList<Movie> movies = dataModel.getMoviesByYear(year);

        out.println("<html><head><title>Movies</title></head><body>");
        out.println("<h1>Movies After " + year + "</h1>");
        out.println("<ul>");
        for (Movie movie : movies) {
            out.println("<li>" + movie.getTitle() + " (" + movie.getYear() + ")</li>");
        }
        out.println("</ul>");
        out.println("<a href=\"?pphase=1\">Back</a>");
        out.println("</body></html>");
    }
}
