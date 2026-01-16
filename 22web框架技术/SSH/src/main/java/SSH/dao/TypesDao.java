package SSH.dao;

import SSH.entity.Type;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Transactional(readOnly = false)
public class TypesDao {

    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }


    public List<Type> getTypes() {
        //查询出所有
        return (List<Type>) hibernateTemplate.find("FROM Type");
    }

    public void saveType(Type type) {
        hibernateTemplate.saveOrUpdate(type);
    }
    public void addType(Type type) {
        hibernateTemplate.save(type);
    }

    public void deleteTypeById(Long id) {
        hibernateTemplate.delete(Objects.requireNonNull(hibernateTemplate.get(Type.class, id)));
    }

    public Type getTypeById(Long id) {
        return hibernateTemplate.get(Type.class, id);
    }
}
