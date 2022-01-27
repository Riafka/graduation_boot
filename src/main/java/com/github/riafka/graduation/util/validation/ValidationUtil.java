package com.github.riafka.graduation.util.validation;

import com.github.riafka.graduation.HasId;
import com.github.riafka.graduation.error.IllegalRequestDataException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;

import java.util.Optional;

@UtilityClass
@Slf4j
public class ValidationUtil {
    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new IllegalRequestDataException("Not found entity with " + msg);
        }
    }

    public static <T> T checkNotFoundOptional(Optional<T> optionalBean, String msg) {
        if (optionalBean.isEmpty()) {
            throw new IllegalRequestDataException("Not found entity with " + msg);
        }
        return optionalBean.get();
    }

    public static <T> T checkNotFoundOptional(Optional<T> optionalBean, int id) {
        checkNotFoundWithId(optionalBean.isPresent(), id);
        return optionalBean.get();
    }


    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must have id=" + id);
        }
    }

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new IllegalRequestDataException("Entity with id=" + id + " not found");
        }
    }

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }
}