package SSH.dao;

import SSH.entity.Film;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = false)
public class FilmDao {
    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public List<Film> getAllFilm() {
        return (List<Film>) hibernateTemplate.find("from Film");
    }

    public Film getFilmById(Long id) {
        return hibernateTemplate.get(Film.class, id);
    }

    public List<Film> getFilmByDirector(String director) {
        return (List<Film>) hibernateTemplate.find("from Film where director like ?1", "%" + director + "%");
    }

    public void save(Film film) {
        hibernateTemplate.saveOrUpdate(film);
    }

    public List<Film> getFilmsByType(String type) {
        return (List<Film>) hibernateTemplate.find("from Film where type like ?1", "%" + type + "%");
    }

    public void deleteFilmById(Long id) {
        hibernateTemplate.delete(getFilmById(id));
    }

    public List<Film> getFilmsByName(String name) {
        // 根据name模糊查找，使用hql语句
        String hql = "from Film where name like :filmName";
        return (List<Film>) hibernateTemplate.findByNamedParam(hql, "filmName", "%" + name + "%");
    }

    public List<Film> getFilmByKey(String key) {
        String hql = "from Film where name like :key or director like :key or  type like :key or language like :key ";
        return (List<Film>) hibernateTemplate.findByNamedParam(hql,"key","%" + key + "%");
    }

    public void saveOrUpdate(Film film) {
        hibernateTemplate.saveOrUpdate(film);
    }
}
