package SSH.action;

import SSH.entity.Film;
import SSH.entity.News;
import SSH.entity.Type;
import SSH.service.IFilmService;
import SSH.service.INewsService;
import SSH.service.ITypesService;
import SSH.service.IUserService;
import SSH.entity.User;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class UserAction extends ActionSupport implements ModelDriven<User> {

    User user = new User();
    @Override
    public User getModel() {
        return user;
    }

    private ITypesService typesServiceImpl;
    public void setTypesServiceImpl(ITypesService typesServiceImpl) {
        this.typesServiceImpl = typesServiceImpl;
    }
    private INewsService newsServiceImpl;
    public void setNewsServiceImpl(INewsService newsServiceImpl) {
        this.newsServiceImpl = newsServiceImpl;
    }
    private IFilmService filmServiceImpl;
    public void setFilmServiceImpl(IFilmService filmServiceImpl) {
        this.filmServiceImpl = filmServiceImpl;
    }
    private IUserService userServiceImpl;
    public void setUserServiceImpl(IUserService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }


    public String login(){
        user = userServiceImpl.login(user.getUsername(), user.getPassword());
        if (user != null){
            //初始化session
            init(user);
            return SUCCESS;
        }else {
            HttpSession session = ServletActionContext.getRequest().getSession();
            session.setAttribute("error", "用户名或密码错误");
            return INPUT;
        }
    }
    public String register(){
        HttpSession session = ServletActionContext.getRequest().getSession();
        if (userServiceImpl.getUserByUsername(user.getUsername()) != null){
            session.setAttribute("error", "用户已存在");
            return INPUT;
        }else{
            if (userServiceImpl.register(user)){
                return SUCCESS;
            }
            session.setAttribute("error", "注册失败！请检查");
            return INPUT;
        }
    }
    public String getUserInfo(){
        HttpSession session = ServletActionContext.getRequest().getSession();
        User user = userServiceImpl.getUserById((Long) session.getAttribute("UserId"));
        session.setAttribute("UserInfo", user);
        return SUCCESS;
    }
    public String saveUserWithoutPassword(){
        HttpSession session = ServletActionContext.getRequest().getSession();
        if (userServiceImpl.saveWithoutPassword(user)){
            refresh();
            session.setAttribute("error", "修改成功！");
            return SUCCESS;
        }
        session.setAttribute("error", "修改失败！");
        return INPUT;
    }

    public String updatePassword(){
        HttpSession session = ServletActionContext.getRequest().getSession();
        User oldUser = userServiceImpl.getUserById((Long) session.getAttribute("UserId"));
        oldUser.setPassword(user.getPassword());
        if (userServiceImpl.updatePassword(oldUser)){
            refresh();
            session.setAttribute("error", "修改成功！");
            return SUCCESS;
        }else {
            session.setAttribute("error", "修改失败！");
            return INPUT;
        }
    }
    //退出登录
    public String logout(){
        HttpSession session = ServletActionContext.getRequest().getSession();
        //移除全部session
        session.invalidate();
        return SUCCESS;
    }

    public String refresh(){
        //刷新session
        HttpSession session = ServletActionContext.getRequest().getSession();
        //session.setAttribute("UserId",user.getId());
        System.out.println("id:"+session.getAttribute("UserId"));
        User user = userServiceImpl.getUserById((Long) session.getAttribute("UserId"));
        session.removeAttribute("UserInfo");
        session.setAttribute("UserInfo", user);
        session.removeAttribute("error");
        return SUCCESS;
    }
    //初始化
    public void init(User user){
        try{
            HttpSession session = ServletActionContext.getRequest().getSession();
            //登陆成功，初始化各个session
            //session存储id
            session.setAttribute("UserId",user.getId());
            System.out.println("id:"+session.getAttribute("UserId"));

            //获取用户信息
            user = userServiceImpl.getUserById(user.getId());
            session.setAttribute("UserInfo",user);
            //获取所有类型
            List<Type> types = typesServiceImpl.getTypes();
            session.setAttribute("Types",types);
            //获取所有新闻
            List<News> news = newsServiceImpl.getAllNews();
            session.setAttribute("News",news);
            //获取所有电影
            List<SSH.entity.Film> films = filmServiceImpl.getAllFilm();
            session.setAttribute("Films",films);


            List<Film> filmsRecommend = new ArrayList<>();
            List<News> newsRecommend = new ArrayList<>();
//            // 随机取5个电影存入session
//            for (int i = 0; i < 5; i++){
//                int id = (int) (Math.random() * films.size());
//                filmsRecommend.add(films.get(id));
//            }
//            session.setAttribute("filmsRecommend", filmsRecommend);

            // 随机取3个新闻存入session
            for (int i = 0; i < 3; i++){
                int id = (int) (Math.random() * news.size());
                newsRecommend.add(news.get(id));
            }
            session.setAttribute("newsRecommend", newsRecommend);

        }
        catch (Exception e){
            HttpSession session = ServletActionContext.getRequest().getSession();
            session.setAttribute("error", "初始化失败！");
        }
    }
}
