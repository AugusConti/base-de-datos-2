package unlp.info.bd2.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Driver")
public class DriverUser extends User {

    @Column(nullable = true, length = 255)
    private String expedient;

    @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "drivers_routes",
            joinColumns = { @JoinColumn(name = "driver_id") },
            inverseJoinColumns = { @JoinColumn(name = "route_id") }
    )
    private List<Route> routes;

    public DriverUser() {
    }

    public DriverUser(String username, String password, String name, String email, Date birthdate, String phoneNumber,
            String expedient) {
        super(username, password, name, email, birthdate, phoneNumber);
        this.expedient = expedient;
        this.routes = new ArrayList<>();
    }

    public String getExpedient() {
        return expedient;
    }

    public void setExpedient(String expedient) {
        this.expedient = expedient;
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
    @Override
    public boolean canBeDesactive() {
        return this.routes.isEmpty();
    }
}
