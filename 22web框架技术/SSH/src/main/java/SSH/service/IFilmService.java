package SSH.service;

import SSH.entity.Film;

import java.util.List;

public interface IFilmService {
    List<Film> getAllFilm();

    Film getFilmById(Long id);

    List<Film> getFilmsByDirector(String director);

    void save(Film film);

    List<Film> getFilmsByType(String type);

    void saveByNewFilm(Film film);

    void deleteFilmById(Long id);

    List<Film> getFilmsByName(String name);

    List<Film> getFilmByKey(String name);
}
