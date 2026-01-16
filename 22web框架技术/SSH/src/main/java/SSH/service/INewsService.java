package SSH.service;

import SSH.entity.News;

import java.util.List;

public interface INewsService {
    List<News> getAllNews();

    void addNews(News news);

    News getNewsById(Long id);

    void deleteNews(Long id);

    void saveNews(News news);

    List<News> getNewsByKey(String title);
}
