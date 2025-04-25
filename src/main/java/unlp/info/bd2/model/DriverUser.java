package unlp.info.bd2.model;

import jakarta.persistence.*;

import jakarta.persistence.JoinColumn;
import java.util.List;

@Entity
public class DriverUser extends User {

    private String expedient;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "drivers_routes", 
            joinColumns = { @JoinColumn(name = "driver_id") },
            inverseJoinColumns = {@JoinColumn(name = "route_id") }
    )
    private List<Route> routes;

    public String getExpedient() {
        return expedient;
    }

    public void setExpedient(String expedient) {
        this.expedient = expedient;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRouts(List<Route> routs) {
        this.routes = routs;
    }

    public void addRoute(Route r) {
        this.routes.add(r);
    }

    public boolean canBeDesactive() {
        return this.routes.isEmpty();
    }
}
