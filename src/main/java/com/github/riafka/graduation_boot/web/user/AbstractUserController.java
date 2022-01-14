package com.github.riafka.graduation_boot.web.user;

import com.github.riafka.graduation_boot.model.User;
import com.github.riafka.graduation_boot.repository.UserRepository;
import com.github.riafka.graduation_boot.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.Optional;

import static com.github.riafka.graduation_boot.util.validation.ValidationUtil.checkNotFoundWithId;

@Slf4j
public abstract class AbstractUserController {

    @Autowired
    protected UserRepository repository;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public ResponseEntity<User> get(int id) {
        log.info("get {}", id);
        Optional<User> user = repository.findById(id);
        checkNotFoundWithId(user.isPresent(), id);
        return ResponseEntity.of(repository.findById(id));
    }

    public void delete(int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }

    protected User prepareAndSave(User user) {
        return repository.save(UserUtil.prepareToSave(user));
    }
}