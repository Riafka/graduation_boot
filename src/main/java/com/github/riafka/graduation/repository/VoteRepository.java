package com.github.riafka.graduation.repository;

import com.github.riafka.graduation.model.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {
    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId AND v.voteDate = :voteDate")
    Optional<Vote> findByUserIdAndDate(int userId, LocalDate voteDate);

    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId ORDER BY v.voteDate DESC")
    List<Vote> findByUserId(int userId);
}
