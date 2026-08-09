package de.tobi.asz_inventory_api.bierwart.drink;

import de.tobi.asz_inventory_api.bierwart.bwAccountSnapshot.BwAccountSnapshotService;
import de.tobi.asz_inventory_api.enums.AccountType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class DrinkService {

    private final DrinkCsvRepository repository;
    private final BwAccountSnapshotService snapshotService;
    private final String filePath;
    private static final Logger log = LoggerFactory.getLogger(DrinkService.class);

    public DrinkService(DrinkCsvRepository repository, BwAccountSnapshotService snapshotService, @Value("${app.drinks.csv-path}") String filePath){
        this.repository = repository;
        this.snapshotService = snapshotService;
        this.filePath = filePath;
    }

    public List<Drink> getAllDrinks() throws IOException{
        List<Drink> drinks = repository.getAllDrinks(filePath);
        log.debug("DrinkService loaded {} drinks.", drinks.size());

        return drinks;
    }

    public void addDrink(Drink drink) throws IOException{
        List<Drink> drinks = repository.getAllDrinks(filePath);

        long nextId = drinks.stream()
                .mapToLong(Drink::getId)
                .max()
                .orElse(0) + 1;

        drink.setId(nextId);

        calculateSellingPrice(drink);
        calculateTotalValue(drink);

        repository.addDrink(drinks, drink);
        repository.saveDrinks(filePath, drinks);

        log.info("DrinkService added drink {} with id {}.", drink.getName(), drink.getId());

        String note = String.format("Automatische Buchung: %s %s", drink.getName(), drink.getTotalValue());
        snapshotService.addTransactionSnapshot(drink.getTotalValue(), AccountType.INVENTORY, note);
    }

    public void updateDrink(long id, Drink drink) throws IOException {
        List<Drink> drinks = repository.getAllDrinks(filePath);

        Drink oldDrink = drinks.stream().filter(d -> d.getId() == id).findAny().orElseThrow();
        BigDecimal oldTotalValue = oldDrink.getTotalValue();

        drink.setId(id);

        calculateSellingPrice(drink);
        calculateTotalValue(drink);

        repository.updateDrink(drinks, drink);
        repository.saveDrinks(filePath, drinks);

        log.info("DrinkService updated drink {} with id {}.", drink.getName(), drink.getId());

        BigDecimal valueIncrease = drink.getTotalValue().subtract(oldTotalValue);
        String note = String.format("Automatische Inventarkorrekturbuchung: %s %s €", drink.getName(), valueIncrease);
        snapshotService.addTransactionSnapshot(valueIncrease, AccountType.INVENTORY, note);
    }

    public void deleteDrink(long id) throws IOException {
        List<Drink> drinks = repository.getAllDrinks(filePath);

        Drink drink = drinks.stream().filter(d -> d.getId() == id).findAny().orElseThrow();

        repository.deleteDrink(drinks ,id);
        repository.saveDrinks(filePath, drinks);

        log.info("DrinkService deleted drink {} with id {}.", drink.getName(), drink.getId());

        String note = String.format("Automatische Rückbuchung: %s %s €", drink.getName(), drink.getTotalValue());
        snapshotService.addTransactionSnapshot(drink.getTotalValue().negate(), AccountType.INVENTORY, note);
    }

    private void calculateSellingPrice(Drink drink){
        drink.setSellingPrice(drink.getPurchasePrice().multiply(BigDecimal.valueOf(drink.getFactor())));
    }

    private void calculateTotalValue(Drink drink){
        drink.setTotalValue(drink.getPurchasePrice().multiply(BigDecimal.valueOf(drink.getAmount())));
    }
}
