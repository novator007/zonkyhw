package eu.inovator.zonky.client;

import eu.inovator.zonky.entity.Loan;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by novat on 12.10.2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ZonkyClientTest {


    @InjectMocks
    ZonkyClient client = new ZonkyClient();

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ResponseEntity<Loan[]> response;

    @Before
    public void init() {
        client = new ZonkyClient();
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testSingleCall() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Loan[].class))).thenReturn(response);
        when(response.getBody()).thenReturn(createResponse());
        List<Loan> loansAfter = client.getLoansAfter(ZonedDateTime.now());
        Assert.assertTrue(loansAfter.size() == 2);
    }

    @Test
    public void testMultipleCall() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Loan[].class))).thenReturn(response);
        when(response.getBody()).thenReturn(createFutureResponse()).thenReturn(createResponse()).thenThrow(new RuntimeException("Here lives dragons"));
        List<Loan> loansAfter = client.getLoansAfter(ZonedDateTime.now());
        Assert.assertTrue(loansAfter.size() == 5);
    }


    private Loan[] createResponse() {
        Loan loan1 = new Loan();
        loan1.setId(1);
        loan1.setName("Test loan 1");
        loan1.setDatePublished(ZonedDateTime.now().plusDays(2));
        Loan loan2 = new Loan();
        loan2.setId(2);
        loan2.setName("Test loan 2");
        loan2.setDatePublished(ZonedDateTime.now().plusDays(1));
        Loan loan3 = new Loan();
        loan3.setId(3);
        loan3.setName("Test loan 3");
        loan3.setDatePublished(ZonedDateTime.now().minusSeconds(10));
        return new Loan[]{loan1, loan2, loan3};
    }

    private Loan[] createFutureResponse() {
        Loan loan1 = new Loan();
        loan1.setId(1);
        loan1.setName("Test loan 1");
        loan1.setDatePublished(ZonedDateTime.now().plusDays(5));
        Loan loan2 = new Loan();
        loan2.setId(2);
        loan2.setName("Test loan 2");
        loan2.setDatePublished(ZonedDateTime.now().plusDays(4));
        Loan loan3 = new Loan();
        loan3.setId(3);
        loan3.setName("Test loan 3");
        loan3.setDatePublished(ZonedDateTime.now().plusDays(3));
        return new Loan[]{loan1, loan2, loan3};
    }

}