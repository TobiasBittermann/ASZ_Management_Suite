package de.tobi.asz_inventory_api.bierwart.drink;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class DrinkController {

    private final DrinkService drinkService;

    public DrinkController(DrinkService drinkService){
        this.drinkService = drinkService;
    }

    @GetMapping("/drinks")
    public List<Drink> getAllDrinks() throws IOException {
        return drinkService.getAllDrinks();
    }

    @PostMapping("/drinks")
    public void addDrink(@RequestBody Drink drink) throws IOException {
        drinkService.addDrink(drink);
    }

    @PutMapping("/drinks/{id}")
    public void updateDrink(@PathVariable long id, @RequestBody Drink drink) throws IOException {
        drinkService.updateDrink(id, drink);
    }

    @DeleteMapping("/drinks/{id}")
    public void deleteDrink(@PathVariable long id) throws IOException {
        drinkService.deleteDrink(id);
    }
}
