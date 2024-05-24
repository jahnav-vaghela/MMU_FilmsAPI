package me.jahnav.films_api.controlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.PropertyException;

import me.jahnav.films_api.database.FilmDAO;
import me.jahnav.films_api.models.Film;
import me.jahnav.films_api.Response;


@MultipartConfig
@WebServlet(name = "FilmsApiControler", value = "/")
public class FilmsApiControler extends HttpServlet {

    /**
     * start
     */

    public void init() {

    }

    /**
     * Get Request
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // read header value
        String  response_type = "";
        response_type += request.getHeader("response_type");

        //response_type = "xml"; // only for testing


        // get Films from db
        FilmDAO filmdb = new FilmDAO();
        ArrayList<Film> filmlist = filmdb.getAllFilms();

        if(response_type.equals("html")){
            //response.setContentType("text/html");
            // set attributes
            request.setAttribute("filmlist", filmlist);
            // load jsp html
            RequestDispatcher dispatcher = request.getRequestDispatcher("films.jsp");
            dispatcher.include(request,response);
        } else if (response_type.equals("xml")) {
            response.setContentType("text/xml");

            Response res = new Response(true,"data is hear" );
            res.films = filmlist;

            String xml = " Error in XML creation";

            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Response.class);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                StringWriter stringWriter = new StringWriter();
                marshaller.marshal(res, stringWriter);

                xml = stringWriter.toString();

            } catch (PropertyException e) {
                throw new RuntimeException(e);
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }

            response.getWriter().write(xml);

        } else if (response_type.equals("json")) {
            response.setContentType("application/json");

            Response res = new Response(true,"data is hear" );
            res.films = filmlist;
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(res);
            response.getWriter().write(json);

        }else{ // default text
            response.setContentType("text/plain");
            if(!filmlist.isEmpty()) {
                for (Film film : filmlist) {
                    response.getWriter().write("\n ");
                    response.getWriter().write(film.toString());
                }
            }else{
                response.getWriter().write("No data Found.");
            }
        }

    }

    /**
     * Post Request
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // read header value
        String  response_type = "";
        response_type += request.getHeader("response_type");

        //response_type = "html"; // only for testing

        Response res = new Response();
        res.films = null;

        // read post form data
        Film film = new Film();
        film.setTitle( request.getParameter("title") );
        film.setYear(Integer.parseInt(request.getParameter("year")));
        film.setDirector(request.getParameter("director"));
        film.setStars(request.getParameter("stars"));
        film.setReview(request.getParameter("review"));
        /*
        film.setTitle(" test test Test ");
        film.setYear(2025);
        film.setDirector("DD Director");
        film.setStars("Simpn Oner Sdfloid ");
        film.setReview("onliner dsfasdfdf");
        */

        // save new Film in db
        FilmDAO filmdb = new FilmDAO();
        int r = filmdb.addFilm(film); // add in db
        if( r == 0 ){
            res.success = false;
            res.msg = "film data not added in dababase.";
        }
        if(r > 0){
            res.success = true;
            res.msg = "Film data added in database.";
        }

        // Responce diffrent formate
        if(response_type.equals("html")){
            //response.setContentType("text/html");
            // set attributes
            request.setAttribute("success", res.success );
            request.setAttribute("msg", res.msg );
            // load jsp html
            RequestDispatcher dispatcher = request.getRequestDispatcher("add-update-delete.jsp");
            dispatcher.include(request,response);

        } else if (response_type.equals("xml")) {
            response.setContentType("text/xml");

            String xml = " Error in XML creation";

            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Response.class);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                StringWriter stringWriter = new StringWriter();
                marshaller.marshal(res, stringWriter);

                xml = stringWriter.toString();

            } catch (PropertyException e) {
                throw new RuntimeException(e);
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }

            response.getWriter().write(xml);

        } else if (response_type.equals("json")) {
            response.setContentType("application/json");

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(res);
            response.getWriter().write(json);

        }else{ // default text
            response.setContentType("text/plain");

            response.getWriter().write("\n sucsess="+res.success);
            response.getWriter().write("\n msg="+res.msg);

        }

    }

    /**
     * Put Request
     */
    public void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // read header value
        String  response_type = "";
        response_type += request.getHeader("response_type");

        //response_type = "text"; // only for testing


        Response res = new Response();
        res.films = null;

        // read post form data
        Film film = new Film();
        film.setId(Integer.parseInt(request.getParameter("film_id")));
        film.setTitle(request.getParameter("title"));
        film.setYear(Integer.parseInt(request.getParameter("year")));
        film.setDirector (request.getParameter("director"));
        film.setStars(request.getParameter("stars"));
        film.setReview(request.getParameter("review"));

        // save new Film in db
        FilmDAO filmdb = new FilmDAO();
        int r = filmdb.updateFilm(film); // update in db
        if( r == 0 ){
            res.success = false;
            res.msg = "film data not updated in database.";
        }
        if(r > 0){
            res.success = true;
            res.msg = "Film data updated in database.";
        }


        // Responce diffrent formate
        if(response_type.equals("html")){
            response.setContentType("text/html");
            // set attributes
            //request.setAttribute("success", res.success );
            //request.setAttribute("msg", res.msg );
            // load jsp html
            //RequestDispatcher dispatcher = request.getRequestDispatcher("add-update-delete.jsp");
            //dispatcher.include(request,response);

            String str = "";
            str += "<section class=\"responce\" data-responce-type=\"html\">";
            str += "<div class=\"success\">"+ res.success + "</div>";
            str += "<div class=\"msg\">"+ res.msg + "</div>";
            str += "</section>";
            response.getWriter().write(str);

            // jsp only support GET POST and OPTION method

        } else if (response_type.equals("xml")) {
            response.setContentType("text/xml");

            String xml = " Error in XML creation";

            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Response.class);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                StringWriter stringWriter = new StringWriter();
                marshaller.marshal(res, stringWriter);

                xml = stringWriter.toString();

            } catch (PropertyException e) {
                throw new RuntimeException(e);
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }

            response.getWriter().write(xml);

        } else if (response_type.equals("json")) {
            response.setContentType("application/json");

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(res);
            response.getWriter().write(json);

        }else{ // default text
            response.setContentType("text/plain");

            response.getWriter().write("\n sucsess="+res.success);
            response.getWriter().write("\n msg="+res.msg);

        }

    }

    /**
     * Delete Request
     */
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // read header value
        String  response_type = "";
        response_type += request.getHeader("response_type");

        //response_type = "text"; // only for testing


        Response res = new Response();
        res.films = null;

        // read post form data
        int film_id = Integer.parseInt(request.getParameter("film_id"));

        System.out.println(film_id);


        // save new Film in db
        FilmDAO filmdb = new FilmDAO();
        int r = filmdb.deleteFilm(film_id); // delete from db
        if(r == 0){
            //response.getWriter().write("Error: Book not added.");
            res.success = false;
            res.msg = "Error: film data not deleted in database.";
        }
        if(r > 0){
            res.success = true;
            res.msg = "Film deleted from database.";
        }

        // Responce diffrent formate
        if(response_type.equals("html")){
            response.setContentType("text/html");
            // set attributes
            //request.setAttribute("success", res.success );
            //request.setAttribute("msg", res.msg );
            // load jsp html
            //RequestDispatcher dispatcher = request.getRequestDispatcher("add-update-delete.jsp");
            //dispatcher.include(request,response);

            String str = "";
            str += "<section class=\"responce\" data-responce-type=\"html\">";
            str += "<div class=\"success\">"+ res.success + "</div>";
            str += "<div class=\"msg\">"+ res.msg + "</div>";
            str += "</section>";
            response.getWriter().write(str);

            // jsp only support GET POST and OPTION method

        } else if (response_type.equals("xml")) {
            response.setContentType("text/xml");

            String xml = " Error in XML creation";

            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Response.class);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                StringWriter stringWriter = new StringWriter();
                marshaller.marshal(res, stringWriter);

                xml = stringWriter.toString();

            } catch (PropertyException e) {
                throw new RuntimeException(e);
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }

            response.getWriter().write(xml);

        } else if (response_type.equals("json")) {
            response.setContentType("application/json");

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(res);
            response.getWriter().write(json);

        }else{ // default text
            response.setContentType("text/plain");

            response.getWriter().write("\n sucsess="+res.success);
            response.getWriter().write("\n msg="+res.msg);

        }

    }

    /**
     * End
     */
    public void destroy() {

    }


}
