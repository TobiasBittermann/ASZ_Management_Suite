package de.tobi.asz_inventory_api.position;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PositionController {
    private final PositionService positionService;

    public PositionController(PositionService positionService){
        this.positionService = positionService;
    }

    @GetMapping("/positions")
    public List<Position> getAllPositions(){
        return positionService.getAllPositions();
    }

    @PostMapping("/positions")
    public void addPosition(@RequestBody Position position){
        positionService.addPositions(position);
    }

    @PutMapping("/positions/{id}")
    public void updatePosition(@PathVariable long id, @RequestBody Position position){
        positionService.updatePosition(id, position);
    }

    @DeleteMapping("/positions/{id}")
    public void deletePosition(@PathVariable long id){
        positionService.deletePosition(id);
    }
}
