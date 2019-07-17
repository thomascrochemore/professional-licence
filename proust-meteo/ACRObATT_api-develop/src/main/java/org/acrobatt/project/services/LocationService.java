package org.acrobatt.project.services;

import org.acrobatt.project.dao.mysql.CountryDAO;
import org.acrobatt.project.dao.mysql.LocationDAO;
import org.acrobatt.project.model.mysql.Country;
import org.acrobatt.project.model.mysql.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class LocationService {

    private static Logger logger = LogManager.getLogger(LocationService.class);

    private static LocationDAO ldao = LocationDAO.getInstance();
    private static CountryDAO cdao = CountryDAO.getInstance();

    private LocationService() {}

    /**
     * Finds all the currently stored locations
     * @return the list of locations
     */
    public static List<Location> findAllLocations() {
        return ldao.getAll();
    }

    /**
     * Finds one location using its ID
     * @param id the id
     * @return the location, or null if it doesn't exist
     */
    public static Location findLocationById(Long id) throws IllegalArgumentException {
        if(id == null) throw new IllegalArgumentException("argument cannot be null");
        return ldao.getById(id);
    }

    /**
     * Finds a location based on its coordinates.
     * This function calculates the nearest stored location from the supplied coords,
     * allowing the use of devices where the indicated coordinates aren't always the same.
     * @param lat the latitude
     * @param lng the longitude
     * @return the nearest location
     * @throws IllegalArgumentException if one or more arguments are null
     */
    public static Location findLocationForCoordinates(Float lat, Float lng) throws IllegalArgumentException {
        if(lat == null || lng == null) throw new IllegalArgumentException("arguments cannot be null");
        List<Location> locs = ldao.getAll();
        logger.debug("Initial coords = "+lat+", "+lng);

        try {
            Location location = locs.get(0);
            double minDistance = Float.MAX_VALUE;

            for (Location loc : locs) {
                Float tmpLat = loc.getLatitude();
                Float tmpLng = loc.getLongitude();

                double distance = Math.hypot(lat-tmpLat, lng-tmpLng);
                if(distance < minDistance) {
                    minDistance = distance;
                    location = loc;
                }
            }

            logger.debug("Nearest location: "+ location);
            return location;
        } catch(Exception e) {
            logger.fatal(e);
        }
        return null;
    }

    /**
     * Returns all stored locations inside of a stored country (unused)
     * @param c the country object
     * @return the list of locations, or null if there aren't any
     * @throws IllegalArgumentException if the country is null
     */
    public static List<Location> findLocationsInCountry(Country c) throws IllegalArgumentException {
        if(c == null || cdao.getById(c.getId()) == null) throw new IllegalArgumentException("argument is null or doesn't exist in the records");
        return ldao.getByCountry(c);
    }

    /**
     * Removes a location (unused)
     * @param l the location to remove
     */
    public static void removeLocation(Location l) {
        if(l == null || ldao.getById(l.getId()) == null) throw new IllegalArgumentException("argument is null or doesn't exist in the records");
        ldao.delete(l);
    }

    /**
     * Adds a new location to the database
     * @param name the name of the city
     * @param lat the approximate latitude
     * @param lng the approximate longitude
     * @param c the country where the location is
     * @return the newly added location
     */
    public static Location addNewLocation(String name, Float lat, Float lng, Country c) {
        Location l = new Location(name, lat, lng, c);
        return ldao.insert(l);
    }

    /**
     * Returns a compat list of the location names. For use client-side in dropdown lists.
     * @return the list of names
     */
    public static List<String> getCompactNameList() {
        List<String> names = new ArrayList<>();
        List<Location> locs = ldao.getAll();
        for(Location l : locs) {
            names.add(l.getCity());
        }
        return names;
    }
}
