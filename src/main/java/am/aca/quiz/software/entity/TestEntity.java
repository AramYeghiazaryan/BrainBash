package am.aca.quiz.software.entity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "test")
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "test_name", nullable = false)
    private String test;

    @Column(name = "duration", nullable = false)
    private long duration;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "question_test",
        joinColumns = {@JoinColumn(name = "test_id")},
        inverseJoinColumns = {@JoinColumn(name = "question_id")})
    private List<QuestionEntity> questionEntities = new ArrayList<>();

    @OneToMany(mappedBy = "testEntity", cascade = CascadeType.ALL)
    private Set<HistoryEntity> historyEntities = new HashSet<>();


    public TestEntity(String test_name, String description, long duration, List<QuestionEntity> questionEntities) {
        this.test = test_name;
        this.duration = duration;
        this.description = description;
        this.questionEntities = questionEntities;
    }

    public TestEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTest_name() {
        return this.test;
    }

    public void setTest_name(String test_name) {
        this.test = test_name;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<QuestionEntity> getQuestionEntities() {
        return questionEntities;
    }

    public void setQuestionEntities(List<QuestionEntity> questionEntities) {
        this.questionEntities = questionEntities;
    }


    public Set<HistoryEntity> getHistoryEntities() {
        return historyEntities;
    }

    public void setHistoryEntities(Set<HistoryEntity> historyEntities) {
        this.historyEntities = historyEntities;
    }

    @Override
    public String toString() {
        return "TestEntity{" +
            "id=" + id +
            ", test_name='" + test + '\'' +
            ", duration=" + duration +
            ", description='" + description + '\'' +
            '}';
    }
}
