package unlp.info.bd2.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Stop {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255)
    private String description;

    @ManyToMany(mappedBy = "stops", cascade = {}, fetch = FetchType.EAGER)
    private List<Route> routes;

    public Stop() {
    }

    public Stop(String name, String description) {
        this.name = name;
        this.description = description;
        this.routes = List.of();
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
