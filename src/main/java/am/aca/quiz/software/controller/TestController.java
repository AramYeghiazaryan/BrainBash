package am.aca.quiz.software.controller;

import am.aca.quiz.software.entity.QuestionEntity;
import am.aca.quiz.software.entity.UserEntity;
import am.aca.quiz.software.service.dto.TestDto;
import am.aca.quiz.software.service.dto.UserDto;
import am.aca.quiz.software.service.implementations.QuestionServiceImp;
import am.aca.quiz.software.service.implementations.TestServiceImp;
import am.aca.quiz.software.service.implementations.TopicServiceImp;
import am.aca.quiz.software.service.implementations.UserServiceImp;
import am.aca.quiz.software.service.mapper.QuestionMapper;
import am.aca.quiz.software.service.mapper.TestMapper;
import am.aca.quiz.software.service.mapper.TopicMapper;
import am.aca.quiz.software.service.mapper.UserMapper;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    private final TestServiceImp testServiceImp;
    private final TestMapper testMapper;
    private final TopicServiceImp topicServiceImp;
    private final TopicMapper topicMapper;
    private final QuestionServiceImp questionServiceImp;
    private final QuestionMapper questionMapper;
    private final UserMapper userMapper;
    private final UserServiceImp userServiceImp;


    public TestController(TestServiceImp testServiceImp, TestMapper testMapper, TopicServiceImp topicServiceImp, TopicMapper topicMapper, QuestionServiceImp questionServiceImp, QuestionMapper questionMapper, UserMapper user, UserMapper userMapper, UserServiceImp userServiceImp) {
        this.testServiceImp = testServiceImp;
        this.testMapper = testMapper;
        this.topicServiceImp = topicServiceImp;
        this.topicMapper = topicMapper;
        this.questionServiceImp = questionServiceImp;
        this.questionMapper = questionMapper;
        this.userMapper = userMapper;

        this.userServiceImp = userServiceImp;
    }


    @GetMapping("/{id}")
    public ResponseEntity<TestDto> getById(@PathVariable("id") Long id) {
        try {
            if (testServiceImp.getById(id) != null) {
                return ResponseEntity.ok(testMapper.mapEntityToDto(testServiceImp.getById(id)));
            }
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @GetMapping
    public ResponseEntity<List<TestDto>> getAll() {
        try {
            if (testServiceImp.getAll().isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(testMapper.mapEntitiesToDto(testServiceImp.getAll()));
        } catch (SQLException e) {
        }
        return null;
    }


    @GetMapping
    @RequestMapping("/add")
    public ModelAndView addTest() {

        return new ModelAndView("test");
    }

    @PostMapping("/add")
    @ResponseBody
    public ModelAndView postTest(@RequestBody TestDto test) {

        String test_name = test.getTest_name();
        String description = test.getDescription();
        long duration = test.getDuration();

        List<Long> questionIds = test.getQuestionIds();
        List<QuestionEntity> questions = new ArrayList<>();

        try {
            for (Long questionId : questionIds) {
                QuestionEntity questionEntity = questionServiceImp.getById(questionId);
                questions.add(questionEntity);
            }
            testServiceImp.addTest(test_name, description, duration, questions);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ModelAndView("test");
    }

    @GetMapping("/list")
    public ModelAndView testList() {
        ModelAndView modelAndView = new ModelAndView("testList");
        try {
            modelAndView.addObject("testList", testMapper.mapEntitiesToDto(testServiceImp.getAll()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteTest(@PathVariable("id") Long id) {

        try {
            testServiceImp.removeById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return testList();
    }

    @GetMapping("/menu")
    public ModelAndView loadMenu(){
        ModelAndView modelAndView=new ModelAndView("testMenu");

        return modelAndView;
    }
    @GetMapping("/menu/{id}")
    public ModelAndView loadTestById(@PathVariable("id") Long id){
        ModelAndView modelAndView=new ModelAndView("testByTopic");

        List<Long> testId=testServiceImp.findTestIdByTopicId(id);
        System.out.println(testId);

        return modelAndView;
    }

}
