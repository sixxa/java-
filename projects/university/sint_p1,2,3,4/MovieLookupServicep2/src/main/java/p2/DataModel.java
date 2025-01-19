package p2;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class DataModel {
    private Document dom; // DOM representation of the XML file

    public DataModel(String filePath) throws Exception {
        System.out.println("Initializing DataModel with file: " + filePath);
        File xmlFile = new File(filePath);
        if (!xmlFile.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        dom = builder.parse(xmlFile);
        dom.getDocumentElement().normalize();
        System.out.println("XML file parsed successfully.");
    }

    public ArrayList<String> getLangs() {
        ArrayList<String> languages = new ArrayList<>();
        NodeList movieNodes = dom.getElementsByTagName("movie");

        for (int i = 0; i < movieNodes.getLength(); i++) {
            Element movie = (Element) movieNodes.item(i);
            String langs = movie.getAttribute("langs");
            for (String lang : langs.split(" ")) {
                if (!languages.contains(lang)) {
                    languages.add(lang);
                }
            }
        }
        languages.sort(String::compareTo);
        return languages;
    }

    public ArrayList<Cast> getCast(String lang) {
        ArrayList<Cast> castList = new ArrayList<>();
        NodeList movieNodes = dom.getElementsByTagName("movie");

        for (int i = 0; i < movieNodes.getLength(); i++) {
            Element movie = (Element) movieNodes.item(i);
            String langs = movie.getAttribute("langs");
            if (langs.contains(lang)) {
                NodeList castNodes = movie.getElementsByTagName("cast");
                for (int j = 0; j < castNodes.getLength(); j++) {
                    Element cast = (Element) castNodes.item(j);
                    String idC = cast.getAttribute("idC");
                    String name = cast.getElementsByTagName("name").item(0).getTextContent();
                    String role = cast.getElementsByTagName("role").item(0).getTextContent();
                    castList.add(new Cast(idC, name, role));
                }
            }
        }
        return castList;
    }

    public ArrayList<Movie> getMovies(String idC) {
        ArrayList<Movie> movieList = new ArrayList<>();
        NodeList movieNodes = dom.getElementsByTagName("movie");

        for (int i = 0; i < movieNodes.getLength(); i++) {
            Element movie = (Element) movieNodes.item(i);
            NodeList castNodes = movie.getElementsByTagName("cast");
            for (int j = 0; j < castNodes.getLength(); j++) {
                Element cast = (Element) castNodes.item(j);
                if (cast.getAttribute("idC").equals(idC)) {
                    String idM = movie.getAttribute("idM");
                    String title = movie.getElementsByTagName("title").item(0).getTextContent();
                    int year = Integer.parseInt(movie.getElementsByTagName("year").item(0).getTextContent());
                    movieList.add(new Movie(idM, title, year));
                    break;
                }
            }
        }
        return movieList;
    }

    public ArrayList<Movie> getMoviesByActor(String actorName) {
        ArrayList<Movie> movieList = new ArrayList<>();
        NodeList movieNodes = dom.getElementsByTagName("movie");

        for (int i = 0; i < movieNodes.getLength(); i++) {
            Element movie = (Element) movieNodes.item(i);
            NodeList castNodes = movie.getElementsByTagName("cast");

            for (int j = 0; j < castNodes.getLength(); j++) {
                Element cast = (Element) castNodes.item(j);
                String name = cast.getElementsByTagName("name").item(0).getTextContent();

                if (name.equalsIgnoreCase(actorName)) {
                    String idM = movie.getAttribute("idM");
                    String title = movie.getElementsByTagName("title").item(0).getTextContent();
                    int year = Integer.parseInt(movie.getElementsByTagName("year").item(0).getTextContent());
                    movieList.add(new Movie(idM, title, year));
                    break; // No need to check other casts for this movie
                }
            }
        }

        return movieList;
    }


    public ArrayList<Cast> getCastByLetter(String letter) {
        ArrayList<Cast> filteredCast = new ArrayList<>();
        NodeList movieNodes = dom.getElementsByTagName("movie");

        for (int i = 0; i < movieNodes.getLength(); i++) {
            NodeList castNodes = ((Element) movieNodes.item(i)).getElementsByTagName("cast");
            for (int j = 0; j < castNodes.getLength(); j++) {
                Element cast = (Element) castNodes.item(j);
                String name = cast.getElementsByTagName("name").item(0).getTextContent();
                if (name.startsWith(letter)) {
                    String idC = cast.getAttribute("idC");
                    String role = cast.getElementsByTagName("role").item(0).getTextContent();
                    filteredCast.add(new Cast(idC, name, role));
                }
            }
        }
        return filteredCast;
    }

    public ArrayList<Movie> getMoviesByYear(int year) {
        ArrayList<Movie> filteredMovies = new ArrayList<>();
        NodeList movieNodes = dom.getElementsByTagName("movie");

        for (int i = 0; i < movieNodes.getLength(); i++) {
            Element movie = (Element) movieNodes.item(i);
            int movieYear = Integer.parseInt(movie.getElementsByTagName("year").item(0).getTextContent());
            if (movieYear >= year) {
                String idM = movie.getAttribute("idM");
                String title = movie.getElementsByTagName("title").item(0).getTextContent();
                filteredMovies.add(new Movie(idM, title, movieYear));
            }
        }
        return filteredMovies;
    }

}
