package SSH.service.impl;

import SSH.dao.FilmDao;
import SSH.entity.Film;
import SSH.service.IFilmService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class FilmServiceImpl implements IFilmService {
    private FilmDao filmDao;

    public void setFilmDao(FilmDao filmDao) {
        this.filmDao = filmDao;
    }

    @Override
    public List<Film> getAllFilm() {
        return filmDao.getAllFilm();
    }

    @Override
    public Film getFilmById(Long id) {
        return filmDao.getFilmById(id);
    }

    @Override
    public List<Film> getFilmsByDirector(String director) {
        return filmDao.getFilmByDirector(director);
    }

    @Override
    public void save(Film film) {
        filmDao.save(film);
    }

    @Override
    public List<Film> getFilmsByType(String type) {
        return filmDao.getFilmsByType(type);
    }

    @Override
    public void saveByNewFilm(Film film) {
        filmDao.saveOrUpdate(film);
    }

    @Override
    public void deleteFilmById(Long id) {
        filmDao.deleteFilmById(id);
    }

    @Override
    public List<Film> getFilmsByName(String name) {
        return filmDao.getFilmsByName(name);
    }

    @Override
    public List<Film> getFilmByKey(String name) {
        return filmDao.getFilmByKey(name);
    }
}
