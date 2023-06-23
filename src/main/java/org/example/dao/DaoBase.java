package org.example.dao;

import java.util.List;

public interface DaoBase<Entity, Key> {
    Entity select(Key key);
    List<Entity> selectAll();
    boolean insert(Entity entity);
    boolean delete(Key key);
    boolean update(Entity entity);
}
