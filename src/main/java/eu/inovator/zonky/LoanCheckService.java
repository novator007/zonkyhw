package eu.inovator.zonky;

import eu.inovator.zonky.client.ZonkyClient;
import eu.inovator.zonky.entity.Loan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by novat on 13.10.2016.
 */
@Service
@Slf4j
public class LoanCheckService {

    @Autowired
    private ZonkyClient client;


    private ZonedDateTime lastCheckTime = ZonedDateTime.now();

    @Scheduled(cron = "1 */5 * * * *")
    public void checkNewLoans() {
        log.debug("Checking for new loans. LastCheckTime is {}, current time is {}", lastCheckTime, ZonedDateTime.now());
        List<Loan> newLoans = getNewLoans(getLastCheckTime());
        if (newLoans.size() > 0) {
            setLastCheckTime(newLoans.get(newLoans.size() - 1).getDatePublished());
        } else {
            log.info("No new loans detected");
        }
        newLoans.forEach(LoanCheckService::printLoan);
    }

    public List<Loan> getNewLoans(ZonedDateTime time) {
        assert time != null;
        //check errors / better error handling
        List<Loan> loansAfter = client.getLoansAfter(time);
        log.debug("Loans from client returned {}", loansAfter.size());
        List<Loan> result = loansAfter.stream().sorted((o1, o2) -> o1.getDatePublished().compareTo(o2.getDatePublished()))
                .collect(Collectors.toList());
        return result;
    }

    public static void printLoan(Loan loan) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n****************************\n")
                .append("Loan id = ").append(loan.getId()).append(",Time created: ").append(loan.getDatePublished().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .append(", Loan name =").append(loan.getName())
                .append("\n*********************************\n");
        log.info(sb.toString());
    }

    public synchronized ZonedDateTime getLastCheckTime() {
        return lastCheckTime;
    }

    synchronized void setLastCheckTime(ZonedDateTime lastCheckTime) {
        assert lastCheckTime != null;
        if (lastCheckTime.compareTo(this.lastCheckTime) > 0) {
            this.lastCheckTime = lastCheckTime;
        }
    }
}

