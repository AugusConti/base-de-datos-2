package unlp.info.bd2.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(unique = true, updatable = false, nullable = false, length = 255)
    private String code;

    @Column(nullable = false, precision = 2)
    private float totalPrice;

    @Column(nullable = false)
    private Date date;

    @ManyToOne(optional = false, cascade = {}, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
    @JoinColumn(name = "route_id")
    private Route route;

    @OneToOne(optional = true, mappedBy = "purchase", cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, fetch = FetchType.EAGER)
    private Review review;

    @OneToMany(mappedBy = "purchase", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.LAZY)
    private List<ItemService> itemServiceList;

    public Purchase() {
    }

    public Purchase(String code, Date date, User user, Route route) {
        this.code = code;
        this.totalPrice = route.getPrice();
        this.date = date;
        this.user = user;
        this.route = route;
        this.review = null;
        this.itemServiceList = List.of();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public List<ItemService> getItemServiceList() {
        return itemServiceList;
    }

    public void setItemServiceList(List<ItemService> itemServiceList) {
        this.itemServiceList = itemServiceList;
    }

    // El precio debería ser el del service
    public void addItem(ItemService item, float price) {
        this.itemServiceList.add(item);
        this.totalPrice += price;
    }

    public Review addReview(int rating, String comment) {
        Review r = new Review(rating, comment, this);
        this.review = r;
        return r;
    }
}
