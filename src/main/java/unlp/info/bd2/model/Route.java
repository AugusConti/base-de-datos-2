package unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, precision = 2)
    private float price;

    @Column(nullable = false, precision = 4)
    private float totalKm;

    @Column(nullable = false)
    private int maxNumberUsers;

    @ManyToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "routes_stops",
            joinColumns = { @JoinColumn(name = "route_id") },
            inverseJoinColumns = { @JoinColumn(name = "stop_id") }
    )
    private List<Stop> stops;

    @ManyToMany(mappedBy = "routes", cascade = {}, fetch = FetchType.EAGER)
    private List<DriverUser> drivers;

    @ManyToMany(mappedBy = "routes", cascade = {}, fetch = FetchType.EAGER)
    private List<TourGuideUser> tourGuides;

    public Route() {
    }

    public Route(String name, float price, float totalKm, int maxNumberUsers, List<Stop> stops) {
        this.name = name;
        this.price = price;
        this.totalKm = totalKm;
        this.maxNumberUsers = maxNumberUsers;
        this.stops = stops;
        this.drivers = new ArrayList<>();
        this.tourGuides = new ArrayList<>();
    }

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

    public List<DriverUser> getDrivers() {
        return drivers;
    }

    public List<DriverUser> getDriverList() {
        return drivers;
    }

    public void setDrivers(List<DriverUser> drivers) {
        this.drivers = drivers;
    }

    public List<TourGuideUser> getTourGuides() {
        return tourGuides;
    }

    public List<TourGuideUser> getTourGuideList() {
        return tourGuides;
    }

    public void setTourGuides(List<TourGuideUser> tourGuides) {
        this.tourGuides = tourGuides;
    }

    public void addDriver(DriverUser d) {
        this.drivers.add(d);
        d.addRoute(this);
    }

    public void addTourGuide(TourGuideUser t) {
        this.tourGuides.add(t);
        t.addRoute(this);
    }
}
