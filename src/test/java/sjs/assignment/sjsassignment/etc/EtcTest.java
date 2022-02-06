package sjs.assignment.sjsassignment.etc;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

public class EtcTest {

    @Test
    public void 돈_단위_변경_성공() throws Exception {

        // given
        int money = 130000;
        String moneyChange = String.valueOf(money);

        // when
        String rtn = "";

        rtn = (String) moneyChange.subSequence(0,2);
        rtn += "만 ";
        rtn += (String) moneyChange.subSequence(1,2);
        rtn += "천원";

        // then
        System.out.println(rtn);
        Assertions.assertNotNull(rtn);
    }

    @Test
    public void 돈_단위_변경_실패() throws Exception {

        // given
        int money = 0;
        String moneyChange = String.valueOf(money);

        // when
        String rtn = "";

        rtn = (String) moneyChange.subSequence(0,2);
        rtn += "만 ";
        rtn += (String) moneyChange.subSequence(1,2);
        rtn += "천원";

        // then
        Assertions.assertNotNull(rtn);
    }

    @Test
    public void 외부API_연동_성공() {

        String URL = "https://codetest.3o3.co.kr/scrap/";

        Map<String,String> parma = new LinkedHashMap<>();
        parma.put("name","홍길동");
        parma.put("regNo", "860824-1655068");

        ResponseEntity<String> scarpData = new RestTemplate().postForEntity( URL, parma, String.class );

        System.out.println(scarpData.getBody());
    }

    @Test
    public void 외부API_연동_실패() {

        String URL = "https://codetest.3o3.co.kr/scrap/";

        Map<String,String> parma = new LinkedHashMap<>();
        parma.put("name","홍길동111");
        parma.put("regNo", "860824-1655068");

        ResponseEntity<String> scarpData = new RestTemplate().postForEntity( URL, parma, String.class );

        System.out.println(scarpData.getBody());
    }

    @Test
    public void 환급금_세액공제_계산() {

        // 근로소득 세액공제 계산식
        int taxCredit = 0;
        // 1300000 이하
        int totalAmountUsed = 1200000;
        // 33000000 이상
        int totalPaymentMore = 1400000;

        if( totalAmountUsed <= 1300000 ) {
            taxCredit =  ( totalAmountUsed / 100 ) * 55;
        } else if( totalAmountUsed > 1300000 ) {
            taxCredit = (int) (715000 + ( ( totalAmountUsed - 1300000 ) * 0.3 ));
        }

        System.out.println(taxCredit);

    }

    @Test
    public void 환급금_세액공제한도_계산() {

        // 근로소득 세액공제 한도 계산식
        int taxCreditLimit = 0;
        // 33000000 이하
        int totalPayment = 32000000;
        // 33000000 초과 및 70000000 이상
        int totalPaymentMore = 34000000;
        // 70000000 초과
        int totalPaymentExcess = 34000000;

        if( totalPayment <= 33000000 ) {
            taxCreditLimit = 740000;
        } else if( totalPayment > 33000000 && totalPayment <= 70000000 ) {
            taxCreditLimit = (int) (740000 - ( ( totalPayment - 33000000 ) * 0.008 ));
            if( taxCreditLimit < 660000 ) {
                taxCreditLimit = 660000;
            }
        } else if(  totalPayment > 70000000 ) {
            taxCreditLimit = (int) (660000 - ( ( totalPayment - 70000000 ) *  0.5 ));
            if( taxCreditLimit < 500000 ) {
                taxCreditLimit = 500000;
            }
        }

        System.out.println(taxCreditLimit);

    }

}
