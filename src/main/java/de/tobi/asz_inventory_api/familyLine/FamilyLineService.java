package de.tobi.asz_inventory_api.familyLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FamilyLineService {
    private final FamilyLineRepository repository;
    private static final Logger log = LoggerFactory.getLogger(FamilyLineService.class);

    public FamilyLineService(FamilyLineRepository repository) {
        this.repository = repository;
    }

    public List<FamilyLine> getAllFamilyLines(){
        List<FamilyLine> familyLines = repository.findAll();
        log.debug("FamilyLineService loaded {} drinks.", familyLines.size());

        return familyLines;
    }

    public void addFamilyLine(FamilyLine familyLine){
        repository.save(familyLine);
        log.info("FamilyLineService added familyLine {} with id {}.", familyLine.getName(), familyLine.getId());
    }

    public void updateFamilyLine(long id, FamilyLine familyLine){
        familyLine.setId(id);
        repository.save(familyLine);

        log.info("FamilyLineService updated familyLine {} with id {}.", familyLine.getName(), familyLine.getId());
    }

    public void deleteDrink(long id){
        FamilyLine familyLine = repository.findById(id).orElseThrow();
        repository.deleteById(id);

        log.info("FamilyLineService deleted familyLine {} with id {}.", familyLine.getName(), familyLine.getId());
    }
}
