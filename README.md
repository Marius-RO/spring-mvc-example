# Simple eCommerce application built using Spring MVC

## Description

It is a simple web application (online shop) that contains CRUD functionalities and was created in order to practice my skills related to the **Spring Framework**. It was built using the MVC architecture and contains several roles for users.

**User roles and related functionalities:**

* **Visitor** (does not actually exist and it is the basic role that everyone who browses the application without being logged in has)
  * can view/search/filter products 
  * can add products to the shopping cart
* **Customer**
  * can do everything a **Visitor** does
  * can place orders
  * can view its orders
  * can update his profile info
* **Employee**
  * can view/search/filter products, but does **NOT** have access to shopping cart
  * can add/modify/delete products
  * can view all orders
  * can update his profile info
* **Admin**
  * can do everything an **Employee** does
  * can create/delete **Employee** accounts
  * can see employees activity
  * can add/delete categories

## Used technologies
* **Java**
* **HTML / CSS / JavaScript / AJAX / Bootstrap** (for the front-side of view pages)
* **JSP / JSTL** (for the server-side side of view pages)
* **MySQL** (for data storage)
* **Spring Core** (for dependency injection)
* **Spring MVC** (for building the actual MVC application)
* **Spring Data JDBC** (for data storage in a MySQL database)
* **Spring Security** (for user authentication and authorization)
* **Docker** (for the containerization of the application) 


## Installation


## Demo (only the specific functionalities of each role will be presented)

### Visitor

### Customer

### Employee

### Admin
