package am.aca.quiz.software.controller;

import am.aca.quiz.software.entity.HistoryEntity;
import am.aca.quiz.software.entity.QuestionEntity;
import am.aca.quiz.software.entity.TestEntity;
import am.aca.quiz.software.entity.enums.Status;
import am.aca.quiz.software.service.MailService;
import am.aca.quiz.software.service.dto.*;
import am.aca.quiz.software.service.implementations.*;
import am.aca.quiz.software.service.implementations.score.ScorePair;
import am.aca.quiz.software.service.mapper.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.Principal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

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
    private final QuestionController questionController;
    private final HistoryServiceImp historyServiceImp;
    private final MailService mailService;
    private final AnswerServiceImp answerServiceImp;
    private final AnswerMapper answerMapper;
    private final ScoreServiceImp scoreServiceImp;
    private ScorePair<Double, Double> score;
    private List<SubmitQuestionDto> userSubmitQuestionDtos;
    private long endTime;
    private int reloadCount = 0;
    private Long testId;

///////////////////
    public TestController(TestServiceImp testServiceImp, TestMapper testMapper, TopicServiceImp topicServiceImp, TopicMapper topicMapper, QuestionServiceImp questionServiceImp, QuestionMapper questionMapper, UserMapper user, UserMapper userMapper, UserServiceImp userServiceImp, QuestionController questionController, HistoryServiceImp historyServiceImp, MailService mailService, AnswerServiceImp answerServiceImp, AnswerMapper answerMapper, ScoreServiceImp scoreServiceImp) {
        this.testServiceImp = testServiceImp;
        this.testMapper = testMapper;
        this.topicServiceImp = topicServiceImp;
        this.topicMapper = topicMapper;
        this.questionServiceImp = questionServiceImp;
        this.questionMapper = questionMapper;
        this.userMapper = userMapper;
        this.userServiceImp = userServiceImp;
        this.questionController = questionController;
        this.historyServiceImp = historyServiceImp;
        this.mailService = mailService;
        this.answerServiceImp = answerServiceImp;
        this.answerMapper = answerMapper;
        this.scoreServiceImp = scoreServiceImp;
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/add")
    public ModelAndView addTest() {

        return new ModelAndView("test");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
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

    @PreAuthorize("hasAuthority('ADMIN')")
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/delete/{id}")
    public ModelAndView deleteTest(@PathVariable("id") Long id) {

        try {
            testServiceImp.removeById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return testList();
    }

    @PostMapping("/timer/{id}")
    public ResponseEntity<TimerDto> timer(@PathVariable("id") Long id) {

        TimerDto timerDto = new TimerDto();
        long time = System.currentTimeMillis();

        this.testId = id;

        timerDto.setCurrentTime(time);
        timerDto.setEndTime(endTime);


        return ResponseEntity.ok(timerDto);
    }


    @GetMapping("/menu")
    public ModelAndView loadMenu() {
        ModelAndView modelAndView = new ModelAndView("testMenu");

        return modelAndView;
    }

    @GetMapping("/menu/{id}")
    public ModelAndView loadTestById(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("testByTopic");

        Set<BigInteger> testId = testServiceImp.findTestIdByTopicId(id);
        Set<TestDto> testDtos = new HashSet<>();
        testId.stream()
            .forEach(i -> {
                try {
                    testDtos.add(testMapper.mapEntityToDto(testServiceImp.getById(i.longValue())));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        modelAndView.addObject("testList", testDtos);
        modelAndView.addObject("topicId", id);

        return modelAndView;
    }


    @GetMapping("/transfer/{topicId}/{testId}")
    public ModelAndView testTransferPage(@PathVariable("topicId") Long topicId, @PathVariable("testId") Long testId) {
        ModelAndView modelAndView = new ModelAndView("transferPage");
        try {
            TestDto testDto = testMapper.mapEntityToDto(testServiceImp.getById(testId));
            modelAndView.addObject("test", testDto);
            modelAndView.addObject("id", topicId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return modelAndView;

    }

    @GetMapping("/solve/{id}")
    public ModelAndView loadTest(@PathVariable("id") Long id, Principal principal) throws SQLException {

        String sessionId = UUID.randomUUID().toString();
        LocalDateTime currentTime = LocalDateTime.now();
        TestEntity testEntity = testServiceImp.getById(id);

        HistoryEntity historyEntity = historyServiceImp.findHistoryByUserIdAndTetId(
            userServiceImp.findByEmail(principal.getName()).getId(), testEntity.getId(), "UPCOMING");

        if (historyEntity != null) {
            if (currentTime.isBefore(historyEntity.getStartTime())) {
                ModelAndView modelAndView = new ModelAndView("userBanPage");

                modelAndView.addObject("id", id);
                modelAndView.addObject("user_id", userServiceImp.findByEmail(principal.getName()).getId());

                return modelAndView;
            }
        }
        if (historyServiceImp.findHistoryBySUerIdAndStatus(
            userServiceImp.findByEmail(principal.getName()).getId(), "INPROGRESS") == null) {

            HistoryEntity upcoming = historyServiceImp.findHistoryByUserIdAndTetId(userServiceImp.findByEmail(principal.getName()).getId(), id, "UPCOMING");

            if (upcoming == null) {

                upcoming = new HistoryEntity(LocalDateTime.now(), Status.INPROGRESS, 0, userServiceImp.findByEmail(principal.getName()), testServiceImp.getById(id));
                upcoming.setSessionId(sessionId);
                historyServiceImp.addHistory(upcoming);

            } else {
                upcoming.setSessionId(sessionId);
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime duration = upcoming.getStartTime().plusMinutes(testServiceImp.getById(id).getDuration());

                if (now.isAfter(upcoming.getStartTime()) && now.isBefore(duration)) {

                    upcoming.setStatus(Status.INPROGRESS);
                    historyServiceImp.addHistory(upcoming);
                }
            }

        } else {
            id = historyServiceImp.findHistoryBySUerIdAndStatus(
                userServiceImp.findByEmail(principal.getName()).getId(), "INPROGRESS").getTestEntity().getId();
        }

        if (reloadCount == 0) {
            endTime = System.currentTimeMillis() + testServiceImp.getById(id).getDuration() * 1000 * 60;

            reloadCount++;
        }
        ModelAndView modelAndView = new ModelAndView("testSolution");
        modelAndView.addObject("testId", id);

        return modelAndView;


    }

    @PostMapping("/process")
    public ModelAndView checkTest(@RequestBody List<SubmitQuestionDto> submitQuestionDtos, Principal principal) {


        questionController.getTestID().clear();
        questionController.getQuestionEntityList().clear();


        score = testServiceImp.checkTest(submitQuestionDtos);
        try {
            Long userId = userMapper.mapEntityToDto(userServiceImp.findByEmail(principal.getName())).getId();
            scoreServiceImp.avgScore(testId, score.getKey(), userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        userSubmitQuestionDtos = submitQuestionDtos;

        return new ModelAndView("testSolution");
    }


    @GetMapping(value = "/scorepage")
    public ModelAndView scorePage() {

        reloadCount = 0;
        ModelAndView modelAndView = new ModelAndView("testScore");
        TestScoreDto testScoreDto = new TestScoreDto();

        List<QuestionDto> questionDtos = new ArrayList<>();
        Map<Long, List<AnswerDto>> answersByQuestionId = new HashMap<>();

        userSubmitQuestionDtos.forEach(i -> {
            try {
                Long id = questionServiceImp.getById(i.getQuestionId()).getId();

                questionDtos.add(questionMapper.mapEntityToDto(questionServiceImp.getById(i.getQuestionId())));

                answersByQuestionId.put(id, answerMapper
                    .mapEntitiesToDto(answerServiceImp.getAnswerEntitiesByQuestionId(id)));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        double testScore = new BigDecimal(Double.toString(score.getValue())).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        testScoreDto.setTestScore(testScore);
        double userScore = new BigDecimal(Double.toString(score.getKey())).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        testScoreDto.setUserScore(userScore);

        modelAndView.addObject("questionList", questionDtos);
        modelAndView.addObject("testScore", testScoreDto);


        return modelAndView;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/organize")
    public ModelAndView selectTest() {
        ModelAndView modelAndView = new ModelAndView("selectTest");
        try {
            List<TestDto> testDtos = testMapper.mapEntitiesToDto(testServiceImp.getAll());
            modelAndView.addObject("testList", testDtos);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return modelAndView;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/organize")
    public ModelAndView selectTest(@RequestParam("search") String search) {
        ModelAndView modelAndView = new ModelAndView("selectTest");
        if (!search.isEmpty()) {

        }

        return modelAndView;
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/selectUser")
    public ModelAndView saveHistory(@RequestBody TestUsersDto testUsersDto) {

        ModelAndView modelAndView = new ModelAndView("organizeTest");

        List<Long> receivedIds = testUsersDto.getUsersId();
        TestDto testDto = null;

        try {
            testDto = testMapper.mapEntityToDto(testServiceImp.getById(testUsersDto.getTestId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }


        List<UserDto> finalUserDtos = new ArrayList<>();
        receivedIds
            .forEach(
                i -> {
                    try {
                        finalUserDtos.add(userMapper.mapEntityToDto(userServiceImp.getById(i)));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

        finalUserDtos
            .forEach(i -> {
                HistoryEntity historyEntity = historyServiceImp.findHistoryByUserIdAndTetId(i.getId(), testUsersDto.getTestId(), "UPCOMING");
                if (historyEntity == null) {
                    HistoryEntity entity = new HistoryEntity();
                    try {
                        entity.setUserEntity(userServiceImp.getById(i.getId()));

                        entity.setTestEntity(testServiceImp.getById(testUsersDto.getTestId()));
                        entity.setStatus(Status.UPCOMING);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    entity.setStartTime(LocalDateTime.parse(testUsersDto.getStartTime()));
                    entity.setScore(0);
                    historyServiceImp.addHistory(entity);
                } else {
                    historyEntity.setStartTime(LocalDateTime.parse(testUsersDto.getStartTime()));
                    historyServiceImp.addHistory(historyEntity);
                }

            });

        return modelAndView;

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/selectUser/{id}")
    public ModelAndView selectUsers(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("organizeTest");


        List<UserDto> userDtos = null;

        try {
            userDtos = userMapper.mapEntitiesToDto(userServiceImp.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }


        modelAndView.addObject("userList", userDtos);
        modelAndView.addObject("id", id);


        return modelAndView;

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/selectUser/{id}")
    public ModelAndView filterUser(@PathVariable("id") Long id, @RequestParam Map<String, String> formDate) {
        ModelAndView modelAndView = new ModelAndView("organizeTest");
        String user = formDate.get("search");

        List<UserDto> userDtos = new ArrayList<>();

        try {
            TestDto testDto = testMapper.mapEntityToDto(testServiceImp.getById(id));

            if (!userServiceImp.findByNameLike(user).isEmpty()) {

                userDtos = userMapper.mapEntitiesToDto(userServiceImp.findByNameLike(user));

            } else if (!userServiceImp.findBySurnameLike(user).isEmpty()) {

                userDtos = userMapper.mapEntitiesToDto(userServiceImp.findBySurnameLike(user));

            } else if (!userServiceImp.findByNicknameLike(user).isEmpty()) {

                userDtos = userMapper.mapEntitiesToDto(userServiceImp.findByNicknameLike(user));

            }
            modelAndView.addObject("userList", userDtos);
            modelAndView.addObject("test", testDto);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return modelAndView;
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/notify", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView notify(@RequestBody TestUsersDto testUsersDto) {


        List<Long> userIds = testUsersDto.getUsersId();

        List<Long> topicId = topicServiceImp.findTopicIdByTest(testUsersDto.getTestId());


        String subject = "New Test Notification";


        try {
            String text = "Your Test Will Start on " + testUsersDto.getStartTime() + ". And Will Last "
                + testServiceImp.getById(testUsersDto.getTestId()).getDuration() + " minutes. Good luck.   " +
                "Please, visit the following link: https://brainbash.herokuapp.com/test/transfer/" + topicId.get(0) + "/" + testUsersDto.getTestId();

            new Thread(() -> {
                userIds.forEach(
                    i -> {
                    /*
                     Send Mail Faster
                     */

                        try {
                            mailService.sendText(userServiceImp
                                .getById(i).getEmail(), subject, text);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });

            }).start();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ModelAndView("redirect:/test/organize");
    }

    public Long getTestId() {
        return this.testId;
    }

    public ScorePair<Double, Double> getScore() {
        return this.score;
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/random")
    public ModelAndView random() {
        return new ModelAndView("randomTestGenerator");
    }

    @PostMapping(value = "/random/generate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void generateRandomTest(@RequestBody RandomDto randomDto) {

        List<QuestionEntity> testQuestions = testServiceImp.randomQuestionGenerator(randomDto);

        testQuestions.forEach(i -> System.out.println(i.getQuestion()));

        try {
            testServiceImp.addTest(randomDto.getName(), randomDto.getDescription(), randomDto.getDuration(), testQuestions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("update/{id}")
    public ModelAndView updateTest(@PathVariable Long id) {
        return new ModelAndView("testUpdate");
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("update/{id}")
    public ModelAndView updateTest(@PathVariable Long id, @RequestBody TestDto test) {

        try {
            String test_name = test.getTest_name();
            String description = test.getDescription();
            long duration = test.getDuration();

            List<Long> questionIds = test.getQuestionIds();
            List<QuestionEntity> questions = new ArrayList<>();
            for (Long questionId : questionIds) {
                QuestionEntity questionEntity = questionServiceImp.getById(questionId);
                questions.add(questionEntity);
            }
            TestEntity testEntity = new TestEntity(test_name, description, duration, questions);
            testEntity.setId(id);
            testServiceImp.update(testEntity);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        ModelAndView modelAndView = new ModelAndView("redirect:/testList");
        try {
            modelAndView.addObject("testList", testMapper.mapEntitiesToDto(testServiceImp.getAll()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return modelAndView;

    }

}
