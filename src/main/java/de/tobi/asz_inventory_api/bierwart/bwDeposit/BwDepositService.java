package de.tobi.asz_inventory_api.bierwart.bwDeposit;

import de.tobi.asz_inventory_api.bierwart.bwAccountSnapshot.BwAccountSnapshotService;
import de.tobi.asz_inventory_api.member.Member;
import de.tobi.asz_inventory_api.member.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BwDepositService {
    private final BwDepositRepository repository;
    private final MemberService memberService;
    private final BwAccountSnapshotService snapshotService;
    private static final Logger log = LoggerFactory.getLogger(BwDepositService.class);

    public BwDepositService(BwDepositRepository repository,
                            MemberService memberService,
                            BwAccountSnapshotService snapshotService) {
        this.repository = repository;
        this.memberService = memberService;
        this.snapshotService = snapshotService;
    }

    public List<BwDeposit> getAllBwDeposits() {
        List<BwDeposit> deposits = repository.findAll();
        log.debug("BwDepositsService loaded {} deposits.", deposits.size());

        return deposits;
    }

    public void addBwDeposit(BwDeposit deposit) {
        repository.save(deposit);
        changeBalance(deposit.getMemberId(), deposit.getDeposit());
        log.info("BwDepositService added deposit with id {}", deposit.getId());

        Member member = memberService.getMemberById(deposit.getMemberId());
        String note = String.format("Automatische Buchung: %s %s %s €", member.getFirstName(), member.getLastName(), deposit.getDeposit());
        snapshotService.addTransactionSnapshot(deposit.getDeposit(), deposit.getAccountType(), note);
    }

    public void updateBwDeposit(long id, BwDeposit deposit) {
        deposit.setId(id);
        // Get old deposit to correct the number
        BwDeposit oldDeposit = repository.findById(id).orElseThrow();
        BigDecimal depositToCorrect = oldDeposit.getDeposit();
        repository.save(deposit);
        log.info("BwDepositService updated deposit with id {}", deposit.getId());

        Member member = memberService.getMemberById(deposit.getMemberId());

        // Calculate new deposit
        BigDecimal newDeposit = deposit.getDeposit().subtract(depositToCorrect);
        changeBalance(deposit.getMemberId(), newDeposit);

        String note = String.format("Automatische Korrekturbuchung: %s %s %s €", member.getFirstName(), member.getLastName(), newDeposit);
        snapshotService.addTransactionSnapshot(newDeposit, deposit.getAccountType(), note);
    }

    public void deleteBwDeposit(long id) {
        BwDeposit deposit = repository.findById(id).orElseThrow();
        repository.deleteById(id);
        changeBalance(deposit.getMemberId(), deposit.getDeposit().negate());
        log.info("BwDepositService deleted deposit with id {}", deposit.getId());

        Member member = memberService.getMemberById(deposit.getMemberId());
        String note = String.format("Automatische Rückbuchung: %s %s vom %s", member.getFirstName(), member.getLastName(), deposit.getDepositDate());
        snapshotService.addTransactionSnapshot(deposit.getDeposit().negate(), deposit.getAccountType(), note);
    }

    public void changeBalance(long memberId, BigDecimal amountDeposit) {
        Member member = memberService.getMemberById(memberId);
        BigDecimal oldBalance = member.getBalance();
        member.setBalance(member.getBalance().add(amountDeposit));
        memberService.updateMember(memberId, member);
        log.info("BwDepositService updated balance from member {} {} with id {} from {} to {}",
                member.getFirstName(),
                member.getLastName(),
                member.getId(),
                oldBalance,
                member.getBalance());
    }

}
