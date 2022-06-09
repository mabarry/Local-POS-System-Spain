DROP TABLE FoodItemsTest;
/*DROP TABLE CustomerSubTotalLineTest;*/

CREATE TABLE FoodItemsTest (
    foodID INT PRIMARY KEY,
    
    foodName VARCHAR NOT NULL,
    
    unitPrice FLOAT NOT NULL,
    CHECK(unitPrice > 0)
);


/*CREATE TABLE CustomerOrder (
    
);*/


CREATE TABLE CustomerSaleLineTest (
    lineID INT,
    orderID INT,
    foodID INT,
    linePrice FLOAT,
    lineQuantity FLOAT,
    
    PRIMARY KEY (lineID),
    
    CHECK (linePrice > 0 AND line lineQuantity > 0)
);








