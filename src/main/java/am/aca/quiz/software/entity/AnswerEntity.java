package am.aca.quiz.software.entity;

import javax.persistence.*;

@Entity
@Table(name = "answer", indexes = {@Index(name = "IDX_questionId", columnList = "question_id")})
public class AnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "is_correct", nullable = false)
    private boolean is_correct;

    @Column(name = "answer", nullable = false, columnDefinition = "text")
    private String answer;

    @ManyToOne
    @JoinColumn(name = "question_id", updatable = false)
    private QuestionEntity questionEntity;

    public AnswerEntity() {
    }

    public AnswerEntity(String answer_text, String description, boolean is_correct, QuestionEntity questionEntity) {
        this.description = description;
        this.is_correct = is_correct;
        this.answer = answer_text;
        this.questionEntity = questionEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;

    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIs_correct() {
        return is_correct;
    }

    public void setIs_correct(boolean is_correct) {
        this.is_correct = is_correct;
    }

    public String getAnswer_text() {
        return answer;
    }

    public void setAnswer_text(String answer_text) {
        this.answer = answer_text;
    }

    public QuestionEntity getQuestionEntity() {
        return questionEntity;
    }

    public void setQuestionEntity(QuestionEntity questionEntity) {
        this.questionEntity = questionEntity;
    }

    @Override
    public String toString() {
        return "AnswerEntity{" +
            "id=" + id +
            ", description='" + description + '\'' +
            ", is_correct=" + is_correct +
            ", answer_text='" + answer + '\'' +
            '}';
    }
}
