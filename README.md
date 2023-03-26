eCommerce Platform is an application for managing products.

At first you don't have any products in the database. You can add products threw the swagger ui.
Once you have products in the database you can use the following endpoints to manage them:

- GET /v1/products - returns all products based on pagination and ordering
- POST /v1/products - creates a new product
- PUT /v1/products/{id} - updates product with the given id and its details
- DELETE /v1/products/{id} - deletes product with the given id

additionally you can use the following endpoints to see different categories of products and their quantities:

- GET /v1/products/categories - returns all distinct categories and the number of products in each category

additionally you can use the following endpoints to order products:

- POST /v1/products/{id}/order/{quantity} - orders the product with the given id and the given quantity while locking the
  product in the database until the transaction is committed or rolled back

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Used technologies

Java 16
Oracle database 19c


----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

1. Database installation :

Install Oracle database 19c on your machine.

Login to the database using sqlplus as administrator.
sqlplus / as sysdba

Create a user and give him all privileges with the following commands:

CREATE USER username IDENTIFIED BY password;
GRANT ALL PRIVILEGES TO username;

Configure your database connection in application.properties file.

------------liquibase----------------

You can install the oracle db threw liquibase plugin manually.
by configure in the pom.xml in liquibase-maven-plugin the database url and credentials
and then use the following command to install the database :

mvn liquibase:update

//if you don't do that then the first start of the application will create the database for you.

---- if you want to delete the database use the following command :

mvn liquibase:dropAll


----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


2. Build the project with maven :

mvn clean install

--it will run the unit tests

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


3. Run the spring boot application and access the swagger ui from the following url :

http://localhost:8086/swagger-ui/index.html

----or you can interact with rest services using curl queries :

-- create product :

curl -X POST -H "Content-Type: application/json" -d '{"name": "HP 10","description": "a hp 10 laptop","category": "
Laptop","quantity": 10}' http://localhost:8086/v1/products

-- for updating :

curl -X PUT -H "Content-Type: application/json" -d '{"id": 12, "name": "HP 10","description": "a hp 10 laptop","
category": "Laptop","quantity": 800}' http://localhost:8086/v1/products/{id}

-- for deleting product with id :

curl -X DELETE -H "Content-Type: application/json" "http://localhost:8086/v1/products/{id}"

-- for selecting based on sorting and pagination:

curl -X GET "http://localhost:8086/v1/products?orderBy=CATEGORY&direction=ASC&page=0&pageSize=2"

etc.




----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Docker setup:


Run this command from root of the project to build image:

```console
docker build --build-arg VERSION=0.0.1-SNAPSHOT -t ecommerce-platform-image .
```


run image on container:

```console
docker run --name ecommerce-platform-service -p 8086:8086 ecommerce-platform-image
```


Docker compose setup:

1. Create images for : eureka-discovery-server, ecommerce-back-office-service and ecommerce-platform-service from their Dockerfiles.
 

2. Run the following command from root of the project to run the containers:

```console
docker-compose up
```

The command will run the following containers:

- ecommerce-eureka-discovery-server
- ecommerce-back-office-service
- ecommerce-platform-service
- ecommerce-platform-database-container
- ecommerce-back-office-database-container

For the database containers you need to configure the volumes in the docker-compose.yml file.
If you setup the databases for the first time, you need to run the liquibase migrations for each container.
The containers share the same network, so they can communicate with each other.


To stop the containers use the following command:

```console
docker-compose down
```


----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
