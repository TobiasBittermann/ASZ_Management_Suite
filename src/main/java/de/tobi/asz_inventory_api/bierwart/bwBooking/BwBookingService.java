package de.tobi.asz_inventory_api.bierwart.bwBooking;

import de.tobi.asz_inventory_api.bierwart.bwAccountSnapshot.BwAccountSnapshotService;
import de.tobi.asz_inventory_api.bierwart.drink.Drink;
import de.tobi.asz_inventory_api.bierwart.drink.DrinkService;
import de.tobi.asz_inventory_api.enums.AccountType;
import de.tobi.asz_inventory_api.member.Member;
import de.tobi.asz_inventory_api.member.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BwBookingService {
    private final BwBookingRepository repository;
    private final MemberService memberService;
    private final DrinkService drinkService;
    private final BwAccountSnapshotService snapshotService;
    private static final Logger log = LoggerFactory.getLogger(BwBookingService.class);

    public BwBookingService(BwBookingRepository repository,
                            MemberService memberService,
                            DrinkService drinkService,
                            BwAccountSnapshotService snapshotService) {
        this.repository = repository;
        this.memberService = memberService;
        this.drinkService = drinkService;
        this.snapshotService = snapshotService;
    }

    public List<BwBooking> getAllBwBookings() {
        List<BwBooking> bookings = repository.findAll();
        log.debug("BwBookingsService loaded {} bookings.", bookings.size());
        return bookings;
    }

    public void addBwBooking(BwBooking booking) {
        repository.save(booking);

        changeBalance(booking, false);
        changeAmountDrinks(booking, false);

        log.info("BwBookingService added booking with id {}", booking.getId());
    }

    public void updateBwBooking(long id, BwBooking booking) {
        BwBooking oldBooking = repository.findById(id).orElseThrow();
        BigDecimal oldCost = oldBooking.getBookingCost();

        changeAmountDrinks(oldBooking, true);
        changeBalance(oldBooking, true);

        booking.setId(id);

        repository.save(booking);

        changeAmountDrinks(booking, false);
        changeBalance(booking, false);

        log.info("BwBookingService updated booking with id {}", booking.getId());

        Drink drink = drinkService.getDrinkById(booking.getDrinkId());

        BigDecimal valueIncrease = booking.getBookingCost().subtract(oldCost);
        String note = String.format("Automatische Inventurkorrekturbuchung: %s %s", drink.getName(), valueIncrease.negate());
        snapshotService.addTransactionSnapshot(valueIncrease.negate(), AccountType.INVENTORY, note);
    }

    public void deleteBwBooking(long id) {
        BwBooking booking = repository.findById(id).orElseThrow();
        repository.deleteById(id);

        changeBalance(booking, true);
        changeAmountDrinks(booking, true);

        log.info("BwBookingService deleted booking with id {}", id);

        Drink drink = drinkService.getDrinkById(booking.getDrinkId());

        String note = String.format("Automatische Inventurrückbuchung: %s %s", drink.getName(), booking.getBookingCost());
        snapshotService.addTransactionSnapshot(booking.getBookingCost(), AccountType.INVENTORY, note);
    }


    private void changeBalance(BwBooking booking, boolean x) {
        Member member = memberService.getMemberById(booking.getMemberId());
        BigDecimal price = booking.getBookingCost();

        if (x) {
            price = price.negate();
        }
        BigDecimal oldBalance = member.getBalance();
        member.setBalance(member.getBalance().subtract(price));

        memberService.updateMember(member.getId(), member);

        log.info("BwBookingService updated balance from member {} {} with id {} from {} to {}",
                member.getFirstName(),
                member.getLastName(),
                member.getId(),
                oldBalance,
                member.getBalance());
    }

    private void changeAmountDrinks(BwBooking booking, boolean x) {
        Drink drink = drinkService.getDrinkById(booking.getDrinkId());
        int amount = booking.getAmountDrink();

        if (x) {
            amount = -amount;
        }

        Integer oldAmount = drink.getAmount();
        drink.setAmount(drink.getAmount() - amount);

        drinkService.updateDrink(drink.getId(), drink);

        log.info("BwBookingService updated amount drinks from drink {} with id {} from {} to {}",
                drink.getName(),
                drink.getId(),
                oldAmount,
                drink.getAmount());
    }
}
