package io.swagger.api.impl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test class for Main class.
 *
 * @author David W. Arnold
 * @version 23/06/2020
 */
public class MainTest
{
    Main main;
    JSONArray allUsers;
    JSONArray allLondonUsers;
    int repeats;
    int min;
    int max;
    List<Integer> randomIndexes;
    JSONArray randomUsers;

    @org.junit.Before
    public void setUp() throws IOException
    {
        main = new Main();

        // curl -X GET "https://bpdts-test-app.herokuapp.com/users" -H "accept: application/json"
        String urlAll = main.getDWP_API() + "users";
        String[] commandAll = {"curl", "-X", "GET", urlAll, "H", "Accept: application/json"};
        ProcessBuilder processBuilderAll = new ProcessBuilder(commandAll);
        Process processAll = processBuilderAll.start();
        StringBuilder sbAll = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(processAll.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                sbAll.append(line);
            }
            allUsers = new JSONArray(sbAll.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // curl -X GET "https://bpdts-test-app.herokuapp.com/city/{city}/users" -H "accept: application/json"
        String urlLon = main.getDWP_API() + "city/London/users";
        String[] commandLon = {"curl", "-X", "GET", urlLon, "H", "Accept: application/json"};
        ProcessBuilder processBuilderLon = new ProcessBuilder(commandLon);
        Process processLon = processBuilderLon.start();
        StringBuilder sbLon = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(processLon.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                sbLon.append(line);
            }
            allLondonUsers = new JSONArray(sbLon.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        repeats = 5; // The number of random user ID's to query for testing
        min = 1; // Lowest index, Included
        max = 1001; // Highest index, Excluded
        randomIndexes = new ArrayList<>();
        randomUsers = new JSONArray();
        for (int i = 1; i <= repeats; ) {
            int random_int = (int) (Math.random() * (max - min + 1) + min);
            if (!randomIndexes.contains(random_int)) {
                randomIndexes.add(random_int);
                // curl -X GET "https://bpdts-test-app.herokuapp.com/user/{id}" -H "accept: application/json"
                String url = main.getDWP_API() + "user/" + random_int;
                String[] commandByID = {"curl", "-X", "GET", url, "H", "Accept: application/json"};
                ProcessBuilder processBuilder = new ProcessBuilder(commandByID);
                Process processByID = processBuilder.start();
                StringBuilder sbByID = new StringBuilder();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(processByID.getInputStream()))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        sbByID.append(line);
                    }
                    randomUsers.put(new JSONObject(sbByID.toString()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                i++;
            }
        }
    }

    @org.junit.After
    public void tearDown()
    {
    }

    @org.junit.Test
    public void distance()
    {
        double BIRMINGHAM_LAT = 52.489471; // https://www.latlong.net/place/birmingham-west-midlands-uk-2716.html
        double BIRMINGHAM_LONG = -1.898575; // https://www.latlong.net/place/birmingham-west-midlands-uk-2716.html
        double miles1 = main.distance(main.getLONDON_LAT(), main.getLONDON_LAT(), main.getLONDON_LONG(), main.getLONDON_LONG());
        double miles2 = main.distance(BIRMINGHAM_LAT, main.getLONDON_LAT(), BIRMINGHAM_LONG, main.getLONDON_LONG());
        assertEquals(0.0, miles1, 0.0);
        assertEquals(101.56, miles2, 0.01);
    }

    @org.junit.Test
    public void queryAllUsers() throws IOException
    {
        compareJSONs(main.queryAllUsers(), allUsers, false);
    }

    @org.junit.Test
    public void queryUsersByCity() throws IOException
    {
        compareJSONs(main.queryUsersByCity("London"), allLondonUsers, false);
    }

    @org.junit.Test
    public void queryUserByID() throws IOException
    {
        JSONArray jsonArray = new JSONArray();
        for (int ri : randomIndexes) {
            jsonArray.put(main.queryUserByID(ri));
        }
        compareJSONs(jsonArray, randomUsers, true);
    }

    private void compareJSONs(JSONArray results, JSONArray expectedArray, boolean featuringCity)
    {
        for (int i = 0; i < results.length(); i++) {
            JSONObject expected = expectedArray.getJSONObject(i);
            JSONObject actual = results.getJSONObject(i);
            assertEquals(expected.getInt("id"), actual.getInt("id"));
            assertEquals(expected.getString("first_name"), actual.getString("first_name"));
            assertEquals(expected.getString("last_name"), actual.getString("last_name"));
            assertEquals(expected.getString("email"), actual.getString("email"));
            assertEquals(expected.getString("ip_address"), actual.getString("ip_address"));
            assertEquals(expected.getDouble("latitude"), actual.getDouble("latitude"), 0.0);
            assertEquals(expected.getDouble("longitude"), actual.getDouble("longitude"), 0.0);
            if (featuringCity) {
                assertEquals(expected.getString("city"), actual.getString("city"));
            }
        }
    }
}