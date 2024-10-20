package com.hbrs;


import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import de.hbrs.ia.code.ManagePersonal;
import de.hbrs.ia.model.SalesMan;
import de.hbrs.ia.model.SocialPerformanceRecord;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class Main implements ManagePersonal {
    // login details via env (Run Configurations -> Edit -> Environment variables)
    public static final String DB_USER = System.getenv("DB_USER");
    public static final String DB_PASS = System.getenv("DB_PASS");

    public static MongoCollection<Document> salesmenDB;
    public static MongoCollection<Document> reportsDB;

    public static void main(String[] args) {
        connectDB();
    }

    /**
     * Connects to the external MongoDB database
     */
    public static void connectDB() {
        MongoClient databaseClient = MongoClients
                .create("mongodb+srv://" + DB_USER + ":" + DB_PASS + "@testcluster.syuir.mongodb.net/?retryWrites=true&w=majority&appName=TestCluster");
        MongoDatabase db = databaseClient.getDatabase("integration_architectures");
        salesmenDB = db.getCollection("salesmen");
        reportsDB = db.getCollection("social_performance_reports");
    }

    /**
     * Adds a new salesman to the database
     *
     * @param record Salesman
     */
    @Override
    public void createSalesMan(SalesMan record) {
        salesmenDB.insertOne(record.toDocument());
    }

    /**
     * Adds a new social performance record to the database
     *
     * @param record   Social Performance Record
     * @param salesMan Associated salesman
     */
    @Override
    public void addSocialPerformanceRecord(SocialPerformanceRecord record, SalesMan salesMan) {
        reportsDB.insertOne(record.toDocument());
    }

    /**
     * @param sid Salesman ID
     * @return Return selected salesman
     */
    @Override
    public SalesMan readSalesMan(int sid) {
        Bson projection = Projections.fields(Projections.include("*"));
        Document doc = salesmenDB.find(eq("sid", sid)).projection(projection).first();

        assert doc != null;

        SalesMan salesman = null;

        try {
            salesman = new SalesMan(doc.getString("firstname"), doc.getString("lastname"), doc.getInteger("sid"));
        } catch (NullPointerException e) {
            System.out.println("Salesman not found");
        }

        return salesman;
    }

    /**
     * @return List of all salesmen
     */
    @Override
    public List<SalesMan> readAllSalesMen() {
        List<SalesMan> databases = new ArrayList<>();

        FindIterable<Document> list = salesmenDB.find();

        for (Document document : list) {
            SalesMan salesman = new SalesMan(document.getString("firstname"), document.getString("lastname"), document.getInteger("sid"));
            databases.add(salesman);
        }

        return databases;
    }

    /**
     * @param salesMan integrityToCompany SalesMan
     * @return List of all SozialPerformanceRecords of all years of the SalesMan
     */
    @Override
    public List<SocialPerformanceRecord> readSocialPerformanceRecord(SalesMan salesMan) {
        List<SocialPerformanceRecord> databases = new ArrayList<>();

        FindIterable<Document> list = reportsDB.find();

        for (Document document : list) {
            SocialPerformanceRecord spr = new SocialPerformanceRecord(
                    document.get("salesMan", SalesMan.class),
                    document.getInteger("year"),
                    document.getInteger("ledershipCompetence"),
                    document.getInteger("opennessToEmployee"),
                    document.getInteger("socialBehaviourToEmployee"),
                    document.getInteger("attitudeTowardsClient"),
                    document.getInteger("communicationsSkill"),
                    document.getInteger("integrityToCompany")
            );

            databases.add(spr);
        }

        return databases;
    }

    /**
     * Remove a salesman from the database
     *
     * @param sid Salesman ID
     */
    @Override
    public void deleteSalesMan(int sid) {
        salesmenDB.deleteOne(eq("sid", sid));
    }

    @Override
    public void deleteSocialPerformanceRecord(SalesMan salesMan, int year) {
        reportsDB.deleteOne(and(eq("salesMan", salesMan), eq("year", year)));
    }

}