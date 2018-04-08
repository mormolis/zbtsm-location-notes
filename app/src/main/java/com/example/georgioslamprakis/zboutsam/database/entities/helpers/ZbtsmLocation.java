package com.example.georgioslamprakis.zboutsam.database.entities.helpers;

/**
 * Created by georgioslamprakis on 08/04/2018.
 */

public class ZbtsmLocation {
    private double lat;
    private  double lng;

    public ZbtsmLocation(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ZbtsmLocation zbtsmLocation = (ZbtsmLocation) o;

        if (Double.compare(zbtsmLocation.lat, lat) != 0) return false;
        return Double.compare(zbtsmLocation.lng, lng) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(lat);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lng);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ZbtsmLocation{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    public double getLat() {

        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
