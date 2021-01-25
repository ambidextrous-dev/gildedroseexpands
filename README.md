**The Gilded Rose Expands** 

This application supports the following endpoints:

1) **ListInventoryItems** - (GET) /v1/items 
    * This endpoint provides a list of all the items in the inventory with respective details
    * Unsecured Endpoint
2) **GetItem** - (GET) /v1/item/{itemName}
    * This endpoint provides details about a specific item
    * Unsecured Endpoint
3) **PurchaseItem** - (POST) /v1/buy
    * This endpoint allows the user the buy an item in the specified quantity
    * Secured Endpoint
    
**How to Build/Run The Application**

Use Maven to build the application from command prompt
`mvn clean install`

Run the application from command prompt `mvn spring-boot:run`

**How to Use The Application**

1. Open Postman
2. Create a following request to list Inventory Items:
   `GET localhost:8080/v1/items` and press Send. The list of inventory items will be displayed in the response section. Does not require any request parameter/object.
   
   Sample Response: 
   
   `[
        {
            "name": "Irish Ale",
            "description": "Ale from Ireland",
            "price": 11,
            "inventory": {
                "name": "Irish Ale",
                "quantity": 110
            }
        },
        {
            "name": "Bread",
            "description": "Fresh Baked Bread",
            "price": 3.60,
            "inventory": {
                "name": "Bread",
                "quantity": 5
            }
        }
    ]`
   
   
3. Create a following request to list Inventory Items:
    `GET localhost:8080/v1/item/Bread` and press Send. The item Bread and all its details will be displayed in the response section. Requires the item name as the path variable.
    
    
    Sample Response:
   
    `{
         "name": "Bread",
         "description": "Fresh Baked Bread",
         "price": 3.60,
         "inventory": {
             "name": "Bread",
             "quantity": 5
         }
     }`
    
4. Create a following request to purchase an item: 
       `POST localhost:8080/v1/buy and press Send. A request to purchase the item will be created, and the response will be available in the response section.
       
   As this endpoint is secured, it will require basic auth credentials as part of the header. Please use Authorization tab in the Postman to add credentials.
   In addition, this request also expects a request body. Make sure to add that in the body section.
   
   Sample Request:
   
   `{   "itemName" : "Bread",
       "quantity" : 2
   }`
   
   Sample Response:
   
   `{
        "orderId": "a9324f08-96b7-4424-bdd3-e915cd293bdb",
        "itemName": "Bread",
        "quantity": 2,
        "orderStatus": "PENDING"
    }`
   

**Few Notes about this Application** 

1. This application is built using Java and Spring Boot. It utilizes an in-memory database for simplicity. I avoided the usage of any other dependencies/tools.
2. This application is built using MVC(Model-View-Controller) pattern. This pattern provides a good separation of the layers which in turn lead to increased flexibility, code maintainability and independent testing.
3. For authentication, I chose Basic Authentication through Spring Security as it is simple to implement. However, as it is not secure, in the future, I would like to secure it with SSL or use a token based authentication system depending on the requirements.
4. For data format, I used JSON as it is simple, easy to use, readable, lightweight and is supported by most browsers.
5. Data for testing is being created through resources/data.sql file. Ths file could be edited in order to add more data.
6. Jacoco plugin is used for test coverage.
7. There is no functionality to cancel the orders.
8. Once an order is successful, the user is provided with the OrderResponse which contains the order details. This response is also being saved in a database table as a reference for all the orders. A unique orderId is being generated for every order.

Future Enhancements:
This project could be improved as most of the projects could. I took the design decisions in order to maintain code quality and development speed. 
If given more time, I would enhance this project in the following ways:

* Use Swagger to add API documentation
* Use tools like Lombok to remove boilerplate code
* Improve Security of the endpoint - Add SSL to Basic Authentication or move to another mechanism (Form Based / Token Based)
* Improve testing
* Add more functionality such as payment support

Surge Pricing:

* This application implements Surge pricing through an in memory ConcurrentHashMap. 
* Every time a user views a specific item, the hashmap is updated for that entry. If the hashMap entry exceeds the threshold, then surgePricing is enabled. 
* When a user sends a request to purchase an item, a request is made to get the latest price. If the price surge is in effect, the price is multiplied with a configurable surge multiplier, and the user is billed for that amount.
* When a user requests an item, the application checks the latest price and provides that in the item details.
* A scheduled task is configured to run every hour (configurable), to clear the view counter and disable the surgePricing if in effect. A new counter will start for the new hour.