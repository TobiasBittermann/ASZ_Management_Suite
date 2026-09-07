package de.tobi.asz_inventory_api.member;

import de.tobi.asz_inventory_api.enums.AcademicDegree;
import de.tobi.asz_inventory_api.enums.Position;
import de.tobi.asz_inventory_api.enums.Status;
import jakarta.persistence.Embedded;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Member {
    private long id;
    private AcademicDegree academicDegree;
    private String fieldOfStudy;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    @Embedded
    private Address address;
    private String phone;
    private String email;
    private BigDecimal balance;
    private boolean sepaBw;
    private String iban;
    //doj - date of joining
    private LocalDate dojActiveMember;
    private LocalDate dojAlumni;
    private LocalDate dateOfExit;
    private FamilyLine familyLine;
    private Status status;
    private Position position;
    public List<Position> dischargedPositions;

    public Member() {
    }

    public Member(Member other) {
        this.id = other.id;
        this.academicDegree = other.academicDegree;
        this.fieldOfStudy = other.fieldOfStudy;
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.birthday = other.birthday;
        this.address = other.address;
        this.phone = other.phone;
        this.email = other.email;
        this.balance = other.balance;
        this.sepaBw = other.sepaBw;
        this.iban = other.iban;
        this.dojActiveMember = other.dojActiveMember;
        this.dojAlumni = other.dojAlumni;
        this.dateOfExit = other.dateOfExit;
        this.familyLine = other.familyLine;
        ;
        this.status = other.status;
        this.position = other.position;
        this.dischargedPositions = other.dischargedPositions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AcademicDegree getAcademicDegree() {
        return academicDegree;
    }

    public void setAcademicDegree(AcademicDegree academicDegree) {
        this.academicDegree = academicDegree;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSepaBw() {
        return sepaBw;
    }

    public void setSepaBw(boolean sepaBw) {
        this.sepaBw = sepaBw;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public LocalDate getDojActiveMember() {
        return dojActiveMember;
    }

    public void setDojActiveMember(LocalDate dojActiveMember) {
        this.dojActiveMember = dojActiveMember;
    }

    public LocalDate getDojAlumni() {
        return dojAlumni;
    }

    public void setDojAlumni(LocalDate dojAlumni) {
        this.dojAlumni = dojAlumni;
    }

    public LocalDate getDateOfExit() {
        return dateOfExit;
    }

    public void setDateOfExit(LocalDate dateOfExit) {
        this.dateOfExit = dateOfExit;
    }

    public FamilyLine getFamilyLine() {
        return familyLine;
    }

    public void setFamilyLine(FamilyLine familyLine) {
        this.familyLine = familyLine;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List<Position> getDischargedPositions() {
        return dischargedPositions;
    }

    public void setDischargedPositions(List<Position> dischargedPositions) {
        this.dischargedPositions = dischargedPositions;
    }

    public void updateFrom(Member member) {
        this.academicDegree = member.academicDegree;
        this.fieldOfStudy = member.fieldOfStudy;
        this.firstName = member.firstName;
        this.lastName = member.lastName;
        this.birthday = member.birthday;
        this.address = member.address;
        this.phone = member.phone;
        this.email = member.email;
        this.balance = member.balance;
        this.sepaBw = member.sepaBw;
        this.iban = member.iban;
        this.dojActiveMember = member.dojActiveMember;
        this.dojAlumni = member.dojAlumni;
        this.dateOfExit = member.dateOfExit;
        this.familyLine = member.familyLine;
        this.status = member.status;
        this.position = member.position;
        this.dischargedPositions = member.dischargedPositions;
    }
}
