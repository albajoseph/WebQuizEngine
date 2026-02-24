package engine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuizCompletionRepository extends CrudRepository<QuizCompletion, Integer>, PagingAndSortingRepository<QuizCompletion, Integer> {

    // Spring automatically implements this query based on the method name!
    Page<QuizCompletion> findByAuthorEmailOrderByCompletedAtDesc(String authorEmail, Pageable pageable);
}