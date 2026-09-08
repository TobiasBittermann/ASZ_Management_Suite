package de.tobi.asz_inventory_api.bierwart.drink;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DrinkController {

    private final DrinkService drinkService;

    public DrinkController(DrinkService drinkService){
        this.drinkService = drinkService;
    }

    @GetMapping("/drinks")
    public List<Drink> getAllDrinks() {
        return drinkService.getAllDrinks();
    }

    @PostMapping("/drinks")
    public void addDrink(@RequestBody Drink drink) {
        drinkService.addDrink(drink);
    }

    @PutMapping("/drinks/{id}")
    public void updateDrink(@PathVariable long id, @RequestBody Drink drink)  {
        drinkService.updateDrink(id, drink);
    }

    @DeleteMapping("/drinks/{id}")
    public void deleteDrink(@PathVariable long id)   {
        drinkService.deleteDrink(id);
    }
}
