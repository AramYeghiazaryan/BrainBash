package am.aca.quiz_software.service.dto;


import am.aca.quiz_software.entity.HistoryEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public interface HistoryService {
    //create
    boolean addCategory(HistoryEntity history) throws SQLException;

    //read
    List<HistoryEntity> getAll() throws SQLException;

    //update
    boolean update(HistoryEntity history) throws SQLException;

    //delete
    HistoryEntity remove(HistoryEntity history) throws SQLException;
}