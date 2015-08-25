package com.opensource.cybercitizen;

import android.location.Location;

import com.opensource.util.Util;

import org.junit.Test;

public class UtilTest
{
    @Test
    public void testUtils()
    {

        float[] floats = new float[]{100, 2000, 1.14f};
        for (float f : floats)
        {
            System.out.println(Util.formatDistanceInMeters(f, 2));
        }
    }

    @Test
    public void testDistance()
    {
        Location location = new Location("HERE");
        location.setLatitude(3.239795);
        location.setLongitude(101.4248717);

        Location locationTwo = new Location("HERE");
        location.setLatitude(2.9173249);
        location.setLongitude(101.6501049);

        System.out.println(Util.formatDistanceInMeters(location.distanceTo(locationTwo), 1));
    }
}
