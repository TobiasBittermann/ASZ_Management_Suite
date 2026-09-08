package de.tobi.asz_inventory_api.academicDegree;

import de.tobi.asz_inventory_api.enums.AcademicDegreeType;
import de.tobi.asz_inventory_api.member.Member;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class AcademicDegree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Enumerated(EnumType.STRING)
    private AcademicDegreeType academicDegreeType;
    private String fieldOfStudy;
    private LocalDate date;

    public AcademicDegree() {}

    public AcademicDegree(AcademicDegree other){
        this.id = other.id;
        this.member = other.member;
        this.academicDegreeType = other.academicDegreeType;
        this.fieldOfStudy = other.fieldOfStudy;
        this.date = other.date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public AcademicDegreeType getAcademicDegreeType() {
        return academicDegreeType;
    }

    public void setAcademicDegreeType(AcademicDegreeType academicDegreeType) {
        this.academicDegreeType = academicDegreeType;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void updateFrom(AcademicDegree academicDegree){
        this.member = academicDegree.member;
        this.academicDegreeType = academicDegree.academicDegreeType;
        this.fieldOfStudy = academicDegree.fieldOfStudy;
        this.date = academicDegree.date;
    }
}
