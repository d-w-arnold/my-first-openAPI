package io.swagger.api.impl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Please complete the online test following the link: https://bpdts-test-app.herokuapp.com
 * <p>
 * The link contains a swagger spec for an API.
 * <p>
 * Using the language of your choice please build your own API
 * which calls the API at https://bpdts-test-app.herokuapp.com/,
 * and returns people who are listed as either living in London,
 * or whose current coordinates are within 50 miles of London.
 * <p>
 * Push the answer to Github, and send
 * longbenton.peopleandcapabilitysoftwareengineertest@dwp.gsi.gov.uk
 * a link within 7 days of receipt of this test. TODO: Finish by 30th Jun @ 11:30am
 * <p>
 * Please consider your approach to clean code and testing when composing your solution.
 * <p>
 * On receipt of your completed test to the above email address,
 * we will contact you via your civil service jobs account,
 * to advise if you have been successful in being progressed to the next stage,
 * which is a video interview.
 *
 * @author David W. Arnold
 * @version 23/06/2020
 */
public class Main
{
    // The URL for the DWP API
    private final String DWP_API = "https://bpdts-test-app.herokuapp.com/";
    // Latitude of the centre of London (in Decimal Degrees) https://www.latlong.net/place/london-the-uk-14153.html
    private final double LONDON_LAT = 51.509865;
    // Longitude of the centre of London (in Decimal Degrees) https://www.latlong.net/place/london-the-uk-14153.html
    private final double LONDON_LONG = -0.118092;
    // Intended to store all users
    private Set<User> allUsers;
    // Intended to store users with a city property matching a specified city
    private Set<User> usersByCity;

    public Main()
    {
        allUsers = new HashSet<>();
        usersByCity = new HashSet<>();
    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * @param lat1 Lat Start point
     * @param lat2 Lat End point
     * @param lon1 Lon Start point
     * @param lon2 Lon End point
     * @param el1  Start altitude in metres
     * @param el2  End altitude in metres
     * @returns Distance in miles
     */
    public double distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2)
    {
        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to metres
        double height = el1 - el2;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        return metresToMiles(Math.sqrt(distance));
    }

    /**
     * Get the lat of the centre of London (in Decimal Degrees)
     *
     * @return Lat of the center of London
     */
    public double getLONDON_LAT()
    {
        return LONDON_LAT;
    }

    /**
     * Get the long of the centre of London (in Decimal Degrees)
     *
     * @return Long of the center of London
     */
    public double getLONDON_LONG()
    {
        return LONDON_LONG;
    }

    /**
     * Getter for allUsers
     *
     * @return allUsers
     */
    public Set<User> getAllUsers()
    {
        return allUsers;
    }

    /**
     * Getter for usersByCity
     *
     * @return usersByCity
     */
    public Set<User> getUsersByCity()
    {
        return usersByCity;
    }

    /**
     * Query the DWP Api to get all users
     *
     * @return All users
     * @throws IOException Using cURL
     */
    public JSONArray queryAllUsers() throws IOException
    {
        // curl -X GET "https://bpdts-test-app.herokuapp.com/users" -H "accept: application/json"
        String url = DWP_API + "users";
        String[] command = {"curl", "-X", "GET", url, "H", "Accept: application/json"};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        return parseInputStreamToJSONArray(process);
    }

    /**
     * Query the DWP API to get the city of a user by specifying their user ID.
     *
     * @param id The user's ID
     * @return The user's city
     * @throws IOException Using cURL
     */
    public String queryUserCity(int id) throws IOException
    {
        // curl -X GET "https://bpdts-test-app.herokuapp.com/user/{id}" -H "accept: application/json"
        String url = DWP_API + "user/" + id;
        String[] command = {"curl", "-X", "GET", url, "H", "Accept: application/json"};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        JSONObject json = parseInputStreamToJSONObject(process);
        return json.getString("city");
    }

    /**
     * Query the DWP API to get all users that match with a specificed city
     *
     * @param city The city to match with
     * @return All users that have the specified city as a property
     * @throws IOException Using cURL
     */
    public JSONArray queryUsersByCity(String city) throws IOException
    {
        // curl -X GET "https://bpdts-test-app.herokuapp.com/city/{city}/users" -H "accept: application/json"
        String url = DWP_API + "city/" + city + "/users";
        String[] command = {"curl", "-X", "GET", url, "H", "Accept: application/json"};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        return parseInputStreamToJSONArray(process);
    }

    /**
     * Populates allUsers from a given JSON array
     *
     * @param jsonArray All users as a JSON array
     * @throws IOException Using cURL
     */
    public void populateAllUsersFromJSON(JSONArray jsonArray) throws IOException
    {
        allUsers = new HashSet<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            int userID = o.getInt("id");
            allUsers.add(new User(userID, o.getString("first_name"),
                    o.getString("last_name"), o.getString("email"),
                    o.getString("ip_address"), o.getDouble("latitude"),
                    o.getDouble("longitude"), queryUserCity(userID)));
        }
    }

    /**
     * Populates usersByCity from a given JSON array
     *
     * @param jsonArray Users matching a specified city as a JSON array
     * @param city      The city used in the query to match against
     */
    public void populateUsersByCityFromJSON(JSONArray jsonArray, String city)
    {
        usersByCity = new HashSet<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            int userID = o.getInt("id");
            usersByCity.add(new User(userID, o.getString("first_name"),
                    o.getString("last_name"), o.getString("email"),
                    o.getString("ip_address"), o.getDouble("latitude"),
                    o.getDouble("longitude"), city));
        }
    }

    /**
     * Convert metres to miles.
     *
     * @param metres A distance in metres
     * @return Distance in miles
     */
    private double metresToMiles(double metres)
    {
        return metres * 0.000621371192; // 1 metre is 0.000621371192 miles
    }

    /**
     * Parse an InputStream from a cURL process to a JSON Array
     *
     * @param process The cURL process
     * @return A retrieved JSON Array
     */
    private JSONArray parseInputStreamToJSONArray(Process process)
    {
        StringBuilder sb = new StringBuilder();
        JSONArray jsonArray = new JSONArray();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            jsonArray = new JSONArray(sb.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return jsonArray;
    }

    /**
     * Parse an InputStream from a cURL process to a JSON Object
     *
     * @param process The cURL process
     * @return A retrieved JSON Object
     */
    private JSONObject parseInputStreamToJSONObject(Process process)
    {
        StringBuilder sb = new StringBuilder();
        JSONObject jsonObject = new JSONObject();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            jsonObject = new JSONObject(sb.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }
}
