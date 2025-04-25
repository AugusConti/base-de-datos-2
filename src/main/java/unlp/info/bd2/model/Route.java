package unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private float price;

    private float totalKm;

    private int maxNumberUsers;

    @ManyToMany
    @JoinTable(
            name = "routes_stops", 
            joinColumns = {@JoinColumn(name = "route_id") }, 
            inverseJoinColumns = { @JoinColumn(name = "stop_id") }
    )
    private List<Stop> stops;

    @ManyToMany(mappedBy = "routes")
    private List<DriverUser> driverList;

    @ManyToMany(mappedBy = "routes")
    private List<TourGuideUser> tourGuideList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
