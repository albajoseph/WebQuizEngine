package engine;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@RestController
public class QuizController {

    private final QuizRepository quizRepository;
    private final QuizCompletionRepository quizCompletionRepository;

    @Autowired
    public QuizController(QuizRepository quizRepository, QuizCompletionRepository quizCompletionRepository) {
        this.quizRepository = quizRepository;
        this.quizCompletionRepository = quizCompletionRepository;
    }

    // =========================================
    // STAGE 1 ENDPOINTS (Backward Compatibility)
    // =========================================

    @GetMapping("/api/quiz")
    public Map<String, Object> getStage1Quiz() {
        return Map.of(
                "title", "The Java Logo",
                "text", "What is depicted on the Java logo?",
                "options", List.of("Robot", "Tea leaf", "Cup of coffee", "Bug")
        );
    }

    @PostMapping("/api/quiz")
    public QuizResponse solveStage1Quiz(@RequestParam("answer") int answer) {
        if (answer == 2) {
            return new QuizResponse(true, "Congratulations, you're right!");
        } else {
            return new QuizResponse(false, "Wrong answer! Please, try again.");
        }
    }

    // =========================================
    // STAGE 2-6 ENDPOINTS
    // =========================================

    @PostMapping("/api/quizzes")
    public Quiz createQuiz(@Valid @RequestBody Quiz quiz, @AuthenticationPrincipal UserDetails userDetails) {
        quiz.setAuthorEmail(userDetails.getUsername());
        return quizRepository.save(quiz);
    }

    @GetMapping("/api/quizzes/{id}")
    public Quiz getQuiz(@PathVariable int id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found"));
    }

    // UPDATED: Now supports Pagination
    @GetMapping("/api/quizzes")
    public Page<Quiz> getAllQuizzes(@RequestParam(defaultValue = "0") int page) {
        return quizRepository.findAll(PageRequest.of(page, 10));
    }

    // UPDATED: Now saves successful completions
    @PostMapping("/api/quizzes/{id}/solve")
    public QuizResponse solveQuiz(@PathVariable int id, @RequestBody Answer answer, @AuthenticationPrincipal UserDetails userDetails) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found"));

        Set<Integer> correctAnswers = new HashSet<>(quiz.getAnswer());
        Set<Integer> userAnswers = new HashSet<>(answer.getAnswer());

        if (correctAnswers.equals(userAnswers)) {
            // Save completion to database upon success
            QuizCompletion completion = new QuizCompletion();
            completion.setQuizId(id);
            completion.setAuthorEmail(userDetails.getUsername());
            completion.setCompletedAt(LocalDateTime.now());
            quizCompletionRepository.save(completion);

            return new QuizResponse(true, "Congratulations, you're right!");
        } else {
            return new QuizResponse(false, "Wrong answer! Please, try again.");
        }
    }

    // NEW: Stage 6 Completed Quizzes Endpoint
    @GetMapping("/api/quizzes/completed")
    public Page<QuizCompletion> getCompletedQuizzes(@RequestParam(defaultValue = "0") int page, @AuthenticationPrincipal UserDetails userDetails) {
        return quizCompletionRepository.findByAuthorEmailOrderByCompletedAtDesc(
                userDetails.getUsername(),
                PageRequest.of(page, 10)
        );
    }

    @DeleteMapping("/api/quizzes/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails) {
        Quiz quiz = quizRepository.findById(id).orElse(null);

        if (quiz == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!quiz.getAuthorEmail().equals(userDetails.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        quizRepository.delete(quiz);
        return ResponseEntity.noContent().build();
    }
}