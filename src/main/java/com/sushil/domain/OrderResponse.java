package com.sushil.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "purchaseOrder")
public class OrderResponse {

    @Id
    @Column(name = "orderId", nullable = false, unique = true)
    private String orderId;

    @Column(name = "itemName", nullable = false)
    private String itemName;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "orderStatus", nullable = false)
    private String orderStatus;

    public OrderResponse() {

    }

    public OrderResponse(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.orderId = UUID.randomUUID().toString();
        this.orderStatus = OrderStatus.PENDING.toString();
    }

    public String getOrderId() {
        return orderId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String name) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

}
