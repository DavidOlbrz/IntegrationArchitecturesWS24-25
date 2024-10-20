package test;

import de.hbrs.ia.code.Manage;
import com.mongodb.client.MongoCollection;
import de.hbrs.ia.model.SalesMan;
import de.hbrs.ia.model.SocialPerformanceRecord;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.*;

class HighPerformanceTest {
    // login details via env (Run Configurations -> Edit -> Environment variables)
    private final String DB_USER = System.getenv("DB_USER");
    private final String DB_PASS = System.getenv("DB_PASS");

    private MongoCollection<Document> salesmen;
    private MongoCollection<Document> reports;
    private Manage management;

    /**
     * Attention: You might update the version of the Driver
     * for newer version of MongoDB!
     * This tests run with MongoDB 4.2.17 Community
     */
    @BeforeEach
    void setUp() {
        Manage.connectDB();

        salesmen = Manage.salesmenDB;
        reports = Manage.reportsDB;
        management = new Manage();
    }

    @AfterEach
    void tearDown() {
        salesmen.drop();
        reports.drop();
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
        assert newDocument != null;
        Integer sid = (Integer) newDocument.get("sid");
        assertEquals(90133, sid);
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
        assert newDocument != null;
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
        assert lastDocument != null;
        assertEquals(3947, (Integer) lastDocument.get("sid"), "Salesman ID wrong");
        assertEquals("Random", lastDocument.get("firstname"), "Salesman first name wrong");
        assertEquals("Person", lastDocument.get("lastname"), "Salesman last name wrong");
    }

    @Test
    void addSocialPerformanceRecordTest() {
        // create salesman
        SalesMan salesman = new SalesMan("Random", "Person", 3947);
        // create report for Salesman
        SocialPerformanceRecord record = new SocialPerformanceRecord(1000, 0,
                0, 0, 0, 0, 0);

        // add salesman and report to database
        management.createSalesMan(salesman);
        management.addSocialPerformanceRecord(record, salesman);

        // check for correctness
        Document lastDocument = reports.find().first();
        System.out.println("Record: " + lastDocument);
        assert lastDocument != null;
        assertEquals(3947, lastDocument.getInteger("SID"), "Wrong Salesman");
        assertEquals(1000, (Integer) lastDocument.get("year"), "Wrong year");
        assertEquals(0, (Integer) lastDocument.get("leaderShipCompetence"), "Wrong leadership competence");
        assertEquals(0, (Integer) lastDocument.get("opennessToEmployee"), "Wrong openess");
        assertEquals(0, (Integer) lastDocument.get("socialBehaviourToEmployee"), "Wrong behaviour");
        assertEquals(0, (Integer) lastDocument.get("attitudeTowardsClient"), "Wrong attidue");
        assertEquals(0, (Integer) lastDocument.get("communicationSkills"), "Wrong skills");
        assertEquals(0, (Integer) lastDocument.get("integrityToCompany"), "Wrong integrity");
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
    }

    @Test
    void readAllSalesMenTest() {
        SalesMan salesMan1 = new SalesMan("Random", "Person", 3947);
        SalesMan salesMan2 = new SalesMan("Zufällig", "Mensch", 1234);
        SalesMan salesMan3 = new SalesMan("Kai", "Bühner", 1004);
        SalesMan salesMan4 = new SalesMan("Jan", "Kaupp", 1000);
        SalesMan salesMan5 = new SalesMan("David", "Olbertz", 5879);

        List<Integer> IDs = new ArrayList<>();
        IDs.add(salesMan1.getId());
        IDs.add(salesMan2.getId());
        IDs.add(salesMan3.getId());
        IDs.add(salesMan4.getId());
        IDs.add(salesMan5.getId());

        salesmen.insertOne(salesMan1.toDocument());
        salesmen.insertOne(salesMan2.toDocument());
        salesmen.insertOne(salesMan3.toDocument());
        salesmen.insertOne(salesMan4.toDocument());
        salesmen.insertOne(salesMan5.toDocument());

        List<SalesMan> results = management.readAllSalesMen();
        List<Integer> resultIDs = new ArrayList<>();

        for (SalesMan salesMan : results) {
            resultIDs.add(salesMan.getId());
        }

        assertTrue(resultIDs.containsAll(IDs));
    }

    @Test
    void readSocialPerformanceRecordTest() {
        //create a Stupa Salesman
        SalesMan salesMan1 = new SalesMan("Kai", "Bühner", 1004);
        SalesMan salesMan2 = new SalesMan("Stephan", "Schwabenman", 1004);


        // create performance records for the Stupa Salesman
        SocialPerformanceRecord record1 = new SocialPerformanceRecord(2024, 2,
                3, 4, 5, 1, 5);
        SocialPerformanceRecord record2 = new SocialPerformanceRecord(2023, 1,
                2, 3, 4, 5, 6);
        SocialPerformanceRecord record3 = new SocialPerformanceRecord(2022, 2,
                3, 4, 2, 1, 3);

        SocialPerformanceRecord record4 = new SocialPerformanceRecord(2021, 5,
                4, 5, 6, 2, 6);
        SocialPerformanceRecord record5 = new SocialPerformanceRecord(2020, 1,
                3, 7, 7, 2, 1);

        // adding the stupasalesmen to the database
        salesmen.insertOne(salesMan1.toDocument());
        salesmen.insertOne(salesMan2.toDocument());
        // adding performance records to database and assigning them to the stupa salesman
        management.addSocialPerformanceRecord(record1, salesMan1);
        management.addSocialPerformanceRecord(record2, salesMan1);
        management.addSocialPerformanceRecord(record3, salesMan1);

        management.addSocialPerformanceRecord(record4, salesMan2);
        management.addSocialPerformanceRecord(record5, salesMan2);

        List<Integer> resultIDs = new ArrayList<>();

        List<Integer> years = new ArrayList<>();
        years.add(2022);
        years.add(2023);
        years.add(2024);

        resultIDs.add(record1.getYear());
        resultIDs.add(record2.getYear());
        resultIDs.add(record3.getYear());

        assertTrue(resultIDs.containsAll(years));
        assertFalse(resultIDs.contains(2020) || resultIDs.contains(2021));
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
        // create example salesman & record
        SalesMan salesman = new SalesMan("Random", "Person", 3947);
        SocialPerformanceRecord record = new SocialPerformanceRecord(1000, 0,
                0, 0, 0, 0, 0);
        // add salesman to database
        salesmen.insertOne(salesman.toDocument());
        management.addSocialPerformanceRecord(record, salesman);
        // check correctness
        management.deleteSocialPerformanceRecord(salesman, 1000);
        assertNull(
                reports.find(and(
                        eq("SID", salesman.getId()),
                        eq("year", record.getYear())
                )).first(),
                "Social Performance Record is still present inside database"
        );
    }

}