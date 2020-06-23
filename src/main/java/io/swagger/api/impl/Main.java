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
    private final String DWP_API = "https://bpdts-test-app.herokuapp.com/";
    private Set<User> allUsers;
    private Set<User> users;

    public Main()
    {
        allUsers = new HashSet<>();
        users = new HashSet<>();
    }

    public void getUsers() throws IOException
    {
        // curl -X GET "https://bpdts-test-app.herokuapp.com/users" -H "accept: application/json"
        String url = DWP_API + "users";
        String[] command = {"curl", "-X", "GET", url, "H", "Accept: application/json"};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        populateUsersFromJSON(parseInputStreamToJSONArray(process));
    }

    public void getUsers(String city) throws IOException
    {
        // curl -X GET "https://bpdts-test-app.herokuapp.com/city/London/users" -H "accept: application/json"
        String url = DWP_API + "city/" + city + "/users";
        String[] command = {"curl", "-X", "GET", url, "H", "Accept: application/json"};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        populateUsersFromJSON(parseInputStreamToJSONArray(process), city);
    }

    private String getUserCity(int id) throws IOException
    {
        // curl -X GET "https://bpdts-test-app.herokuapp.com/user/135" -H "accept: application/json"
        String url = DWP_API + "user/" + id;
        String[] command = {"curl", "-X", "GET", url, "H", "Accept: application/json"};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        JSONObject json = parseInputStreamToJSONObject(process);
        return json.getString("city");
    }

    private JSONArray parseInputStreamToJSONArray(Process process)
    {
        StringBuilder sb = new StringBuilder();
        JSONArray json = new JSONArray();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            json = new JSONArray(sb.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    private JSONObject parseInputStreamToJSONObject(Process process)
    {
        StringBuilder sb = new StringBuilder();
        JSONObject json = new JSONObject();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            json = new JSONObject(sb.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    private void populateUsersFromJSON(JSONArray jsonArray) throws IOException
    {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            int userID = o.getInt("id");
            System.out.println("Adding to 'allUsers', id: " + userID);
            allUsers.add(new User(userID, o.getString("first_name"),
                    o.getString("last_name"), o.getString("email"),
                    o.getString("ip_address"), o.getDouble("latitude"),
                    o.getDouble("longitude"), getUserCity(userID)));
        }
    }

    private void populateUsersFromJSON(JSONArray jsonArray, String city) throws IOException
    {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            int userID = o.getInt("id");
            System.out.println("Adding to 'users', id: " + userID);
            users.add(new User(userID, o.getString("first_name"),
                    o.getString("last_name"), o.getString("email"),
                    o.getString("ip_address"), o.getDouble("latitude"),
                    o.getDouble("longitude"), city));
        }
    }
}
