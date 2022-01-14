package com.github.riafka.graduation_boot.web.vote;

import com.github.riafka.graduation_boot.repository.VoteRepository;
import com.github.riafka.graduation_boot.to.VoteTo;
import com.github.riafka.graduation_boot.util.VoteUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class VoteController {
    static final String REST_URL = "/api/votes";

    private final VoteRepository voteRepository;

    @GetMapping
    public List<VoteTo> getAll() {
        log.info("get all votes for today");
        return voteRepository.getAllByVoteDate(LocalDate.now())
                .stream()
                .map(VoteUtil::createTo)
                .collect(Collectors.toList());
    }
}
