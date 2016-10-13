package eu.inovator.zonky.client;

import eu.inovator.zonky.entity.Loan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by novat on 12.10.2016.
 */
@Service
@Slf4j
public class ZonkyClient {


    @Autowired
    private RestTemplate template;

    @Value("${marketPlaceUrl:https://api.zonky.cz/loans/marketplace}")
    private String marketPlaceUrl;


    public List<Loan> getLoansAfter(ZonedDateTime dateTime) {
        log.debug("calling marketplace service");
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Order", "-datePublished");
        headers.add("X-Page", "0");
        headers.add("X-Size", "50");

        List<Loan> result = new ArrayList<>();
        boolean searchFinished = false;

        int page = 0;
        while (!searchFinished) {
            headers.set("X-Page", String.valueOf(page));
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<Loan[]> responseEntity = template.exchange(marketPlaceUrl, HttpMethod.GET, entity, Loan[].class);
            Loan[] loans = responseEntity.getBody();
            log.trace("Response from zonky {}", Arrays.deepToString(loans));
            if (loans.length > 0) {
                for (Loan loan : loans) {
                    if (loan.getDatePublished().isAfter(dateTime)) {
                        result.add(loan);
                    } else {
                        searchFinished = true;
                        break;
                    }
                }
            } else {
                searchFinished = true;
            }

            log.debug("Headers are {}", responseEntity.getHeaders());
            page++;
        }
        return result;
    }
}
