package unlp.info.bd2.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("TourGuide")
public class TourGuideUser extends User {

    @Column(nullable = true, length = 255)
    private String education;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "tour_guides_routes",
            joinColumns = { @JoinColumn(name = "tour_guide_id") },
            inverseJoinColumns = { @JoinColumn(name = "route_id") }
    )
    private List<Route> routes;

    public TourGuideUser() {
    }

    public TourGuideUser(String username, String password, String name, String email, Date birthdate,
            String phoneNumber, String education) {
        super(username, password, name, email, birthdate, phoneNumber);
        this.education = education;
        this.routes = new ArrayList<>();
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public List<Route> getRoutes() {return routes;}

    public void setRoutes(List<Route> routes) {this.routes = routes;}

    public void addRoute(Route r) {this.routes.add(r);}

    @Override
    public boolean canBeDesactive() {
        return this.routes.isEmpty();
    }

}
