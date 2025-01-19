package controllers;

import models.DataModel;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class MainController extends HttpServlet {

    //http://localhost:7137/sint137/P3M?pphase=1

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String phase = request.getParameter("pphase");
        if (phase == null) {
            phase = "1"; // Default to Phase 1
        }

        switch (phase) {
            case "1":
                handlePhase1(request, response);
                break;
            case "2":
                handlePhase2(request, response);
                break;
            case "3":
                handlePhase3(request, response);
                break;
            default:
                response.getWriter().println("Invalid phase.");
        }
    }

    private void handlePhase1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataModel dataModel = new DataModel();
        request.setAttribute("languages", dataModel.getLanguages());
        request.getRequestDispatcher("/WEB-INF/jsp/phase1.jsp").forward(request, response);
    }

    private void handlePhase2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lang = request.getParameter("plang");
        if (lang == null || lang.isEmpty()) {
            response.getWriter().println("Error: Language not provided.");
            return;
        }

        DataModel dataModel = new DataModel();
        request.setAttribute("cast", dataModel.getCast(lang));
        request.setAttribute("lang", lang);
        request.getRequestDispatcher("/WEB-INF/jsp/phase2.jsp").forward(request, response);
    }

    private void handlePhase3(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idC = request.getParameter("pidC");
        if (idC == null || idC.isEmpty()) {
            response.getWriter().println("Error: Actor/Actress ID not provided.");
            return;
        }

        String lang = request.getParameter("plang");
        DataModel dataModel = new DataModel();
        request.setAttribute("movies", dataModel.getMovies(idC));
        request.setAttribute("lang", lang);
        request.getRequestDispatcher("/WEB-INF/jsp/phase3.jsp").forward(request, response);
    }
}
