# Case study -  heycar - backend challenge
## Setup
*   JDK 8 or higher version
*   Maven 3.5.X
*   Run application using command line from the project directory - mvn spring-boot:run
*   Application will be up in local port *8080*
*   Local Swagger URL- http://localhost:8080/swagger-ui.html

## 1. Upload CSV file:
*  Use "csv-Controller" service in swagger
*  /api/upload_csv/{dealer_id} - upload CSV File
*  Exception returned for Invalid File upload

## 2. Vehicle Listings:
*  Use "vehicle-listing-controller" service in swagger
*  /api/vehicle_listings - View all available Vehicle Listings
*  /api/vehicle_listings/{dealer_id} - add Vehicles for a dealer
*  /api/vehicle_listings/search - search using the following parameters: make, model, year and color

## Application Features
*   Spring Boot project with Spring Data JPA, Spring Dev tools, H2 database, Lombok, 
JUnit 5 and Swagger
*   Centralised Exception handling using @RestControllerAdvice - please refer the package car.hey.platform.exception
*   Service, Controller and Integration Test cases available

## Dockerized App
* Deploying to Docker Hub With Jib - Run mvn compile com.google.cloud.tools:jib-maven-plugin:0.9.10:build -Dimage=shijinraj/platform-app Or mvn compile jib:build
* Run application locally - docker run -p 127.0.0.1:8080:8080/tcp shijinraj/platform-app
* http://localhost:8080/swagger-ui.html