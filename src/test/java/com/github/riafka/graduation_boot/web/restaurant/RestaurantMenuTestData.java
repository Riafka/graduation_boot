package com.github.riafka.graduation_boot.web.restaurant;

import com.github.riafka.graduation_boot.model.Restaurant;
import com.github.riafka.graduation_boot.model.RestaurantMenu;
import com.github.riafka.graduation_boot.to.RestaurantMenuTo;
import com.github.riafka.graduation_boot.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static com.github.riafka.graduation_boot.web.restaurant.RestaurantTestData.*;

public class RestaurantMenuTestData {
    public static final MatcherFactory.Matcher<RestaurantMenu> RESTAURANT_MENU_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(RestaurantMenu.class, "restaurant");

    public static final MatcherFactory.Matcher<RestaurantMenuTo> RESTAURANT_MENU_TO_MATCHER = MatcherFactory.usingEqualsComparator(RestaurantMenuTo.class);

    public static final int OYSTERS_ID = 1;
    public static final int FRIED_SQUID_ID = 2;
    public static final int SALMON_WITH_LEMON_ID = 3;
    public static final int RED_WINE_ID = 4;
    public static final int TASTY_DESERT_ID = 5;
    public static final int BIG_MAC_ID = 6;
    public static final int BIG_TASTY_ID = 7;
    public static final int HAMBURGER_ID = 8;
    public static final int COCA_COLA_ID = 9;
    public static final int COFFEE_ID = 10;
    public static final int KRABBY_MEAL_ID = 11;
    public static final int KRABBY_PATTY_ID = 12;
    public static final int KELP_RINGS_ID = 13;
    public static final int KELP_SHAKE_ID = 14;
    public static final int SAILORS_SURPRISE_ID = 15;
    public static final int CRAYFISH_SALAD_ID = 16;
    public static final int ONION_SOUP_ID = 17;
    public static final int BUTTER_BEAN_RAGOUT_ID = 18;
    public static final int BACON_WRAPPED_CHICKEN_ID = 19;
    public static final int FISH_OF_THE_DAY_ID = 20;
    public static final int KHINKALI_ID = 21;
    public static final int KHACHAPURI_IN_ADJARIAN_STYLE_ID = 22;
    public static final int KHACHAPURI_IN_IMERETIAN_STYLE_ID = 23;

    public static final RestaurantMenu oysters = new RestaurantMenu(OYSTERS_ID, blueLagoon, "Oysters", 100000, LocalDate.now());
    public static final RestaurantMenu friedSquid = new RestaurantMenu(FRIED_SQUID_ID, blueLagoon, "Fried squid", 50000, LocalDate.now());
    public static final RestaurantMenu salmonWithLemon = new RestaurantMenu(SALMON_WITH_LEMON_ID, blueLagoon, "Salmon with lemon", 50000, LocalDate.now());
    public static final RestaurantMenu redWine = new RestaurantMenu(RED_WINE_ID, blueLagoon, "Red Wine", 250000, LocalDate.now());
    public static final RestaurantMenu tastyDesert = new RestaurantMenu(TASTY_DESERT_ID, blueLagoon, "Tasty desert", 25000, LocalDate.now());

    public static final RestaurantMenu bigMac = new RestaurantMenu(BIG_MAC_ID, mcDonalds, "Big Mac", 13000, LocalDate.now());
    public static final RestaurantMenu bigTasty = new RestaurantMenu(BIG_TASTY_ID, mcDonalds, "Big Tasty", 26000, LocalDate.now());
    public static final RestaurantMenu hamburger = new RestaurantMenu(HAMBURGER_ID, mcDonalds, "Hamburger", 5000, LocalDate.now());
    public static final RestaurantMenu cocaCola = new RestaurantMenu(COCA_COLA_ID, mcDonalds, "Coca Cola", 3000, LocalDate.now());
    public static final RestaurantMenu coffee = new RestaurantMenu(COFFEE_ID, mcDonalds, "Coffee", 5000, LocalDate.now());

    public static final RestaurantMenu krabbyMeal = new RestaurantMenu(KRABBY_MEAL_ID, krustyKrabs, "Krabby meal", 25050, LocalDate.now());
    public static final RestaurantMenu krabbyPatty = new RestaurantMenu(KRABBY_PATTY_ID, krustyKrabs, "Krabby Patty", 26030, LocalDate.now());
    public static final RestaurantMenu kelpRings = new RestaurantMenu(KELP_RINGS_ID, krustyKrabs, "Kelp Rings", 20000, LocalDate.now());
    public static final RestaurantMenu kelpShake = new RestaurantMenu(KELP_SHAKE_ID, krustyKrabs, "Kelp Shake", 18040, LocalDate.now());
    public static final RestaurantMenu sailorsSurprise = new RestaurantMenu(SAILORS_SURPRISE_ID, krustyKrabs, "Sailors Surprise", 30000, LocalDate.now());

    public static final RestaurantMenu crayfishSalad = new RestaurantMenu(CRAYFISH_SALAD_ID, riga, "Crayfish salad", 50000, LocalDate.now());
    public static final RestaurantMenu onionSoup = new RestaurantMenu(ONION_SOUP_ID, riga, "Onion soup", 75000, LocalDate.now());
    public static final RestaurantMenu butterBeanRagout = new RestaurantMenu(BUTTER_BEAN_RAGOUT_ID, riga, "Butter bean ragout", 80000, LocalDate.now());
    public static final RestaurantMenu baconWrappedChicken = new RestaurantMenu(BACON_WRAPPED_CHICKEN_ID, riga, "Bacon wrapped chicken", 70000, LocalDate.now());
    public static final RestaurantMenu fishOfTheDay = new RestaurantMenu(FISH_OF_THE_DAY_ID, riga, "Fish of the Day", 90000, LocalDate.now());

    public static final RestaurantMenu khinkali = new RestaurantMenu(KHINKALI_ID, suliko, "Khinkali", 40000, LocalDate.now());
    public static final RestaurantMenu khachapuriInAdjarianStyle = new RestaurantMenu(KHACHAPURI_IN_ADJARIAN_STYLE_ID, suliko, "Khachapuri in Adjarian style", 50000, LocalDate.now());
    public static final RestaurantMenu khachapuriInImeretianStyle = new RestaurantMenu(KHACHAPURI_IN_IMERETIAN_STYLE_ID, suliko, "Khachapuri in Imeretian style", 50000, LocalDate.now());

    public static final Restaurant blueLagoonWithMenu = new Restaurant(BLUE_LAGOON_ID, "Blue Lagoon", List.of(friedSquid, oysters, redWine, salmonWithLemon, tastyDesert));
    public static final Restaurant mcDonaldsWithMenu = new Restaurant(MC_DONALDS_ID, "MC Donald's", List.of(bigMac, bigTasty, cocaCola, coffee, hamburger));
    public static final Restaurant krustyKrabsWithMenu = new Restaurant(KRUSTY_KRABS_ID, "Krusty Krabs", List.of(kelpRings, kelpShake, krabbyPatty, krabbyMeal, sailorsSurprise));
    public static final Restaurant rigaWithMenu = new Restaurant(RIGA_ID, "Riga", List.of(baconWrappedChicken, butterBeanRagout, crayfishSalad, fishOfTheDay, onionSoup));
    public static final Restaurant sulikoWithMenu = new Restaurant(SULIKO_ID, "Suliko", List.of(khachapuriInAdjarianStyle, khachapuriInImeretianStyle, khinkali));

    public static RestaurantMenuTo getNew() {
        return new RestaurantMenuTo(null, "NewMeal", BLUE_LAGOON_ID, 50000, LocalDate.now());
    }

    public static RestaurantMenuTo getUpdated() {
        return new RestaurantMenuTo(1, "UpdatedMeal", BLUE_LAGOON_ID, 40000, LocalDate.now());
    }

    public static RestaurantMenuTo getInvalid() {
        return new RestaurantMenuTo(1, "Tasty desert", BLUE_LAGOON_ID, 40000, LocalDate.now());
    }
}
