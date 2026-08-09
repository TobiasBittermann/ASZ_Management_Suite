package de.tobi.asz_inventory_api.bierwart.bwBooking;

import de.tobi.asz_inventory_api.bierwart.bwAccountSnapshot.BwAccountSnapshotService;
import de.tobi.asz_inventory_api.bierwart.drink.Drink;
import de.tobi.asz_inventory_api.bierwart.drink.DrinkCsvRepository;
import de.tobi.asz_inventory_api.enums.AccountType;
import de.tobi.asz_inventory_api.member.Member;
import de.tobi.asz_inventory_api.member.MemberCsvRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class BwBookingService {
    private final BwBookingCsvRepository repository;
    private final MemberCsvRepository memberRepository;
    private final DrinkCsvRepository drinkRepository;
    private final BwAccountSnapshotService snapshotService;
    private final String filePath;
    private final String memberFilePath;
    private final String drinkFilePath;
    private static final Logger log = LoggerFactory.getLogger(BwBookingService.class);

    public BwBookingService(BwBookingCsvRepository repository,
                            MemberCsvRepository memberRepository,
                            DrinkCsvRepository drinkRepository,
                            BwAccountSnapshotService snapshotService,
                            @Value("${app.bwbookings.csv-path}") String filePath,
                            @Value("${app.members.csv-path}") String memberFilePath,
                            @Value("${app.drinks.csv-path}") String drinkFilePath) {
        this.repository = repository;
        this.memberRepository = memberRepository;
        this.drinkRepository = drinkRepository;
        this.snapshotService = snapshotService;
        this.filePath = filePath;
        this.memberFilePath = memberFilePath;
        this.drinkFilePath = drinkFilePath;
    }

    public List<BwBooking> getAllBwBookings() throws IOException {
        List<BwBooking> bookings = repository.getAllBwBookings(filePath);
        log.debug("BwBookingsService loaded {} bookings.", bookings.size());

        return bookings;
    }

    public void addBwBooking(BwBooking booking) throws IOException {
        List<BwBooking> bookings = repository.getAllBwBookings(filePath);
        List<Drink> drinks = drinkRepository.getAllDrinks(drinkFilePath);

        long nextId = bookings.stream()
                .mapToLong(BwBooking::getId)
                .max()
                .orElse(0) + 1;

        booking.setId(nextId);

        repository.addBwBooking(bookings, booking);
        repository.saveBwBooking(filePath, bookings);

        changeBalance(booking, false);
        changeAmountDrinks(booking, false);

        log.info("BwBookingService added booking with id {}", booking.getId());

        Drink drink = drinks.stream().filter(d -> d.getId() == booking.getDrinkId()).findAny().orElseThrow();

        String note = String.format("Automatische Inventarbuchung: %s %s", drink.getName(), booking.getBookingCost());
        snapshotService.addTransactionSnapshot(booking.getBookingCost().negate(), AccountType.INVENTORY, note);
    }

    public void updateBwBooking(long id, BwBooking booking) throws IOException {
        List<BwBooking> bookings = repository.getAllBwBookings(filePath);
        List<Drink> drinks = drinkRepository.getAllDrinks(drinkFilePath);

        BwBooking oldBooking = bookings.stream().filter(b -> b.getId() == id).findAny().orElseThrow();
        BigDecimal oldCost = oldBooking.getBookingCost();

        changeAmountDrinks(oldBooking, true);
        changeBalance(oldBooking, true);

        booking.setId(id);

        repository.updateBwBooking(bookings, booking);
        repository.saveBwBooking(filePath, bookings);

        changeAmountDrinks(booking, false);
        changeBalance(booking, false);

        log.info("BwBookingService updated booking with id {}", booking.getId());

        Drink drink = drinks.stream().filter(d -> d.getId() == booking.getDrinkId()).findAny().orElseThrow();

        BigDecimal valueIncrease = booking.getBookingCost().subtract(oldCost);
        String note = String.format("Automatische Inventarkorrekturbuchung: %s %s", drink.getName(), valueIncrease.negate());
        snapshotService.addTransactionSnapshot(valueIncrease.negate(), AccountType.INVENTORY, note);
    }

    public void deleteBwBooking(long id) throws IOException {
        List<BwBooking> bookings = repository.getAllBwBookings(filePath);
        List<Drink> drinks = drinkRepository.getAllDrinks(drinkFilePath);

        BwBooking booking = bookings.stream().filter(b -> b.getId() == id).findAny().orElseThrow();

        repository.deleteBwBooking(bookings, id);
        repository.saveBwBooking(filePath, bookings);

        changeBalance(booking, true);
        changeAmountDrinks(booking, true);

        log.info("BwBookingService deleted booking with id {}", id);

        Drink drink = drinks.stream().filter(d -> d.getId() == booking.getDrinkId()).findAny().orElseThrow();

        String note = String.format("Automatische Inventarrückbuchung: %s %s", drink.getName(), booking.getBookingCost());
        snapshotService.addTransactionSnapshot(booking.getBookingCost(), AccountType.INVENTORY, note);
    }


    private void changeBalance(BwBooking booking, boolean x) throws IOException {
        List<Member> members = memberRepository.getAllMembers(memberFilePath);

        Member member = members.stream().filter(m -> m.getId() == booking.getMemberId()).findAny().orElseThrow();

        BigDecimal price = booking.getBookingCost();

        if (x) {
            price = price.negate();
        }
        BigDecimal oldBalance = member.getBalance();
        member.setBalance(member.getBalance().subtract(price));

        memberRepository.updateMember(members, member);
        memberRepository.saveMembers(memberFilePath, members);

        log.info("BwBookingService updated balance from member {} {} with id {} from {} to {}",
                member.getFirstName(),
                member.getLastName(),
                member.getId(),
                oldBalance,
                member.getBalance());
    }

    private void changeAmountDrinks(BwBooking booking, boolean x) throws IOException {
        List<Drink> drinks = drinkRepository.getAllDrinks(drinkFilePath);

        Drink drink = drinks.stream().filter(d -> d.getId() == booking.getDrinkId()).findAny().orElseThrow();

        int amount = booking.getAmountDrink();

        if (x) {
            amount = -amount;
        }

        Integer oldAmount = drink.getAmount();
        drink.setAmount(drink.getAmount() - amount);

        drinkRepository.updateDrink(drinks, drink);
        drinkRepository.saveDrinks(drinkFilePath, drinks);

        log.info("BwBookingService updated amount drinks from drink {} with id {} from {} to {}",
                drink.getName(),
                drink.getId(),
                oldAmount,
                drink.getAmount());
    }
}
