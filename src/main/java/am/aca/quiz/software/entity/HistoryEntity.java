package am.aca.quiz.software.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@Table(name = "history")
public class HistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", nullable = false,columnDefinition = "timestamp default CURRENT_DATE")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Min(value = 0, message = "Invalid Score Value")
    @Column(name = "score")
    private double score;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity userEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "test_id", insertable = false, updatable = false)
    private TestEntity testEntity;

    public HistoryEntity() {
    }

    public HistoryEntity(LocalDateTime startTime, Status status,@Min(value = 0, message = "Invalid Score Value") double score, UserEntity userEntity, TestEntity testEntity) {
        this.startTime = startTime;
        this.status = status;
        this.score = score;
        this.userEntity = userEntity;
        this.testEntity = testEntity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }


    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public TestEntity getTestEntity() {
        return testEntity;
    }

    public void setTestEntity(TestEntity testEntity) {
        this.testEntity = testEntity;
    }

    @Override
    public String toString() {
        return "HistoryEntity{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status='" + status + '\'' +
                ", score=" + score +
                ", userEntity=" + userEntity +
                ", testEntity=" + testEntity +
                '}';
    }

    public enum Status {
        INPROGRESS("in progress"),
        UPCOMING("upcoming"),
        COMPLETED("completed");

        private final String stringValue;


        Status(String stringValue) {
            this.stringValue = stringValue;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }
}