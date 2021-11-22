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
* **Spring Data JDBC** (for database communication/operations)
* **Spring Security** (for user authentication and authorization)
* **Docker** (for the containerization of the application) 


## Installation and Getting Started

### **1. Prerequisites:** 
* [Docker](https://docs.docker.com/get-docker/)

### **2. Installing**
```
$ git clone https://github.com/Marius-RO/spring-mvc-example.git
$ cd spring-mvc-example/install
$ docker compose -f app-compose.yaml up
```
### **3. Run the app**

* After the containers are started app can be accessed at http://localhost:8888

* By default this application will have already some demo data and some demo role-based accounts:
  * **Customers:** customer_1.demo@yahoo.com and customer_2.demo@gmail.com
  * **Employees:** employee_1.demo@yahoo.com and employee_2.demo@gmail.com
  * **Admin:** admin.demo@gmail.com
 
* Password is **123456** for every account.

### **4. Stop the app**

* Make sure you are in the right directory **spring-mvc-example/install** in order to use the following command.

```
$ docker compose -f app-compose.yaml down
```

---
## Demo (only the specific functionalities of each role will be presented)

* ### Visitor

https://user-images.githubusercontent.com/37272520/142882445-130136d5-f3b5-4c8a-b4c0-18e5323eb983.mp4

* ### Customer

https://user-images.githubusercontent.com/37272520/142882507-e6ffb20c-380a-4dd5-b5e3-38cfd27b61c5.mp4

* ### Employee

https://user-images.githubusercontent.com/37272520/142882553-47a14327-659a-4929-abed-5a92e89895a5.mp4

* ### Admin

https://user-images.githubusercontent.com/37272520/142882610-76b2b017-23b3-47ea-a496-3911d7447f5b.mp4

---
## Reporting Issues

* New issues can be created [here](https://github.com/Marius-RO/spring-mvc-example/issues/new).
* If you want to report a bug, please provide as much information as possible with the issue report.