package com.dsaportal.repository;

import com.dsaportal.entity.Submission;
import com.dsaportal.entity.Submission.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByUserId(Long userId);
    List<Submission> findByProblemId(Long problemId);
    List<Submission> findByUserIdAndProblemId(Long userId, Long problemId);
    List<Submission> findByUserIdAndStatus(Long userId, Status status);
    
    @Query("SELECT s FROM Submission s WHERE s.user.id = :userId ORDER BY s.submittedAt DESC")
    List<Submission> findByUserIdOrderBySubmittedAtDesc(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(s) FROM Submission s WHERE s.user.id = :userId AND s.status = 'ACCEPTED'")
    Long countAcceptedSubmissionsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(s) FROM Submission s WHERE s.user.id = :userId")
    Long countTotalSubmissionsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT AVG(s.accuracy) FROM Submission s WHERE s.user.id = :userId AND s.status = 'ACCEPTED'")
    Double getAverageAccuracyByUserId(@Param("userId") Long userId);
    
    @Query("SELECT s.problem.topic, AVG(s.accuracy) FROM Submission s " +
           "WHERE s.user.id = :userId AND s.status = 'ACCEPTED' " +
           "GROUP BY s.problem.topic")
    List<Object[]> getAccuracyByTopicForUser(@Param("userId") Long userId);
}
