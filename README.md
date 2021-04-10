# Furniture Inventory Management Application

> **Authors**
- Brenden Dielissen
- Maria Martine Baclig
- Nafisa Tabassum
- Ronn Delos Reyes

> **Description**

This application calculates the cheapest combination of available inventory items that can be used to fill a specific order. It takes in user input for:
1. a furniture category
2. its type, and 
3. the number of items requested

After an order request is received, it calculates and outputs the cheapest option for creating the requested pieces of furniture or specifies if the request is not possible to fill. If the request can be filled, an order form in a `.txt` format is produced. The order form includes the original request and the list of desired components, as well as the total price. When a successful order is placed, the database is updated to reflect selected items that are no longer available in inventory.

If a request cannot be filled, the names of suggested manufacturers are included in the outputted order form instead.

## How to Use It

> **Linux/Mac OS X**
```bash
$ git clone https://github.com/BrendenDielissen-uofc/ENSF409-Project.git
$ cd ENSF409-Project/src
$ javac edu/ucalgary/ensf409/OrderForm.java
$ java edu.ucalgary.ensf409.OrderForm
```
> **Windows**
```cmd
git clone https://github.com/BrendenDielissen-uofc/ENSF409-Project.git
cd ENSF409-Project\src
javac edu\ucalgary\ensf409\OrderForm.java
java edu.ucalgary.ensf409.OrderForm
```
## Code-base Structure

The project is coded using a simple and intuitive structure presented below:
```bash
< ENSF409-Project >
  ├── inventory.sql
  ├── README.md
  └── src
      ├── edu
      │   └── ucalgary
      │       └── ensf409
      │           ├── Chair.java
      │           ├── Desk.java
      │           ├── Filing.java
      │           ├── Furniture.java
      │           ├── Inventory.java
      │           ├── Lamp.java
      │           ├── Manufacturer.java
      │           ├── OrderForm.java
      │           └── OrderFormTest.java
      ├── lib
      |   ├── hamcrest-core-1.3.jar
      |   ├── junit-4.13.2.jar
      |   ├── mysql-connector-java-8.0.23.jar
      |   └── system-rules-1.19.0.jar
      |
      └── orderform.txt
```
## How to Run Unit Tests
The unit tests can be run in any order, but we have used the `@FixMethodOrder` annotation to run the tests by ascending name

> **Linux/Mac OS X**
```bash
# .jar files are located in ENSF409-Project/lib
# Replace .XX with your respective JUnit driver version number
$ cd ENSF409-Project/src
$ javac -cp .:junit-4.XX.jar:hamcrest-core-1.3.jar edu/ucalgary/ensf409/OrderFormTest.java
$ java -cp .:junit-4.XX.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore edu.ucalgary.ensf409.OrderFormTest
```
> **Windows**
```cmd
# .jar files are located in ENSF409-Project\lib
# Replace .XX with your respective JUnit driver version number
cd ENSF409-Project\src
javac -cp .;junit-4.XX.jar;hamcrest-core-1.3.jar edu\ucalgary\ensf409\OrderFormTest.java
java -cp .;junit-4.XX.jar;hamcrest-core-1.3.jar org.junit.runner.JUnitCore edu.ucalgary.ensf409.OrderFormTest
```
