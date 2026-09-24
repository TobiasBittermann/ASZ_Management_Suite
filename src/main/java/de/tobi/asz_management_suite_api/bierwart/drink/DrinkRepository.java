package de.tobi.asz_management_suite_api.bierwart.drink;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DrinkRepository extends JpaRepository<Drink, Long> {
}
