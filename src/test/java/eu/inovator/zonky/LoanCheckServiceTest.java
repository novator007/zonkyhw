package eu.inovator.zonky;

import eu.inovator.zonky.client.ZonkyClient;
import eu.inovator.zonky.entity.Loan;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by novat on 13.10.2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoanCheckServiceTest {


    @Mock
    private ZonkyClient zonkyClient;

    @InjectMocks
    private LoanCheckService loanCheckService = new LoanCheckService();

    @Before
    public void init() {
        loanCheckService = new LoanCheckService();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetNewLoans() throws Exception {
        when(zonkyClient.getLoansAfter(any())).thenReturn(createLoanList());
        List<Loan> newLoans = loanCheckService.getNewLoans(ZonedDateTime.now());
        Assert.assertTrue(newLoans.size() == 3);
        Loan loan = newLoans.iterator().next();
        Assert.assertEquals(Integer.valueOf(1), loan.getId());
    }

    @Test
    public void testCheckForLoans() {
        when(zonkyClient.getLoansAfter(any())).thenReturn(createLoanList());
        ZonedDateTime lastCheckTimeBefore = loanCheckService.getLastCheckTime();
        loanCheckService.checkNewLoans();
        Assert.assertNotEquals(lastCheckTimeBefore, loanCheckService.getLastCheckTime());
    }

    @Test
    public void testSetLastCheckedDate() {
        LoanCheckService instance = new LoanCheckService();
        ZonedDateTime timeInFuture = ZonedDateTime.now().plusDays(2);
        instance.setLastCheckTime(timeInFuture);
        Assert.assertEquals(timeInFuture, instance.getLastCheckTime());

    }

    @Test
    public void testSetLastCheckedDateNotOlder() {
        LoanCheckService instance = new LoanCheckService();
        ZonedDateTime timeInFuture = ZonedDateTime.now().plusDays(2);
        instance.setLastCheckTime(timeInFuture);
        Assert.assertEquals(timeInFuture, instance.getLastCheckTime());
        ZonedDateTime timeInPast = ZonedDateTime.now().minusDays(2);
        instance.setLastCheckTime(timeInPast);
        Assert.assertEquals(timeInFuture, instance.getLastCheckTime());
    }


    public static List<Loan> createLoanList() {
        Loan loan1 = new Loan();
        loan1.setId(1);
        loan1.setName("Test loan 1");
        loan1.setDatePublished(ZonedDateTime.now().minusDays(10));
        Loan loan2 = new Loan();
        loan2.setId(2);
        loan2.setName("Test loan 2");
        loan2.setDatePublished(ZonedDateTime.now().plusDays(1));
        Loan loan3 = new Loan();
        loan3.setId(3);
        loan3.setName("Test loan 3");
        loan3.setDatePublished(ZonedDateTime.now().minusSeconds(10));
        return Arrays.asList(loan3, loan2, loan1);
    }

}