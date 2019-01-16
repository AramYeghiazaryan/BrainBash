package am.aca.quiz_software.service.dto;

import am.aca.quiz_software.entity.QuestionEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public interface QuestionService {
    //create
    boolean addCategory(QuestionEntity question) throws SQLException;

    //read
    List<QuestionEntity> getAll() throws SQLException;

    //update
    boolean update(QuestionEntity question,Long id) throws SQLException;

    //delete
    QuestionEntity remove(QuestionEntity question) throws SQLException;

    QuestionEntity getById(Long id) throws SQLException;

    QuestionEntity removeByid(Long id) throws SQLException;

}

