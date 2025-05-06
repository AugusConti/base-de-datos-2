package unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, precision = 2)
    private float price;

    @Column(nullable = false, length = 255)
    private String description;

    @OneToMany(mappedBy = "service", cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY)
    private List<ItemService> itemServiceList;

    @ManyToOne(optional = false, cascade = {}, fetch = FetchType.EAGER)
    private Supplier supplier;

    public Service() {
    }

    public Service(String name, float price, String description, Supplier supplier) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.itemServiceList = new ArrayList<>();
        this.supplier = supplier;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ItemService> getItemServiceList() {
        return itemServiceList;
    }

    public void setItemServiceList(List<ItemService> itemServiceList) {
        this.itemServiceList = itemServiceList;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public void addItem(ItemService item) {
        this.itemServiceList.add(item);
    }
}
