package de.tobi.asz_inventory_api.bierwart.drink;

import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DrinkCsvRepository {

    private String getDrinksHeader() {
        Field[] fields = Drink.class.getDeclaredFields();
        StringBuilder header = new StringBuilder();

        for (int i = 0; i < fields.length; i++) {
            header.append(fields[i].getName());

            if (i < fields.length - 1) {
                header.append(",");
            }
        }
        return header.toString();
    }

    public List<Drink> getAllDrinks(String filePath) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("CSV file path must not be blank");
        }

        List<Drink> drinks = new ArrayList<>();
        Path path = Path.of(filePath);

        if (Files.notExists(path)) {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.createFile(path);
        }

        if (Files.size(path) == 0) {
            Files.writeString(path, getDrinksHeader() + System.lineSeparator());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                if (line.isBlank()) {
                    continue;
                }

                String[] values = line.split(",");

                Drink drink = new Drink();
                drink.setId(Long.parseLong(values[0]));
                drink.setName(values[1]);
                drink.setPurchasePrice(new BigDecimal(values[2]));
                drink.setSellingPrice(new BigDecimal(values[3]));
                drink.setFactor(Double.parseDouble(values[4]));
                drink.setAmount(Integer.parseInt(values[5]));
                drink.setTotalValue(new BigDecimal(values[6]));

                drinks.add(drink);
            }
        }
        return drinks;
    }

    public void addDrink(List<Drink> drinks, Drink drink) {
        drinks.add(drink);
    }

    public void updateDrink(List<Drink> drinks, Drink updatedDrink) {
        for (Drink drink : drinks) {
            if (drink.getId() == updatedDrink.getId()) {
                drink.updateFrom(updatedDrink);
                return;
            }
        }
    }

    public void deleteDrink(List<Drink> drinks, long id) {
        drinks.removeIf(drink -> drink.getId() == id);
    }

    public void saveDrinks(String filePath, List<Drink> drinks) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("CSV file path must not be blank");
        }

        Path path = Path.of(filePath);
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        if(Files.notExists(path)){
            Files.createFile(path);
        }

        StringBuilder content = new StringBuilder();
        content.append(getDrinksHeader()).append(System.lineSeparator());

        for (Drink drink : drinks) {
            content.append(drink.getId()).append(",")
                    .append(drink.getName()).append(",")
                    .append(drink.getPurchasePrice()).append(",")
                    .append(drink.getSellingPrice()).append(",")
                    .append(drink.getFactor()).append(",")
                    .append(drink.getAmount()).append(",")
                    .append(drink.getTotalValue())
                    .append(System.lineSeparator());
        }

        Files.writeString(path, content.toString());
    }
}
