package io.swagger.api.impl;

/**
 * A User from the DWP API.
 *
 * @author David W. Arnold
 * @version 23/06/2020
 */
public class User
{
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String ipAddress;
    private final double latitude;
    private final double longitude;
    private final String city;

    public User(int id, String firstName, String lastName, String email, String ipAddress, double latitude, double longitude, String city)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.ipAddress = ipAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }

    public int getId()
    {
        return id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public String getCity()
    {
        return city;
    }
}
