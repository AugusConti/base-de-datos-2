package unlp.info.bd2.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class TourGuideUser extends User {

    private String education;

    @ManyToMany()
    @JoinTable(
            name = "tour_guides_routes",
            joinColumns = { @JoinColumn(name = "tour_guide_id") },
            inverseJoinColumns = { @JoinColumn(name = "route_id") }
    )
    private List<Route> routes;


    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public void addRoute(Route r) {
        this.routes.add(r);
    }

    public boolean canBeDesactive() {
        return this.routes.isEmpty();
    }
}
