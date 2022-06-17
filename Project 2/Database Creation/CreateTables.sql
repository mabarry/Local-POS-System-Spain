CREATE TABLE FoodItems (
    foodID INT,
    foodName VARCHAR(255),
    unitPrice FLOAT,
    foodQuantity FLOAT,
    storageType VARCHAR(255),
    
    PRIMARY KEY (foodID),
    
    CHECK(unitPrice > 0)
);


CREATE TABLE EmployeeList (
    employeeID INT,
    employeeName VARCHAR(255) NOT NULL,
    canChangePrice BOOLEAN NOT NULL,
    canOrderVendor BOOLEAN NOT NULL,
    
    PRIMARY KEY (employeeID)
);


CREATE TABLE CustomerOrder ( 
    customerOrderID INT,
    customerOrderDate DATE,
    customerOrderTotal FLOAT,
    paymentMethod VARCHAR(255) NOT NULL,
    employeeID INT,
    
    PRIMARY KEY (customerOrderID),

    FOREIGN KEY (employeeID) REFERENCES EmployeeList(employeeID),

    CHECK (customerOrderTotal >= 0)
);


CREATE TABLE VendorOrder (
    vendorOrderID INT,
    vendorName VARCHAR(255),
    vendorOrderDate DATE,
    vendorOrderTotal FLOAT,
    employeeID INT,

    PRIMARY KEY (vendorOrderID),

    FOREIGN KEY (employeeID) REFERENCES EmployeeList(employeeID)
);


CREATE TABLE CustomerSaleLine (
    saleLineID INT,
    customerOrderID INT,
    foodID INT,
    saleLinePrice FLOAT,
    saleLineQuantity FLOAT,
    
    PRIMARY KEY (saleLineID),

    FOREIGN KEY (customerOrderID) REFERENCES CustomerOrder(customerOrderID),
    FOREIGN KEY (foodID) REFERENCES FoodItems(foodID),

    CHECK ((saleLinePrice > 0) AND (saleLineQuantity > 0))
);


CREATE TABLE VendorBuyLine (
    vendorLineID INT,
    vendorOrderID INT,
    foodID INT,
    vendorLinePrice FLOAT,
    vendorLineQuantity FLOAT,

    PRIMARY KEY (vendorLineID),
    
    FOREIGN KEY (vendorOrderID) REFERENCES VendorOrder(vendorOrderID),
    FOREIGN KEY (foodID) REFERENCES FoodItems(foodID)
);


GRANT ALL PRIVILEGES ON FoodItems TO csce315950_kyle;
GRANT ALL PRIVILEGES ON FoodItems TO csce315950_harrison;
GRANT ALL PRIVILEGES ON FoodItems TO csce315950_cameron;
GRANT ALL PRIVILEGES ON FoodItems TO csce315950_matthew;
GRANT ALL PRIVILEGES ON CustomerSaleLine TO csce315950_kyle;
GRANT ALL PRIVILEGES ON CustomerSaleLine TO csce315950_harrison;
GRANT ALL PRIVILEGES ON CustomerSaleLine TO csce315950_cameron;
GRANT ALL PRIVILEGES ON CustomerSaleLine TO csce315950_matthew;
GRANT ALL PRIVILEGES ON CustomerOrder TO csce315950_kyle;
GRANT ALL PRIVILEGES ON CustomerOrder TO csce315950_harrison;
GRANT ALL PRIVILEGES ON CustomerOrder TO csce315950_cameron;
GRANT ALL PRIVILEGES ON CustomerOrder TO csce315950_matthew;
GRANT ALL PRIVILEGES ON EmployeeList TO csce315950_kyle;
GRANT ALL PRIVILEGES ON EmployeeList TO csce315950_harrison;
GRANT ALL PRIVILEGES ON EmployeeList TO csce315950_cameron;
GRANT ALL PRIVILEGES ON EmployeeList TO csce315950_matthew;
GRANT ALL PRIVILEGES ON VendorOrder TO csce315950_kyle;
GRANT ALL PRIVILEGES ON VendorOrder TO csce315950_harrison;
GRANT ALL PRIVILEGES ON VendorOrder TO csce315950_cameron;
GRANT ALL PRIVILEGES ON VendorOrder TO csce315950_matthew;
GRANT ALL PRIVILEGES ON VendorBuyLine TO csce315950_kyle;
GRANT ALL PRIVILEGES ON VendorBuyLine TO csce315950_harrison;
GRANT ALL PRIVILEGES ON VendorBuyLine TO csce315950_cameron;
GRANT ALL PRIVILEGES ON VendorBuyLine TO csce315950_matthew;