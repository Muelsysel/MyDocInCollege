package SSH.action;

import SSH.entity.News;
import SSH.service.impl.NewsServiceImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.util.List;

public class NewsAction extends ActionSupport implements ModelDriven<News> {
    News news = new News();
    @Override
    public News getModel() {
        return news;
    }
    private NewsServiceImpl newsServiceImpl;

    public void setNewsServiceImpl(NewsServiceImpl newsServiceImpl) {
        this.newsServiceImpl = newsServiceImpl;
    }


    public String getAllNews() {
        List<News> News = newsServiceImpl.getAllNews();
        HttpSession session = ServletActionContext.getRequest().getSession();
        session.setAttribute("News", News);
        return SUCCESS;
    }
    public String addNews() {
        try{
            newsServiceImpl.addNews(news);
            //刷新session
            refresh();
            HttpSession session = ServletActionContext.getRequest().getSession();
            session.setAttribute("error", "添加成功！");
            return SUCCESS;
        }catch (Exception e){
            HttpSession session = ServletActionContext.getRequest().getSession();
            session.setAttribute("error", "添加失败！");
            return INPUT;
        }
    }
    public String getNewsById(){
        HttpSession session = ServletActionContext.getRequest().getSession();
        news = newsServiceImpl.getNewsById(news.getId());
        if (news != null){
            session.setAttribute("oneNews", news);
            return SUCCESS;
        }else{
            session.setAttribute("error", "该新闻不存在！");
            return INPUT;
        }

    }
    public String getNewsByKey(){
        HttpSession session = ServletActionContext.getRequest().getSession();
        List<News> News = newsServiceImpl.getNewsByKey(news.getTitle());
        if (News != null){
            session.setAttribute("oneNews", News);
            return SUCCESS;
        }else{
            session.setAttribute("error", "该新闻不存在！");
            return INPUT;
        }
    }
    public String deleteNews(){
        newsServiceImpl.deleteNews(news.getId());
        //刷新session
        refresh();
        return SUCCESS;
    }
    public String saveNews(){
        HttpSession session = ServletActionContext.getRequest().getSession();
        try{
            newsServiceImpl.saveNews(news);
            //刷新session
            refresh();
            //session.setAttribute("error", "保存成功！");
            return SUCCESS;
        }catch (Exception e){
            session.setAttribute("error", "保存失败！");
            return INPUT;
        }
    }


    private void refresh(){
        HttpSession session = ServletActionContext.getRequest().getSession();
        session.removeAttribute("oneNews");
        //session.removeAttribute("error");
        //刷新session
        getAllNews();
    }

}
