package org.acrobatt.project.utils;

public class UnitUtils {

    private UnitUtils() {}

    /**
     * Converts a Kelvin value in Celsius
     * @param kelvinValue the value in Kelvin
     * @return the value in Celsius
     */
    public static float convertKelvinToCelsius(float kelvinValue){
        return kelvinValue - 273.15f;
    }
    public static float convertFahrenheitToCelsius(float farenheitValue){return (farenheitValue - 32.0f) * 5.0f/9.0f;}
}
