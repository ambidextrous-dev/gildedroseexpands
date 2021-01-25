package com.sushil.repository;

//This class will be used to store all sql queries in one place for easier maintenance
public class SQLQueries {

    public static final String updateInventoryQuery = "update Inventory inv set inv.quantity = :quantity where inv.name = :name";

}
