package eu.inovator.zonky.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Created by novat on 12.10.2016.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Loan {


    /*
    "id" : 1,
      "name" : "Loan refinancing",
      "story" : "Dear investors, ...",
      "purpose" : "6",
      "photos" : [ {
                "name" : "6",
                "url" : "/loans/31959/photos/1987"
            } ],
      "nickName" : "zonky0",
      "termInMonths" : 42,
      "interestRate" : 0.059900,
      "rating" : "AAA",
      "topped" : null,
      "amount" : 200000.00,
      "remainingInvestment" : 152600.00,
      "investmentRate" : 0.237,
      "covered" : false,
      "datePublished" : "2016-04-19T18:25:41.208+02:00",
      "published" : true,
      "deadline" : "2016-04-26T18:23:53.101+02:00",
      "investmentsCount" : 72,
      "questionsCount" : 3,
      "region" : "6",
      "mainIncomeType" : "EMPLOYMENT"
     */

    private Integer id;
    private String name;
    private String story;
    private ZonedDateTime datePublished;
    private String nickName;
    private BigDecimal investmentRate;

}
