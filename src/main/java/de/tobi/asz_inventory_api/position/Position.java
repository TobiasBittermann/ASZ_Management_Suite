package de.tobi.asz_inventory_api.position;

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
    @JoinColumn(name="member_id")
    private Member member;
    @Enumerated(EnumType.STRING)
    private PositionType position;
    @Enumerated(EnumType.STRING)
    private Semester semester;
    private LocalDate year;
    private boolean discharged;

    public Position() {}

    public Position (Position other){
        this.id = other.id;
        this.member = other.member;
        this.position = other.position;
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

    public PositionType getPosition() {
        return position;
    }

    public void setPosition(PositionType position) {
        this.position = position;
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
        this.position = position.position;
        this.semester = position.semester;
        this.year = position.year;
        this.discharged = position.discharged;
    }
}
