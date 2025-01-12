# Material Rental Platform

## Project Description
A training institution in the arts wants to develop a platform where students can reserve and rent materials for their projects and final works. The materials range from lamps, stage elements, or light panels to small necessities such as cables. As a proof of concept, it is sufficient to keep track of about ten diverse devices and accessories.

Through this application, registered students should be able to choose their required materials. They do this by adding them to their account for a specific period and receive a confirmation. It helps users to have a decent search function or catalog for this purpose.

We were tasked with building a web application as a proof of concept where customers can use it to rent devices from a limited catalog. Important features include the ability to fill a shopping cart and consult a catalog where devices and accessories from different categories can be found. Other useful extras are also considered but only if all core functionality works.

For the front-end, there were no specific requirements, but the back-end must be made within a Java (Spring) environment as their ICT staff has experience with it. For major issues, David Van Steertegem has been hired as a consultant. However, the budget only allows for him to be called in once, more costs 10% of the revenue.

## Installation and Running the Application

### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Apache Maven: Version 3.8.1 or higher
- Node.js and npm Version 16.x or higher (for front-end dependencies)
- MySQL Version 8.0 or compatible

### Steps to Install and Run

1. **Clone the Repository**
   ```bash
   [git clone https://github.com/Lucaswillaert/material-rental-platform.git](https://github.com/Lucaswillaert/EnterpriseApplicationLucasWillaert)
   cd EnterpriseApplicationLucasWillaert
    ```

2. **Set Up the Database**
    - Create a new database in MySQL
    - Update the database connection details in `src/main/resources/application.properties`

spring.application.name=EELucasWillaert
spring.datasource.url=jdbc:mysql://localhost:3306/EnterpriseApp
spring.datasource.username=root
spring.datasource.password=password

3. **Build the Application**
   ```bash
   mvn clean install
   ```
4. **Run the Application**
    ```bash
   mvn spring-boot:run
    ```

5. **Access the Application**
The application can be accessed at `http://localhost:9090`


**Front-End Dependencies**
navigate to the frontend directory and install with:
```bash
cd src/main/resources/static
npm install
```

### IntelliJ IDEA Setup
If you are using IntelliJ IDEA, follow these steps to set up the project:

1. Open IntelliJ and click **File > Open**. Select the root directory of the project (`material-rental-platform`).
2. IntelliJ will automatically detect it as a Maven project and load dependencies. If not, click on **Reload All Maven Projects**.
3. Ensure the correct SDK is selected:
   - Go to **File > Project Structure > SDK**.
   - Select **JDK 17** or add it if missing.
4. Configure the `application.properties` file with the correct database credentials.
5. Run the project:
   - Navigate to **Run > Edit Configurations**.
   - Add a new configuration for Spring Boot and select the main class: 
     `be.rungroup.eelucaswillaert.EeLucasWillaertApplication`.
   - Click **Run**.



**Sources Used**
- **Spring Boot**: For building the back-end services - https://docs.spring.io/spring-boot/index.html 
- **Thymeleaf**: For server-side rendering of HTML - https://ultraq.github.io/thymeleaf-layout-dialect/ 
- **Bootstrap**: For styling the front-end - https://getbootstrap.com/docs/5.3/getting-started/introduction/ 
- **Youtube** : For tutorials on how to use Spring Boot and Thymeleaf -
    - https://youtube.com/playlist?list=PL82C6-O4XrHejlASdecIsroNEbZFYo_X1&si=nwl-ooRJyD2dPbV6 (deze volledige playlist)
    - https://www.youtube.com/watch?v=31KTdfRH6nY&t=3876s
    - https://www.youtube.com/watch?v=LSEYdU8Dp9Y&t=211s
    - https://www.youtube.com/watch?v=aS9SQITRocc
    - https://www.youtube.com/playlist?list=PLR2yPNIFMlL-33mG4qF8dkq0R4q5887Rq
 

### Demo Video

Below is the demo video where I showcase the project. The implemented functionalities follow the provided list on canvas:

- A registration system for users.
- A secure login system.
- The ability to display all products in a catalog.
- The ability to display products from specific categories (cables, lighting, control panels), with relevant filters.
- Adding products to a shopping cart.
- Confirming purchases (checkout system, limited to a confirmation page).

  
https://github.com/user-attachments/assets/4adbee6f-1529-49ef-9f73-543e4ed72942

Additionally, I have added a few extra features, including:
- An admin board.
- The ability to add new materials.
- A data loader, which includes admin credentials to log in and test the features yourself.


For further questions, I am always available on Teams! 
