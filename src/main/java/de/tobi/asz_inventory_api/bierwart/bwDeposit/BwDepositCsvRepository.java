package de.tobi.asz_inventory_api.bierwart.bwDeposit;

import de.tobi.asz_inventory_api.enums.AccountType;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BwDepositCsvRepository {
    private String getBwDepositHeader() {
        Field[] fields = BwDeposit.class.getDeclaredFields();
        StringBuilder header = new StringBuilder();

        for (int i = 0; i < fields.length; i++) {
            header.append(fields[i].getName());

            if (i < fields.length - 1) {
                header.append(",");
            }
        }
        return header.toString();
    }

    public List<BwDeposit> getAllBwDeposits(String filePath) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("CSV must not be blank");
        }

        List<BwDeposit> deposits = new ArrayList<>();
        Path path = Path.of(filePath);

        if (Files.notExists(path)) {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.createFile(path);
        }

        if (Files.size(path) == 0) {
            Files.writeString(path, getBwDepositHeader() + System.lineSeparator());
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

                BwDeposit deposit = new BwDeposit();
                deposit.setId(Long.parseLong(values[0]));
                deposit.setMemberId(Long.parseLong(values[1]));
                deposit.setDeposit(new BigDecimal(values[2]));
                deposit.setAccountType(AccountType.valueOf(values[3]));
                deposit.setDepositDate(LocalDateTime.parse(values[4]));
                deposit.setDescription(values[5]);

                deposits.add(deposit);
            }

            return deposits;
        }
    }

    public void addBwDeposit(List<BwDeposit> deposits, BwDeposit deposit) {
        deposits.add(deposit);
    }

    public void updateBwDeposit(List<BwDeposit> deposits, BwDeposit updatedDeposit) {
        for (BwDeposit deposit : deposits) {
            if (deposit.getId() == updatedDeposit.getId()) {
                deposit.updateFrom(updatedDeposit);
                return;
            }
        }
    }

    public void deleteBwDeposit(List<BwDeposit> deposits, long id) {
        deposits.removeIf(deposit -> deposit.getId() == id);
    }

    public void saveBwDeposit(String filePath, List<BwDeposit> deposits) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("CSV must not be blank");
        }

        Path path = Path.of(filePath);
        Path parent = path.getParent();
        if (parent != null){
            Files.createDirectories(parent);
        }
        if (Files.notExists(path)){
            Files.createFile(path);
        }

        StringBuilder content = new StringBuilder();
        content.append(getBwDepositHeader()).append(System.lineSeparator());

        for (BwDeposit deposit : deposits){
            content.append(deposit.getId()).append(",");
            content.append(deposit.getMemberId()).append(",");
            content.append(deposit.getDeposit()).append(",");
            content.append(deposit.getAccountType()).append(",");
            content.append(deposit.getDepositDate()).append(",");
            content.append(deposit.getDescription());
            content.append(System.lineSeparator());
        }

        Files.writeString(path, content.toString());
    }
}
