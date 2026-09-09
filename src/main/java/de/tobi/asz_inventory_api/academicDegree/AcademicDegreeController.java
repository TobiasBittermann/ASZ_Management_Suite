package de.tobi.asz_inventory_api.academicDegree;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AcademicDegreeController {
    private final AcademicDegreeService academicDegreeService;

    public AcademicDegreeController(AcademicDegreeService academicDegreeService){
        this.academicDegreeService = academicDegreeService;
    }

    @GetMapping("/academicdegrees")
    public List<AcademicDegree> getAllAcademiDegrees() {
        return academicDegreeService.getAllAcademicDegrees();
    }

    @PostMapping("/academicdegrees")
    public void addAcademicDegrees(@RequestBody AcademicDegree degree){
        academicDegreeService.addAcademicDegree(degree);
    }

    @PutMapping("/academicdegrees/{id}")
    public void updateAcademicDegree(@PathVariable long id, @RequestBody AcademicDegree degree){
        academicDegreeService.updateAcademicDegree(id, degree);
    }

    @DeleteMapping("/academicdegrees/{id}")
    public void deleteDrink(@PathVariable long id){
        academicDegreeService.deleteAcademicDegree(id);
    }
}
