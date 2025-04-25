package unlp.info.bd2.model;

import jakarta.persistence.*;

@Entity
public class ItemService {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private int quantity;

    @ManyToOne(optional = false)
    private Purchase purchase;

    @ManyToOne(optional = false)
    private Service service;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
