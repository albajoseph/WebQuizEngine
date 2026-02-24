package engine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class QuizCompletion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore // We don't want to expose the auto-generated primary key
    private int completionId;

    @JsonProperty("id") // The client expects the quiz ID to be named "id"
    private int quizId;

    private LocalDateTime completedAt;

    @JsonIgnore // Hide the email of the user in the response
    private String authorEmail;

    public QuizCompletion() {}

    // Getters and Setters
    public int getCompletionId() { return completionId; }
    public void setCompletionId(int completionId) { this.completionId = completionId; }

    public int getQuizId() { return quizId; }
    public void setQuizId(int quizId) { this.quizId = quizId; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public String getAuthorEmail() { return authorEmail; }
    public void setAuthorEmail(String authorEmail) { this.authorEmail = authorEmail; }
}