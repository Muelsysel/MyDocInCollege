package SSH.service;

import SSH.entity.Type;

import java.util.List;

public interface ITypesService {
    List<Type> getTypes();

    void saveType(Type type);

    void deleteTypeById(Long id);

    void addType(Type type);

    Type getTypeById(Long id);
}
