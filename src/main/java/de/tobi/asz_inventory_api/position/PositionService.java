package de.tobi.asz_inventory_api.position;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {
    private final PositionRepository repository;
    private static final Logger log = LoggerFactory.getLogger(PositionService.class);

    public PositionService(PositionRepository repository) {
        this.repository = repository;
    }

    public List<Position> getAllPositions(){
        List<Position> positions = repository.findAll();
        log.debug("PositionService loaded {} positions.", positions.size());

        return positions;
    }

    public void addPositions(Position position){
        repository.save(position);
        log.info("PositionService added position {} with id {}.", position.getPositionType(), position.getId());
    }

    public void updatePosition(long id, Position position){
        position.setId(id);
        repository.save(position);

        log.info("PositionService updated position {} with id {}.", position.getPositionType(), position.getId());
    }

    public void deletePosition(long id){
        Position position = repository.findById(id).orElseThrow();
        repository.deleteById(id);

        log.info("PositionService deleted position {} with id {}.", position.getPositionType(), position.getId());
    }
}
