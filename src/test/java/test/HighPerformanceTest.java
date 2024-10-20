package test;

import com.hbrs.Main;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.hbrs.ia.model.SalesMan;
import de.hbrs.ia.model.SocialPerformanceRecord;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HighPerformanceTest {
    // login details via env (Run Configurations -> Edit -> Environment variables)
    private final String DB_USER = System.getenv("DB_USER");
    private final String DB_PASS = System.getenv("DB_PASS");

    //private MongoClient client;
    //private MongoDatabase supermongo;
    private MongoCollection<Document> salesmen;
    private MongoCollection<Document> reports;
    private Main management;

    /**
     * Attention: You might update the version of the Driver
     * for newer version of MongoDB!
     * This tests run with MongoDB 4.2.17 Community
     */
    @BeforeEach
    void setUp() {
        // Setting up the connection to a local MongoDB with standard port 27017
        // must be started within a terminal with command 'mongod'.
        //client = new MongoClient("localhost", 27017);

        Main.connectDB();

        // Get database 'highperformance' (creates one if not available)
        //supermongo = client.getDatabase("highperformanceNewTest");

        salesmen = Main.salesmenDB;
        reports = Main.reportsDB;
        management = new Main();

        // Get Collection 'salesmen' (creates one if not available)
        //salesmen = supermongo.getCollection("salesmen");
    }

    @Test
    void insertSalesMan() {
        // CREATE (Storing) the salesman object
        Document document = new Document();
        document.append("firstname", "Sascha");
        document.append("lastname", "Alda");
        document.append("sid", 90133);

        // ... now storing the object
        salesmen.insertOne(document);

        // READ (Finding) the stored Documnent
        Document newDocument = this.salesmen.find().first();
        System.out.println("Printing the object (JSON): " + newDocument);

        // Assertion
        Integer sid = (Integer) newDocument.get("sid");
        assertEquals(90133, sid);

        // Deletion
        salesmen.drop();
    }

    @Test
    void insertSalesManMoreObjectOriented() {
        // CREATE (Storing) the salesman business object
        // Using setter instead
        SalesMan salesMan = new SalesMan("Leslie", "Malton", 90444);

        // ... now storing the object
        salesmen.insertOne(salesMan.toDocument());

        // READ (Finding) the stored Documnent
        // Mapping Document to business object would be fine...
        Document newDocument = this.salesmen.find().first();
        System.out.println("Printing the object (JSON): " + newDocument);

        // Assertion
        Integer sid = (Integer) newDocument.get("sid");
        assertEquals(90444, sid);

        // Deletion
        salesmen.drop();
    }

    @Test
    void createSalesManTest() {
        // create example salesman
        SalesMan salesman = new SalesMan("Random", "Person", 3947);
        // add salesman to database
        management.createSalesMan(salesman);
        // check correctness
        Document lastDocument = salesmen.find().first();
        System.out.println("Salesman: " + lastDocument);
        assertEquals(3947, (Integer) lastDocument.get("sid"), "Salesman ID wrong");
        assertEquals("Random", lastDocument.get("firstname"), "Salesman first name wrong");
        assertEquals("Person", lastDocument.get("lastname"), "Salesman last name wrong");
        // cleanup
        salesmen.drop();
    }

    @Test
    void addSocialPerformanceRecordTest() {
        // create salesman
        SalesMan salesman = new SalesMan("Random", "Person", 3947);
        // create report for Salesman
        SocialPerformanceRecord record = new SocialPerformanceRecord(salesman, 1000, 0,
                0, 0, 0, 0, 0);

        // add salesman and report to database
        management.createSalesMan(salesman);
        management.addSocialPerformanceRecord(record, salesman);

        // check for correctness
        Document lastDocument = salesmen.find().first();
        System.out.println("Record: " + lastDocument);
        assertEquals(salesman, (SalesMan) lastDocument.get("salesMan"), "Wrong Salesman");
        assertEquals(1000, (Integer) lastDocument.get("year"), "Wrong year");
        assertEquals(0, (Integer) lastDocument.get("leaderShipCompetence"), "Wrong leadership competence");
        assertEquals(0, (Integer) lastDocument.get("openessToEmployee"), "Wrong openess");
        assertEquals(0, (Integer) lastDocument.get("socialBehaviourToEmployee"), "Wrong behaviour");
        assertEquals(0, (Integer) lastDocument.get("attiudeTowardsClient"), "Wrong attidue");
        assertEquals(0, (Integer) lastDocument.get("communicationSkills"), "Wrong skills");
        assertEquals(0, (Integer) lastDocument.get("integrityToCompany"), "Wrong integrity");

        //cleanup
        salesmen.drop();
    }

    @Test
    void readSalesManTest() {
        // create example salesman
        SalesMan salesman = new SalesMan("Random", "Person", 3947);
        // add salesman to database
        salesmen.insertOne(salesman.toDocument());
        // check correctness
        SalesMan result = management.readSalesMan(3947);
        assertEquals(3947, result.getId(), "Salesman ID wrong");
        assertEquals("Random", result.getFirstname(), "Salesman first name wrong");
        assertEquals("Person", result.getLastname(), "Salesman last name wrong");
        // cleanup
        salesmen.drop();
    }

    @Test
    void readAllSalesMenTest() {
        SalesMan salesMan1 = new SalesMan("Random", "Person", 3947);
        SalesMan salesMan2 = new SalesMan("Zufällig", "Mensch", 1234);
        SalesMan salesMan3 = new SalesMan("Kai", "Bühner", 1004);
        SalesMan salesMan4 = new SalesMan("Jan", "Kaupp", 1000);
        SalesMan salesMan5 = new SalesMan("David", "Olbertz", 5879);

        salesmen.insertOne(salesMan1.toDocument());
        salesmen.insertOne(salesMan2.toDocument());
        salesmen.insertOne(salesMan3.toDocument());
        salesmen.insertOne(salesMan4.toDocument());
        salesmen.insertOne(salesMan5.toDocument());

        ArrayList<SalesMan> salesManArrayList = new ArrayList<>();
        salesManArrayList.add(salesMan1);
        salesManArrayList.add(salesMan2);
        salesManArrayList.add(salesMan3);
        salesManArrayList.add(salesMan4);
        salesManArrayList.add(salesMan5);

        //assertArrayEquals(salesManArrayList, management.readAllSalesMen());
    }

    @Test
    void readSocialPerformanceRecordTest() {

    }

    @Test
    void deleteSalesManTest() {
        // create example salesman
        SalesMan salesman = new SalesMan("Random", "Person", 3947);
        // add salesman to database
        salesmen.insertOne(salesman.toDocument());
        // check correctness
        management.deleteSalesMan(3947);
        assertNull(salesmen.find().first(), "Salesman is still present inside database");
    }

    @Test
    void deleteSocialPerformanceRecord() {

    }

}