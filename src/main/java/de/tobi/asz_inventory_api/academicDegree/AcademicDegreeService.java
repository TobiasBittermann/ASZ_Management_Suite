package de.tobi.asz_inventory_api.academicDegree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcademicDegreeService {
    private final AcademicDegreeRepository repository;
    private static final Logger log = LoggerFactory.getLogger(AcademicDegreeService.class);

    public AcademicDegreeService(AcademicDegreeRepository repository) {
        this.repository = repository;
    }

    public List<AcademicDegree> getAllAcademicDegrees(){
        List<AcademicDegree> academicDegrees = repository.findAll();
        log.debug("AcademicDegreeService loaded {} academic degrees.", academicDegrees.size());

        return academicDegrees;
    }

    public void addAcademicDegree(AcademicDegree degree){
        repository.save(degree);
        log.info("AcademicDegree added degree {} with id {}", degree.getAcademicDegreeType(), degree.getId());
    }

    public void updateAcademicDegree(long id, AcademicDegree degree){
        degree.setId(id);
        repository.save(degree);
        log.info("AcademicDegreeService updated degree {} with id {}.", degree.getAcademicDegreeType(), degree.getId());
    }

    public void deleteAcademicDegree(long id){
        AcademicDegree degree = repository.findById(id).orElseThrow();
        repository.deleteById(id);

        log.info("AcademicDegreeService deleted degree {} with id {}.", degree.getAcademicDegreeType(), degree.getId());
    }
}
