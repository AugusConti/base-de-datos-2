package unlp.info.bd2.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Stop {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;


    @ManyToMany(cascade = { CascadeType.PERSIST })
    @JoinTable(
            name = "stops_routes",
            joinColumns = { @JoinColumn(name = "stop_id") },
            inverseJoinColumns = { @JoinColumn(name = "route_id") }
    )
    private List<Route> routes;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routeList) {
        routes = routeList;
    }
}
