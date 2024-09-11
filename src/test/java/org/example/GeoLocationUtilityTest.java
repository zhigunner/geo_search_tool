package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

class GeoLocationUtilityTest {

    GeoLocationUtility util = new GeoLocationUtility();

    @Test
    void testCityStateLocation() throws IOException {
        String result = util.getLocationData("Scranton, PA");
        System.out.println("API Response for Scranton, PA: " + result);

        assertNotNull(result);
        assertTrue(result.contains("Scranton"));
        assertTrue(result.contains("Lat"));
        assertTrue(result.contains("Lon"));
    }

    @Test
    void testZipCodeLocation() throws IOException {
        String result = util.getZipData("87123");
        System.out.println("API Response for Zip 87123: " + result);

        assertNotNull(result);
        assertTrue(result.contains("Albuquerque"));
        assertTrue(result.contains("Lat"));
        assertTrue(result.contains("Lon"));
    }

    @Test
    void testInvalidLocation() throws IOException {

        String result = util.getLocationData("Winterfell, SK");
        System.out.println("API Response for Winterfell, SK: " + result);

        assertEquals("No results found for the given location.", result);
    }

    @Test
    void testMultipleLocations() throws IOException {
        String[] locations = {"Pasadena, CA", "60623"};

        for (String location : locations) {
            if (location.matches("\\d{5}")) {
                String result = util.getZipData(location);
                System.out.println("API Response for Zip " + location + ": " + result);

                assertNotNull(result);
                assertTrue(result.contains("Chicago"));
                assertTrue(result.contains("Lat"));
                assertTrue(result.contains("Lon"));
            } else {
                String result = util.getLocationData(location);
                System.out.println("API Response for " + location + ": " + result);

                assertNotNull(result);
                assertTrue(result.contains("Pasadena"));
                assertTrue(result.contains("Lat"));
                assertTrue(result.contains("Lon"));
            }
        }
    }
}