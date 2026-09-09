package de.tobi.asz_inventory_api.familyLine;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FamilyLineController {
    private final FamilyLineService familyLineService;

    public FamilyLineController(FamilyLineService familyLineService){
        this.familyLineService = familyLineService;
    }

    @GetMapping("/familylines")
    public List<FamilyLine> getAllFamilyLines(){
        return familyLineService.getAllFamilyLines();
    }

    @PostMapping("/familylines")
    public void addFamilyLine(@RequestBody FamilyLine familyLine){
        familyLineService.addFamilyLine(familyLine);
    }

    @PutMapping("/familylines/{id}")
    public void updateFamilyLines(@PathVariable long id, @RequestBody FamilyLine familyLine){
        familyLineService.updateFamilyLine(id, familyLine);
    }

    @DeleteMapping("/familylines/{id}")
    public void deleteFamilyLine(@PathVariable long id){
        familyLineService.deleteDrink(id);
    }
}
