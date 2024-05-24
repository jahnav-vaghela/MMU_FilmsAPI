package me.jahnav.films_api.database;

import me.jahnav.films_api.models.Film;

//import java.sql.*;
//import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class FilmDAO {

    private Connection connection;
    private String username = "root";
    private String password = "";
    private String dbname  = "sam2proj";
    // Note none default port used, 6306 not 3306
    private String url = "jdbc:mysql://localhost:3306/" + dbname;

    public FilmDAO(){}

    // open connection class
    public void openConnection() {

        // loading jdbc driver for mysql
        try{
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        } catch(  Exception e) { e.printStackTrace(); }

        // connecting to database
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch ( SQLException e) {
            e.printStackTrace();
        }
    }

    public Film fetchFilm(ResultSet resultSet ) throws SQLException {

        Film film = new Film();
        film.setId(resultSet.getInt("id"));
        film.setTitle(resultSet.getString("title"));
        film.setYear(resultSet.getInt("year"));
        film.setDirector(resultSet.getString("director"));
        film.setStars(resultSet.getString("stars"));
        film.setReview(resultSet.getString("review"));

        return film;
    }

    // Method to retrieve all Films from the database
    public Film getFilmById(int film_id) {

        Film film = null;
        openConnection();

        String sql_qry = "SELECT * FROM films WHERE id=?";

        try ( PreparedStatement statement = connection.prepareStatement(sql_qry) ) {

            statement.setInt(1, film_id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                film = fetchFilm(resultSet);
            }
            statement.close();
            closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return film;
    }

    // Method to retrieve all Films from the database
    public ArrayList<Film> getAllFilms() {

        ArrayList<Film> films = new ArrayList<>();
        openConnection();

        String sql_qry = "SELECT * FROM films order by id DESC limit 10";

        try (PreparedStatement statement = connection.prepareStatement(sql_qry);
            ResultSet resultSet = statement.executeQuery()) {

             while (resultSet.next()) {
                Film film = fetchFilm(resultSet);
                films.add(film);
            }
            statement.close();
            closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return films;
    }

    // Method to insert a new film into the database
    public int addFilm(Film film) {

        int r = 0;
        openConnection();
        String sql_str = "INSERT INTO films (title, year, director, stars, review) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement( sql_str )) {

            statement.setString(1, film.getTitle());
            statement.setInt(2, film.getYear());
            statement.setString(3, film.getDirector());
            statement.setString(4, film.getStars());
            statement.setString(5, film.getReview());

            r = statement.executeUpdate();


            statement.close();
            closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  r;
    }

    // Method to update an existing book in the database
    public int updateFilm(Film film) {

        int r = 0; // updated rows
        openConnection();
        String sql_str = "UPDATE films SET title=?, year=?, director=?, stars=?, review=? WHERE id=?";

        try (PreparedStatement statement = connection.prepareStatement( sql_str )) {

            statement.setString(1, film.getTitle());
            statement.setInt(2, film.getYear());
            statement.setString(3, film.getDirector());
            statement.setString(4, film.getStars());
            statement.setString(5, film.getReview());
            statement.setInt(6, film.getId());

            r = statement.executeUpdate();

            statement.close();
            closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return r;
    }

    // Method to delete a Film from the database
    public int deleteFilm(int id) {

        int r = 0;
        openConnection();

        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM films WHERE id=?")) {
            statement.setInt(1, id);
            r = statement.executeUpdate();

            statement.close();
            closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return r;
    }


    // Method to close the database connection
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
