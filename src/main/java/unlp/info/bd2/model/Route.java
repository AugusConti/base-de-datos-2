package unlp.info.bd2.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.List;

@Document
public class Route {

    @MongoId(FieldType.OBJECT_ID)
    private ObjectId id;

    @Field
    private String name;

    @Field
    private float price;

    @Field
    private float totalKm;

    @Field
    private int maxNumberUsers;

    @DBRef(lazy = true)
    private List<Stop> stops;

    @DBRef(lazy = true)
    private List<DriverUser> driverList;

    @DBRef(lazy = true)
    private List<TourGuideUser> tourGuideList;

    public Route() {
    }

    public Route(String name, float price, float totalKm, int maxNumberUsers,List<Stop> stops) {
        this.name = name;
        this.price = price;
        this.totalKm = totalKm;
        this.maxNumberUsers = maxNumberUsers;
        this.stops = stops;
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
