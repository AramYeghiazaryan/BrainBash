package am.aca.quiz.software.service.implementations;

import am.aca.quiz.software.entity.CategoryEntity;
import am.aca.quiz.software.repository.CategoryRepository;
import am.aca.quiz.software.service.dto.CategoryDto;
import am.aca.quiz.software.service.intefaces.CategoryService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@Service
public class CategoryServiceImp implements CategoryService {


    private final CategoryRepository categoryRepository;

    public CategoryServiceImp(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public boolean addCategory(String type) throws SQLException {
        CategoryEntity categoryEntity=new CategoryEntity(type);
        categoryRepository.saveAndFlush(categoryEntity);
        return true;
    }


    public List<CategoryEntity> getAll() throws SQLException {
        return categoryRepository.findAll();
    }

    @Override
    public boolean update(CategoryEntity category, Long id) throws SQLException {

        CategoryEntity updated_category = categoryRepository.findById(id).get();
        if (updated_category != null) {
            category.setId(id);
            categoryRepository.saveAndFlush(category);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeById(Long id) throws SQLException {
        CategoryEntity deleted_category = categoryRepository.findById(id).get();
        categoryRepository.delete(deleted_category);
        return true;
    }

    @Override
    public CategoryEntity getById(Long id) throws SQLException {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(id);

        if (!categoryEntity.isPresent()) {
            throw new SQLException("Entity Not Found");
        }
        return categoryEntity.get();
    }
}