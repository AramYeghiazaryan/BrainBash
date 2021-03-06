package am.aca.quiz.software.service.interfaces;


import am.aca.quiz.software.entity.HistoryEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public interface HistoryService {
    //create
    void addHistory(HistoryEntity historyEntity) throws SQLException;

    //read
    List<HistoryEntity> getAll() throws SQLException;

    //update
    void update(HistoryEntity history, Long id) throws SQLException;

    void removeById(Long id) throws SQLException;

    HistoryEntity getById(Long id) throws SQLException;
}