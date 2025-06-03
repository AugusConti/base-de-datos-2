package unlp.info.bd2.model;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class Route {

    private ObjectId id;

    private String name;

    private float price;

    private float totalKm;

    private int maxNumberUsers;

    private List<Stop> stops;

    private List<DriverUser> driverList;

    private List<TourGuideUser> tourGuideList;

    public Route() {
    }

    public Route(String name, float price, float totalKm, int maxNumberUsers) {
        this.name = name;
        this.price = price;
        this.totalKm = totalKm;
        this.maxNumberUsers = maxNumberUsers;
        this.stops = new ArrayList<>();
        this.driverList = new ArrayList<>();
        this.tourGuideList = new ArrayList<>();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getTotalKm() {
        return totalKm;
    }

    public void setTotalKm(float totalKm) {
        this.totalKm = totalKm;
    }

    public int getMaxNumberUsers() {
        return maxNumberUsers;
    }

    public void setMaxNumberUsers(int maxNumberUsers) {
        this.maxNumberUsers = maxNumberUsers;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public List<DriverUser> getDriverList() {
        return driverList;
    }

    public void setDriverList(List<DriverUser> driverList) {
        this.driverList = driverList;
    }

    public List<TourGuideUser> getTourGuideList() {
        return tourGuideList;
    }

    public void setTourGuideList(List<TourGuideUser> tourGuideList) {
        this.tourGuideList = tourGuideList;
    }

    public void addDriver(DriverUser d) {
        this.driverList.add(d);
        d.addRoute(this);
    }

    public void addTourGuide(TourGuideUser t) {
        this.tourGuideList.add(t);
        t.addRoute(this);
    }

}
