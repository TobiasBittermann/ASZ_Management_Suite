package de.tobi.asz_inventory_api.familyLine;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.tobi.asz_inventory_api.member.Member;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class FamilyLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private LocalDate foundingDate;
    @OneToMany(mappedBy = "familyLine")
    @JsonManagedReference
    private List<Member> members;

    public FamilyLine() {}

    public FamilyLine(FamilyLine other){
        this.id = other.id;
        this.name = other.name;
        this.foundingDate = other.foundingDate;
        this.members = other.members;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getFoundingDate() {
        return foundingDate;
    }

    public void setFoundingDate(LocalDate foundingDate) {
        this.foundingDate = foundingDate;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public void updateFrom(FamilyLine familyLine){
        this.name = familyLine.name;
        this.foundingDate = familyLine.foundingDate;
        this.members = familyLine.members;
    }
}
