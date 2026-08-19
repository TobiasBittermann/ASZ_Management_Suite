package de.tobi.asz_inventory_api.bierwart.bwDeposit;

import de.tobi.asz_inventory_api.bierwart.bwAccountSnapshot.BwAccountSnapshotService;
import de.tobi.asz_inventory_api.member.Member;
import de.tobi.asz_inventory_api.member.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class BwDepositService {
    private final BwDepositCsvRepository repository;
    private final MemberService memberService;
    private final BwAccountSnapshotService snapshotService;
    private final String filePath;
    private static final Logger log = LoggerFactory.getLogger(BwDepositService.class);

    public BwDepositService(BwDepositCsvRepository repository,
                            MemberService memberService,
                            BwAccountSnapshotService snapshotService,
                            @Value("${app.deposits.csv-path}") String filePath) {
        this.repository = repository;
        this.memberService = memberService;
        this.snapshotService = snapshotService;
        this.filePath = filePath;
    }

    public List<BwDeposit> getAllBwDeposits() throws IOException {
        List<BwDeposit> deposits = repository.getAllBwDeposits(filePath);
        log.debug("BwDepositsService loaded {} deposits.", deposits.size());

        return deposits;
    }

    public void addBwDeposit(BwDeposit deposit) throws IOException {
        List<BwDeposit> deposits = repository.getAllBwDeposits(filePath);
        List<Member> members = memberService.getAllMembers();

        long nextId = deposits.stream()
                .mapToLong(BwDeposit::getId)
                .max()
                .orElse(0) + 1;

        deposit.setId(nextId);

        repository.addBwDeposit(deposits, deposit);
        repository.saveBwDeposit(filePath, deposits);

        changeBalance(deposit.getMemberId(), deposit.getDeposit());

        log.info("BwDepositService added deposit with id {}", deposit.getId());

        Member member = members.stream().filter(m -> m.getId() == deposit.getMemberId()).findAny().orElseThrow();
        String note = String.format("Automatische Buchung: %s %s %s €", member.getFirstName(), member.getLastName(), deposit.getDeposit());
        snapshotService.addTransactionSnapshot(deposit.getDeposit(), deposit.getAccountType(), note);
    }

    public void updateBwDeposit(long id, BwDeposit deposit) throws IOException {
        List<BwDeposit> deposits = repository.getAllBwDeposits(filePath);
        List<Member> members = memberService.getAllMembers();

        deposit.setId(id);

        // Get old deposit to correct the number
        BwDeposit oldDeposit = deposits.stream().filter(od -> od.getId() == id).findAny().orElseThrow();
        BigDecimal depositToCorrect = oldDeposit.getDeposit();

        repository.updateBwDeposit(deposits, deposit);
        repository.saveBwDeposit(filePath, deposits);

        log.info("BwDepositService updated deposit with id {}", deposit.getId());

        Member member = members.stream().filter(m -> m.getId() == deposit.getMemberId()).findAny().orElseThrow();

        // Calculate new deposit
        BigDecimal newDeposit = deposit.getDeposit().subtract(depositToCorrect);
        changeBalance(deposit.getMemberId(), newDeposit);

        String note = String.format("Automatische Korrekturbuchung: %s %s %s €", member.getFirstName(), member.getLastName(), newDeposit);
        snapshotService.addTransactionSnapshot(newDeposit, deposit.getAccountType(), note);
    }

    public void deleteBwDeposit(long id) throws IOException {
        List<BwDeposit> deposits = repository.getAllBwDeposits(filePath);
        List<Member> members = memberService.getAllMembers();

        BwDeposit deposit = deposits.stream().filter(d -> d.getId() == id).findAny(). orElseThrow();

        repository.deleteBwDeposit(deposits, id);
        repository.saveBwDeposit(filePath, deposits);

        changeBalance(deposit.getMemberId(), deposit.getDeposit().negate());

        log.info("BwDepositService deleted deposit with id {}", deposit.getId());

        Member member = members.stream().filter(m -> m.getId() == deposit.getMemberId()).findAny().orElseThrow();
        String note = String.format("Automatische Rückbuchung: %s %s vom %s", member.getFirstName(), member.getLastName(), deposit.getDepositDate());
        snapshotService.addTransactionSnapshot(deposit.getDeposit().negate(), deposit.getAccountType(), note);
    }

    public void changeBalance(long memberId, BigDecimal amountDeposit) throws IOException {
        List<Member> members = memberService.getAllMembers();

        Member member = members.stream()
                .filter(m -> m.getId() == memberId)
                .findAny()
                .orElseThrow();

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
