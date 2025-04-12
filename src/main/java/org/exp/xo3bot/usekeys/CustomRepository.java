package org.exp.xo3bot.usekeys;

import java.util.List;

public interface CustomRepository<T, ID> {

    T findById(ID id);

    void save(T entity);

    List<T> findAll();


}