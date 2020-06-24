package io.swagger.api.impl;

import io.swagger.api.GetUsersApiService;
import io.swagger.api.NotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2020-06-23T15:45:21.078Z")
public class GetUsersApiServiceImpl extends GetUsersApiService
{
    @Override
    public Response getUsers(SecurityContext securityContext) throws NotFoundException, IOException
    {
        Main main = new Main();

        String city = "London";
        JSONArray londonUsers = main.queryUsersByCity(city);
        main.populateUsersByCityFromJSON(londonUsers, city);

        Set<User> finalUsers = main.getUsersByCity();
        Set<Integer> finalUsersIDs = new HashSet<>();
        for (User finalUser : finalUsers) {
            finalUsersIDs.add(finalUser.getId());
        }

        JSONArray allUsers = main.queryAllUsers();
        for (int i = 0; i < allUsers.length(); i++) {
            JSONObject o = allUsers.getJSONObject(i);
            if (!finalUsersIDs.contains(o.getInt("id"))) {
                // Now checking non-london users to see if their current coordinates are
                // within 50 miles of London
                double latStart = o.getDouble("latitude");
                double lonStart = o.getDouble("longitude");
                double miles = main.distance(latStart, main.getLONDON_LAT(),
                        lonStart, main.getLONDON_LONG());
                if (miles < 50) {
                    int userID = o.getInt("id");
                    finalUsers.add(new User(userID, o.getString("first_name"),
                            o.getString("last_name"), o.getString("email"),
                            o.getString("ip_address"), o.getDouble("latitude"),
                            o.getDouble("longitude"), main.queryUserCity(userID)));
                    finalUsersIDs.add(userID);
                }
            }
        }

        return Response.ok().entity(finalUsers).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
