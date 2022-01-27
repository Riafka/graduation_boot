package com.github.riafka.graduation.web.restaurant;

import com.github.riafka.graduation.model.MenuItem;
import com.github.riafka.graduation.model.Restaurant;
import com.github.riafka.graduation.to.RestaurantTo;
import com.github.riafka.graduation.util.RestaurantUtil;
import com.github.riafka.graduation.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static com.github.riafka.graduation.web.restaurant.RestaurantTestData.*;

public class MenuItemTestData {
    public static final MatcherFactory.Matcher<MenuItem> MENU_ITEM_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuItem.class, "restaurant");

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
    public static final int RED_WINE_ID_2_DAYS_OLD = 27;
    public static final int SALMON_WITH_LEMON_ID_2_DAYS_OLD = 26;
    public static final int TASTY_DESERT_ID_2_DAYS_OLD = 28;

    public static final MenuItem oysters = new MenuItem(OYSTERS_ID, blueLagoon, "Oysters", 100000, LocalDate.now());
    public static final MenuItem friedSquid = new MenuItem(FRIED_SQUID_ID, blueLagoon, "Fried squid", 50000, LocalDate.now());
    public static final MenuItem salmonWithLemon = new MenuItem(SALMON_WITH_LEMON_ID,
            blueLagoon,
            "Salmon with lemon",
            50000,
            LocalDate.now());
    public static final MenuItem salmonWithLemonTwoDaysOld = new MenuItem(SALMON_WITH_LEMON_ID_2_DAYS_OLD,
            blueLagoon,
            "2 days old Salmon with lemon",
            52000,
            LocalDate.now().minusDays(2));
    public static final MenuItem redWine = new MenuItem(RED_WINE_ID, blueLagoon, "Red Wine", 250000, LocalDate.now());
    public static final MenuItem redWineTwoDaysOld = new MenuItem(RED_WINE_ID_2_DAYS_OLD,
            blueLagoon,
            "2 days old Red Wine",
            252000,
            LocalDate.now().minusDays(2));
    public static final MenuItem tastyDesert = new MenuItem(TASTY_DESERT_ID, blueLagoon, "Tasty desert", 25000, LocalDate.now());
    public static final MenuItem tastyDesertTwoDaysOld = new MenuItem(TASTY_DESERT_ID_2_DAYS_OLD,
            blueLagoon,
            "2 days old desert",
            25300,
            LocalDate.now().minusDays(2));

    public static final MenuItem bigMac = new MenuItem(BIG_MAC_ID, mcDonalds, "Big Mac", 13000, LocalDate.now());
    public static final MenuItem bigTasty = new MenuItem(BIG_TASTY_ID, mcDonalds, "Big Tasty", 26000, LocalDate.now());
    public static final MenuItem hamburger = new MenuItem(HAMBURGER_ID, mcDonalds, "Hamburger", 5000, LocalDate.now());
    public static final MenuItem cocaCola = new MenuItem(COCA_COLA_ID, mcDonalds, "Coca Cola", 3000, LocalDate.now());
    public static final MenuItem coffee = new MenuItem(COFFEE_ID, mcDonalds, "Coffee", 5000, LocalDate.now());

    public static final MenuItem krabbyMeal = new MenuItem(KRABBY_MEAL_ID,
            krustyKrabs,
            "Krabby meal",
            25050,
            LocalDate.now());
    public static final MenuItem krabbyPatty = new MenuItem(KRABBY_PATTY_ID,
            krustyKrabs,
            "Krabby Patty",
            26030,
            LocalDate.now());
    public static final MenuItem kelpRings = new MenuItem(KELP_RINGS_ID, krustyKrabs, "Kelp Rings", 20000, LocalDate.now());
    public static final MenuItem kelpShake = new MenuItem(KELP_SHAKE_ID, krustyKrabs, "Kelp Shake", 18040, LocalDate.now());
    public static final MenuItem sailorsSurprise = new MenuItem(SAILORS_SURPRISE_ID,
            krustyKrabs,
            "Sailors Surprise",
            30000,
            LocalDate.now());

    public static final MenuItem crayfishSalad = new MenuItem(CRAYFISH_SALAD_ID,
            riga,
            "Crayfish salad",
            50000,
            LocalDate.now());
    public static final MenuItem onionSoup = new MenuItem(ONION_SOUP_ID, riga, "Onion soup", 75000, LocalDate.now());
    public static final MenuItem butterBeanRagout = new MenuItem(BUTTER_BEAN_RAGOUT_ID,
            riga,
            "Butter bean ragout",
            80000,
            LocalDate.now());
    public static final MenuItem baconWrappedChicken = new MenuItem(BACON_WRAPPED_CHICKEN_ID,
            riga,
            "Bacon wrapped chicken",
            70000,
            LocalDate.now());
    public static final MenuItem fishOfTheDay = new MenuItem(FISH_OF_THE_DAY_ID, riga, "Fish of the Day", 90000, LocalDate.now());

    public static final MenuItem khinkali = new MenuItem(KHINKALI_ID, suliko, "Khinkali", 40000, LocalDate.now());
    public static final MenuItem khachapuriInAdjarianStyle = new MenuItem(KHACHAPURI_IN_ADJARIAN_STYLE_ID,
            suliko,
            "Khachapuri in Adjarian style",
            50000,
            LocalDate.now());
    public static final MenuItem khachapuriInImeretianStyle = new MenuItem(KHACHAPURI_IN_IMERETIAN_STYLE_ID,
            suliko,
            "Khachapuri in Imeretian style",
            50000,
            LocalDate.now());

    public static final RestaurantTo blueLagoonWithMenu = RestaurantUtil.createTo(new Restaurant(BLUE_LAGOON_ID,
            "Blue Lagoon",
            List.of(friedSquid, oysters, redWine, salmonWithLemon, tastyDesert)));
    public static final RestaurantTo mcDonaldsWithMenu = RestaurantUtil.createTo(new Restaurant(MC_DONALDS_ID,
            "MC Donald's",
            List.of(bigMac, bigTasty, cocaCola, coffee, hamburger)));
    public static final RestaurantTo krustyKrabsWithMenu = RestaurantUtil.createTo(new Restaurant(KRUSTY_KRABS_ID,
            "Krusty Krabs",
            List.of(kelpRings, kelpShake, krabbyPatty, krabbyMeal, sailorsSurprise)));
    public static final RestaurantTo rigaWithMenu = RestaurantUtil.createTo(new Restaurant(RIGA_ID,
            "Riga",
            List.of(baconWrappedChicken, butterBeanRagout, crayfishSalad, fishOfTheDay, onionSoup)));
    public static final RestaurantTo sulikoWithMenu = RestaurantUtil.createTo(new Restaurant(SULIKO_ID,
            "Suliko",
            List.of(khachapuriInAdjarianStyle, khachapuriInImeretianStyle, khinkali)));

    public static MenuItem getNew() {
        return new MenuItem(null, blueLagoon, "NewMeal", 50000, LocalDate.now());
    }

    public static MenuItem getUpdated() {
        return new MenuItem(1, blueLagoon, "UpdatedMeal", 40000, LocalDate.now());
    }

    public static MenuItem getInvalid() {
        return new MenuItem(1, blueLagoon, "Tasty desert", 40000, LocalDate.now());
    }
}
