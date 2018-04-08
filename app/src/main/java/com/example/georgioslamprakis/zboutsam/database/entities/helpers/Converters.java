package com.example.georgioslamprakis.zboutsam.database.entities.helpers;

import android.arch.persistence.room.TypeConverter;

/**
 * Created by georgioslamprakis on 08/04/2018.
 */

public class Converters {
    @TypeConverter
    public static ZbtsmLocation locationFromString(String locationString) {
        if(locationString == null)  {
            return null;
        }
        String [] latLng = locationString.split(" ");
        return latLng != null && latLng.length == 2 ? new ZbtsmLocation(Double.valueOf(latLng[0]), Double.valueOf(latLng[1])) : null;

    }

    @TypeConverter
    public static String stringFromLocation(ZbtsmLocation zbtsmLocation) {
        return zbtsmLocation == null ? null : zbtsmLocation.getLat() + " " + zbtsmLocation.getLng();
    }

    //string should have this format "2.123123123 3.41144123124"

}
