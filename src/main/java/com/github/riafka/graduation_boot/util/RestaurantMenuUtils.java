package com.github.riafka.graduation_boot.util;

import com.github.riafka.graduation_boot.error.IllegalRequestDataException;
import com.github.riafka.graduation_boot.model.RestaurantMenu;
import com.github.riafka.graduation_boot.to.RestaurantMenuTo;
import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

@UtilityClass
public class RestaurantMenuUtils {
    public static RestaurantMenuTo createTo(RestaurantMenu restaurantMenu) {
        return new RestaurantMenuTo(restaurantMenu.getId(), restaurantMenu.getName(), restaurantMenu.getRestaurant().getId(), restaurantMenu.getPrice(), restaurantMenu.getMenuDate());
    }

    public static Optional<RestaurantMenuTo> mapTo(Optional<RestaurantMenu> menuOptional) {
        return menuOptional.map(RestaurantMenuUtils::createTo);
    }

    public static RestaurantMenu createNewFromTo(RestaurantMenuTo restaurantMenuTo) {
        return new RestaurantMenu(restaurantMenuTo.getId(), null, restaurantMenuTo.getName(), formatStringToPrice(restaurantMenuTo.getPrice()), restaurantMenuTo.getMenuDate());
    }

    public static String formatPriceToString(Long longPrice) {

        String returnValue = longPrice.toString();
        boolean minusChar = returnValue.startsWith("-");
        returnValue = returnValue.replace("-", "");

        if (returnValue.length() > 2) {

            String tempStr = returnValue.substring(0, returnValue.length() - 2);
            Long val = Long.parseLong(tempStr);

            DecimalFormat df = new DecimalFormat();
            df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.getDefault()));
            returnValue = df.format(val) + "," + returnValue.substring(returnValue.length() - 2);

        } else {

            if (returnValue.length() == 1) {
                returnValue = "0,0" + returnValue;
            } else {
                returnValue = "0," + returnValue;
            }

        }

        if (minusChar) {
            returnValue = "-" + returnValue;
        }

        return returnValue;
    }

    public static long formatStringToPrice(String stringPrice) {

        long returnLong;

        // 1Test :only one - in front, only digits and "." and one "," , and
        // only 2 digits behind ","
        // if "," only 2 not 1 digit behind
        if (!isValidateLongCurrency(stringPrice)) {
            throw new IllegalRequestDataException("Incorrect format for price: " + stringPrice);
        } else {

            String valueFiltered = stringPrice.replace(String.valueOf((char) 160), "").replaceAll("\\s", "");

            // 2: add 2 00 if no ",":
            if (!valueFiltered.contains(",")) {
                valueFiltered += "00";
            } else {

                //E,C or E,CC
                String[] splittedValue = valueFiltered.split(",");
                if (splittedValue[splittedValue.length - 1].length() == 1) {
                    valueFiltered = valueFiltered + 0;
                }

                valueFiltered = valueFiltered.replace(",", "");
            }
            try {
                returnLong = Long.parseLong(valueFiltered);
            } catch (NumberFormatException numEx) {
                throw new IllegalRequestDataException("Incorrect format for price: " + stringPrice);
            }
        }
        return returnLong;
    }

    private static boolean isValidateLongCurrency(String value) {
        String valueFiltered = value.replace(String.valueOf((char) 160), "").replaceAll("\\s", "");

        String regEx = "^-?[1-9][0-9]*(,[0-9][0-9]?)?$|^-?[0-9](,[0-9][0-9]?)?$|^$";

        return Pattern.matches(regEx, valueFiltered);
    }
}
