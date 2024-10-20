package de.hbrs.ia.model;

import org.bson.Document;

public class SocialPerformanceRecord {

    private SalesMan salesMan;
    private Integer year; //Year of the Report

    private Integer leaderShipCompetence;
    private Integer opennessToEmployee;
    private Integer socialBehaviourToEmployee;
    private Integer attitudeTowardsClient;
    private Integer communicationSkills;
    private Integer integrityToCompany;

    /**
     * @param salesMan                  SalesMan of the SozialPerformanceRecord
     * @param year                      Year of SozialPerformanceRecord of the SalesMan
     * @param leaderShipCompetence      Leader Ship Competencein which a SalesMan efficiently leads and influences his team to achieve the company's goals.
     * @param opennessToEmployee        Openness a Salesman shows to his Employees and fellow Co-Workers
     * @param socialBehaviourToEmployee The social behaviour an employee enacts
     * @param attitudeTowardsClient     Represents the attidude and mindeset which a salesmann has when interacting with clients
     * @param communicationSkills       The grade of skills and talents which the salesman displays during social encaunters
     * @param integrityToCompany        Represents the integrity loyalty and intrest the salesman has in regards towards the company
     */
    public SocialPerformanceRecord(SalesMan salesMan, Integer year, Integer leaderShipCompetence, Integer opennessToEmployee,
                                   Integer socialBehaviourToEmployee, Integer attitudeTowardsClient,
                                   Integer communicationSkills, Integer integrityToCompany
    ) {

        this.salesMan = salesMan;
        this.year = year;

        this.leaderShipCompetence = leaderShipCompetence;
        this.opennessToEmployee = opennessToEmployee;
        this.socialBehaviourToEmployee = socialBehaviourToEmployee;
        this.attitudeTowardsClient = attitudeTowardsClient;
        this.communicationSkills = communicationSkills;
        this.integrityToCompany = integrityToCompany;
    }


    public SalesMan getSalesMan() {
        return salesMan;
    }

    public void setSalesMan(SalesMan salesMan) {
        this.salesMan = salesMan;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getLeaderShipCompetence() {
        return leaderShipCompetence;
    }

    public void setLeaderShipCompetence(Integer leaderShipCompetence) {
        this.leaderShipCompetence = leaderShipCompetence;
    }

    public Integer getOpennessToEmployee() {
        return opennessToEmployee;
    }

    public void setOpennessToEmployee(Integer opennessToEmployee) {
        this.opennessToEmployee = opennessToEmployee;
    }

    public Integer getSocialBehaviourToEmployee() {
        return socialBehaviourToEmployee;
    }

    public void setSocialBehaviourToEmployee(Integer socialBehaviourToEmployee) {
        this.socialBehaviourToEmployee = socialBehaviourToEmployee;
    }

    public Integer getAttitudeTowardsClient() {
        return attitudeTowardsClient;
    }

    public void setAttitudeTowardsClient(Integer attitudeTowardsClient) {
        this.attitudeTowardsClient = attitudeTowardsClient;
    }

    public Integer getCommunicationSkills() {
        return communicationSkills;
    }

    public void setCommunicationSkills(Integer communicationSkills) {
        this.communicationSkills = communicationSkills;
    }

    public Integer getIntegrityToCompany() {
        return integrityToCompany;
    }

    public void setIntegrityToCompany(Integer integrityToCompany) {
        this.integrityToCompany = integrityToCompany;
    }


    public Document toDocument() {
        org.bson.Document document = new Document();
        document.append("salesMan", this.salesMan);
        document.append("year", this.year);
        document.append("ledershipCompetence", this.leaderShipCompetence);
        document.append("opennessToEmployee", this.opennessToEmployee);
        document.append("socialBehaviourToEmployee", this.socialBehaviourToEmployee);
        document.append("attitudeTowardsClient", this.attitudeTowardsClient);
        document.append("communicationsSkill", this.communicationSkills);
        document.append("integrityToCompany", this.integrityToCompany);
        return document;
    }

}
