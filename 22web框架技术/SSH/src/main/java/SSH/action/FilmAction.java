package SSH.action;

import SSH.entity.Film;
import SSH.service.impl.FilmServiceImpl;
import SSH.utils.Constant;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.util.List;

public class FilmAction extends ActionSupport implements ModelDriven<Film> {
    Film film = new Film();
    @Override
    public Film getModel() {
        return film;
    }
    private FilmServiceImpl filmServiceImpl;
    public void setFilmServiceImpl(FilmServiceImpl filmServiceImpl) {
        this.filmServiceImpl = filmServiceImpl;
    }

    /**
     * @return {@link String }
     */
    public String getAllFilm() {
        List<Film> films = filmServiceImpl.getAllFilm();
        HttpSession session = ServletActionContext.getRequest().getSession();
        session.setAttribute("Films",films);
        //addFieldError("Films",null);
        return SUCCESS;
    }

    /**
     * @return {@link String }
     */
    public String getFilmById() {
        Film oneFilm = filmServiceImpl.getFilmById(film.getId());
        HttpSession session = ServletActionContext.getRequest().getSession();
        session.setAttribute("Film",oneFilm);
        return SUCCESS;
    }

    /**
     * @return {@link String }
     */
    public String getFilmsByDirector(){
        List<Film> films = filmServiceImpl.getFilmsByDirector(film.getDirector());
        HttpSession session = ServletActionContext.getRequest().getSession();
        session.setAttribute("Film",films);
        return SUCCESS;
    }
    public String getFilmsByName(){
        try{
            List<Film> films = filmServiceImpl.getFilmsByName(film.getName());
            HttpSession session = ServletActionContext.getRequest().getSession();
            session.setAttribute("Film",films);
            return SUCCESS;
        }catch (Exception e){
            return ERROR;
        }

    }

    /**
     * @return {@link String }
     */
    public String addFilm() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        if (session.getAttribute("FilePath") == null) {
            session.setAttribute("FilePath", Constant.DEFAULT_IMAGE_URL);
        }
        if (film.getVideo()==null){
            film.setVideo(Constant.DEFAULT_VIDEO_URL);
        }
        String filePath = (String) session.getAttribute("FilePath");
        film.setUrl(filePath);

        filmServiceImpl.save(film);
        //刷新session
        refresh();
        return SUCCESS;
    }
    //getFilmsByType
    public String getFilmsByType(){
        List<Film> films = filmServiceImpl.getFilmsByType(film.getType());
        HttpSession session = ServletActionContext.getRequest().getSession();
        session.setAttribute("Film",films);
        return SUCCESS;
    }
    public String saveFilm(){
        HttpSession session = ServletActionContext.getRequest().getSession();
        if (session.getAttribute("FilePath") != null) {
            String filePath= (String) session.getAttribute("FilePath");
            film.setUrl(filePath);
        }
        if (film.getVideo()==null){
            film.setVideo(Constant.DEFAULT_VIDEO_URL);
        }
        filmServiceImpl.saveByNewFilm(film);
        //刷新session
        refresh();
        return SUCCESS;
    }
    public String deleteFilmById(){
        filmServiceImpl.deleteFilmById(film.getId());
        //刷新session
        refresh();
        return SUCCESS;
    }
    public String getFilmByKey(){
        HttpSession session = ServletActionContext.getRequest().getSession();
        List<Film> films = filmServiceImpl.getFilmByKey(film.getName());
        if (films.size() == 0){
            session.setAttribute("error","没有找到该电影");
            return INPUT;
        }else {
            session.setAttribute("Film",films);
            return SUCCESS;
        }
    }
    public String getFilmByName(){
        HttpSession session = ServletActionContext.getRequest().getSession();
        List<Film> films = filmServiceImpl.getFilmsByName(film.getName());
        if (films.size() == 0){
            session.setAttribute("error","没有找到该电影");
            return INPUT;
        }else {
            session.setAttribute("Film",films);
            return SUCCESS;
        }
    }

    private void refresh(){
        //刷新session
        HttpSession session = ServletActionContext.getRequest().getSession();
        session.removeAttribute("FilePath");
        session.removeAttribute("Film");
        getAllFilm();
    }
}
