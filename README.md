# Big Retail Enhanced

Retail ERP solution for large busnisses designed with scalibility and reliability in mind, includes all basic features needed to run a real retail business, in addition to supporting distributed mode based on message queue so every store can have it's own dedicated server while syncronizing its data automatically with the central server in the background.

## Main feautres:
- Integrated Accounting & Financial Reporting: Journal records are automatically created to reflect business processes to then be used to generate comprehinsive reports in order to help businesses with decision making.
- Complete Procurement Cycle Automation: Being targeted toward retail buninesses, our system covers the entire workflow of precuremnt operations from identifying stores need to the point recieving orders in stores with the help of its integrated warehousing & invoicing modules.
- Warehouse Management: Cycle counts, inventory management, trasportation orders, in addition to being integrated into the core system accounting and invoicing functionalities.
- Invoicing: Create a comprehensive invoices with all the relevant information a business could need to record, with the ability to make a reusable templates for custom use cases as one could see fit.
- Dedicated POS: Feature complete app with ability to track shifts, read barcodes and make sell invoices that easy to use and suited for real work environment. 
- Robust Dashboard & Control Panel: Business visibility and system control achieved by real time data charts and comprehensive role based authurization system.


## Local Development Setup:  
1. Clone the repository:
   ```
   git clone https://github.com/EngOmarDr/graduation-backend.git
   ```
2. Start local server:
   ```
   ./mvnw spring-boot:run 
   ```

### Enable Distributed Mode:
1. Change the address to your kafka cluster address in the **application.properties** file
   ```
   spring.kafka.bootstrap-servers=your.kafka.server:9092 
   ```

2. Run the app with the profile accourding to your need (central-app or pos-app)
   ```
   ./mvnw spring-boot:run -D spring-boot.run.profiles=<pos-app|central-app> 
   ```
