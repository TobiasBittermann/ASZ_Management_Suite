package de.tobi.asz_inventory_api.member;

import de.tobi.asz_inventory_api.academicDegree.AcademicDegree;
import de.tobi.asz_inventory_api.enums.Status;
import de.tobi.asz_inventory_api.familyLine.FamilyLine;
import de.tobi.asz_inventory_api.position.Position;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToMany(mappedBy = "member")
    private List<AcademicDegree> academicDegree;
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
    @ManyToOne
    @JoinColumn(name = "familyLine_id")
    private FamilyLine familyLine;
    private boolean foundingMember;
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToMany(mappedBy = "member")
    private List<Position> positions;

    public Member() {
    }

    public Member(Member other) {
        this.id = other.id;
        this.academicDegree = other.academicDegree;
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
        this.foundingMember = other.foundingMember;
        this.status = other.status;
        this.positions = other.positions;
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

    public List<AcademicDegree> getAcademicDegree() {
        return academicDegree;
    }

    public void setAcademicDegree(List<AcademicDegree> academicDegree) {
        this.academicDegree = academicDegree;
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

    public boolean isFoundingMember() {
        return foundingMember;
    }

    public void setFoundingMember(boolean foundingMember) {
        this.foundingMember = foundingMember;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public void updateFrom(Member member) {
        this.academicDegree = member.academicDegree;
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
        this.foundingMember = member.foundingMember;
        this.status = member.status;
        this.positions = member.positions;
    }
}
