package SSH.dao;

import SSH.entity.Film;
import SSH.entity.News;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = false)
public class NewsDao {
    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public List<News> getAllNews() {
        return (List<News>) hibernateTemplate.find("from News");
    }

    public void addNews(News news) {
        hibernateTemplate.save(news);
    }

    public News getNewsById(Long id) {
        return hibernateTemplate.get(News.class, id);
    }

    public void deleteNews(Long id) {
        hibernateTemplate.delete(getNewsById(id));
    }

    public void saveNews(News news) {
        hibernateTemplate.saveOrUpdate(news);
    }

    public List<News> getNewsByKey(String key) {
        String hql = "from News where title like :key or author like :key or  description like :key";
        return (List<News>) hibernateTemplate.findByNamedParam(hql,"key","%" + key + "%");
    }
}
