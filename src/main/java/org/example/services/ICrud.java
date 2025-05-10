package org.example.services;
import java.sql.SQLException;
import java.util.List;
public interface ICrud <T> {
    void ajouter (T t) throws SQLException;
    void supprimer (int id) throws SQLException;
    void modifier (T t) throws SQLException;
    List<T> getAll() throws SQLException;

}
