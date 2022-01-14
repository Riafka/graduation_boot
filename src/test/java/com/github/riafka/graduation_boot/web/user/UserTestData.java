package com.github.riafka.graduation_boot.web.user;

import com.github.riafka.graduation_boot.model.Role;
import com.github.riafka.graduation_boot.model.User;
import com.github.riafka.graduation_boot.util.JsonUtil;
import com.github.riafka.graduation_boot.web.MatcherFactory;

import java.util.Collections;
import java.util.Date;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "password");

    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final int USER_ID_2 = 3;
    public static final int NOT_FOUND = 100;
    public static final String USER_MAIL = "user@gmail.com";
    public static final String USER_2_MAIL = "user2@gmail.com";
    public static final String ADMIN_MAIL = "admin@gmail.com";

    public static final User user = new User(USER_ID, "User", USER_MAIL, "password", Role.USER);
    public static final User user2 = new User(USER_ID_2, "User2", USER_2_MAIL, "password", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", ADMIN_MAIL, "admin", Role.ADMIN, Role.USER);

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER_ID, "UpdatedName", USER_MAIL, "newPass", false, new Date(), Collections.singleton(Role.ADMIN));
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
