import java.sql.*;
import java.io.*;
import java.util.*;


public class GeneratePastOrders {
    public static void main(String args[]) {
        /* Notes and Assumptions:
        *    - Open 6 days per week, and 2 days are reserved for festivals
        */

        // [[id, quantity], ...]

        double randomFoodID;
        double randomMass;
        
        // Generates a single random order        
        Random rand = new Random();
        int numOfSaleLines = rand.nextInt(10) + 1;
        double[][] order = new double[numOfSaleLines][2];

        for (int i = 0; i < numOfSaleLines; i++) {
            // Generate an ID between 1 and 55
            // Generate a mass between 0.25 and 3.75
            randomFoodID = (double)(rand.nextInt(55) + 1);
            randomMass = 3.5 * rand.nextDouble() + 0.25;
            
            // some typecasting bullshit
            randomMass = ( (double) ((int)(randomMass * 100) ) ) / 100;

            order[i][0] = randomFoodID;
            order[i][1] = randomMass;

            System.out.println("[" + order[i][0] + ", " + order[i][1] + "]");

            // Query for unit price given the foodID
            // lineTotal = unitPrice * randomMass
        }

        

    }
}
