package org.acrobatt.project.dao.mysql;

import java.io.Serializable;
import java.util.List;

public interface IGenericDAO<T, D extends Serializable> {

    List<T> getAll();

    T getById   (D entityId);
    T insert    (T entityType);
    T persist   (T entityType);
    void update (T entityType);
    void merge  (T entityType);
    void delete (T entityType);
}
