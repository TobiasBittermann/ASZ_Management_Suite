package de.tobi.asz_inventory_api.position;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.tobi.asz_inventory_api.enums.PositionType;
import de.tobi.asz_inventory_api.enums.Semester;
import de.tobi.asz_inventory_api.member.Member;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="member_id")
    private Member member;
    @Enumerated(EnumType.STRING)
    private PositionType positionType;
    @Enumerated(EnumType.STRING)
    private Semester semester;
    private LocalDate year;
    private boolean discharged;

    public Position() {}

    public Position (Position other){
        this.id = other.id;
        this.member = other.member;
        this.positionType = other.positionType;
        this.semester = other.semester;
        this.year = other.year;
        this.discharged = other.discharged;
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

    public PositionType getPositionType() {
        return positionType;
    }

    public void setPositionType(PositionType positionType) {
        this.positionType = positionType;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public LocalDate getYear() {
        return year;
    }

    public void setYear(LocalDate year) {
        this.year = year;
    }

    public boolean isDischarged() {
        return discharged;
    }

    public void setDischarged(boolean discharged) {
        this.discharged = discharged;
    }

    public void updateFrom(Position position){
        this.member = position.member;
        this.positionType = position.positionType;
        this.semester = position.semester;
        this.year = position.year;
        this.discharged = position.discharged;
    }
}
