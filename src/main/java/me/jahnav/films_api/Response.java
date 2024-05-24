package me.jahnav.films_api;

import jakarta.xml.bind.annotation.XmlRootElement;
import me.jahnav.films_api.models.Film;
import java.util.ArrayList;

@XmlRootElement
public class Response {
    public boolean success;
    public String  msg;
    public ArrayList<Film> films;

    public  Response(){}

    public Response(Boolean success, String msg ){
        this.success = success;
        this.msg = msg;
    }
}
