package de.tobi.asz_management_suite_api.bierwart.drink;

import de.tobi.asz_management_suite_api.bierwart.bwAccountSnapshot.BwAccountSnapshotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DrinkServiceTest {

    @Mock
    DrinkRepository repository;

    @Mock
    BwAccountSnapshotService snapshotService;

    @InjectMocks
    DrinkService drinkService;

    @Test
    void addDrink_calculateSellingPriceAndTotalValue(){
        //Arrange
        Drink drink = new Drink();
        drink.setPurchasePrice(BigDecimal.valueOf(2));
        drink.setFactor(1.5);
        drink.setAmount(20);
        //Act
        drinkService.addDrink(drink);
        //Assert
        assertEquals(0, drink.getSellingPrice().compareTo(BigDecimal.valueOf(3.0)));
        assertEquals(0, drink.getTotalValue().compareTo(BigDecimal.valueOf(40.0)));
        verify(repository).save(drink);

    }
}
