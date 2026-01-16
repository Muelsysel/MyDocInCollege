package SSH.action;

import SSH.entity.Type;
import SSH.service.impl.TypesServiceImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.util.List;

public class TypesAction extends ActionSupport implements ModelDriven<Type> {
    Type type = new Type();
    @Override//获取实体类
    public Type getModel() {
        return type;
    }
    private TypesServiceImpl typesServiceImpl;
    public void setTypesServiceImpl(TypesServiceImpl typesServiceImpl) {
        this.typesServiceImpl = typesServiceImpl;
    }


    public String getAllTypes() {
        List<Type> types = typesServiceImpl.getTypes();
        HttpSession session = ServletActionContext.getRequest().getSession();
        session.setAttribute("Types", types);
        return SUCCESS;
    }
    public String getTypeById(){
        Type oneType= typesServiceImpl.getTypeById(type.getId());
        if (type!=null){
            HttpSession session = ServletActionContext.getRequest().getSession();
            session.setAttribute("oneType", oneType);
            return SUCCESS;
        }else {
            return ERROR;
        }
    }
    public String addType() {
        try{
            typesServiceImpl.addType(type);
            //刷新
            refresh();
            return SUCCESS;
        } catch (Exception e){
            e.printStackTrace();
            HttpSession session = ServletActionContext.getRequest().getSession();
            session.setAttribute("error", "添加失败");
            return INPUT;
        }

    }
    public String saveType() {
        try{
            typesServiceImpl.saveType(type);
            //刷新
            refresh();
            return SUCCESS;
        } catch (Exception e){
            HttpSession session = ServletActionContext.getRequest().getSession();
            session.setAttribute("error", "修改失败");
            return INPUT;
        }
    }
    public String deleteType() {
        try {
            typesServiceImpl.deleteTypeById(type.getId());
            //刷新
            refresh();
            return SUCCESS;
        }catch ( Exception e){
            HttpSession session = ServletActionContext.getRequest().getSession();
            session.setAttribute("error", "删除失败");
            return INPUT;
        }
    }


    private void refresh(){
        //刷新session
        HttpSession session = ServletActionContext.getRequest().getSession();

        session.removeAttribute("oneType");
        getAllTypes();
    }
}
