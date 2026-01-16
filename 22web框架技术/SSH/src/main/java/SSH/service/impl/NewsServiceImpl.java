package SSH.service.impl;

import SSH.dao.NewsDao;
import SSH.entity.News;
import SSH.service.INewsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class NewsServiceImpl implements INewsService {
    private NewsDao newsDao;

    public void setNewsDao(NewsDao newsDao) {
        this.newsDao = newsDao;
    }

    @Override
    public List<News> getAllNews() {
        return newsDao.getAllNews();
    }

    @Override
    public void addNews(News news) {
        newsDao.addNews(news);
    }

    @Override
    public News getNewsById(Long id) {
        return newsDao.getNewsById(id);
    }

    @Override
    public void deleteNews(Long id) {
        newsDao.deleteNews(id);
    }

    public void saveNews(News news) {
        newsDao.saveNews(news);
    }

    @Override
    public List<News> getNewsByKey(String title) {
        return newsDao.getNewsByKey(title);
    }
}
