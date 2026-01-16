package SSH.service.impl;

import SSH.dao.TypesDao;
import SSH.entity.Type;
import SSH.service.ITypesService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public class TypesServiceImpl implements ITypesService {

    private TypesDao typesDao;
    public void setTypesDao(TypesDao typesDao) {
        this.typesDao = typesDao;
    }

    @Override
    public List<Type> getTypes() {
        return typesDao.getTypes();
    }

    @Override
    public void saveType(Type type) {
        typesDao.saveType(type);
    }

    @Override
    public void deleteTypeById(Long id) {
        typesDao.deleteTypeById(id);
    }

    @Override
    public void addType(Type type) {
        //类型不存在于数据库中

        List<Type> types = getTypes();
        for (Type type1 : types) {
            if (type1.getType().equals(type.getType())){
                return;
            }
        }
        typesDao.addType(type);
    }

    @Override
    public Type getTypeById(Long id) {
        return typesDao.getTypeById(id);
    }
}
