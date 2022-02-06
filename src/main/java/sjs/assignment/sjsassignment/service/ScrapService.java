package sjs.assignment.sjsassignment.service;

import lombok.RequiredArgsConstructor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sjs.assignment.sjsassignment.dto.ScrapRequestDto;
import sjs.assignment.sjsassignment.jwt.JwtTokenProvider;
import sjs.assignment.sjsassignment.model.ScrapEntity;
import sjs.assignment.sjsassignment.model.UserEntity;
import sjs.assignment.sjsassignment.repository.ScrapRepository;
import sjs.assignment.sjsassignment.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private static String URL = "https://codetest.3o3.co.kr/scrap/";

    private final JwtTokenProvider jwtTokenProvider;
    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;
    private final EntityManager em;

    @Transactional
    public ResponseEntity<String> getUserScrap( String token, Map<String,String> parma ) {

        // 토큰 유효성 및 만료일자 확인
        if ( jwtTokenProvider.validateToken( token ) ) {

            // API 스크랩 정보를 담은 후 scarpResponseDto 저장 후 JSON 전문 DB 저장
            ResponseEntity<String> scarpData = new RestTemplate().postForEntity( URL, parma, String.class );

            // json 담을 객체 생성
            ScrapRequestDto scrapRequestDto = new ScrapRequestDto();

            // API 스크랩 정보 저장
            scrapRequestDto.setScarpData( scarpData.getBody() );
            scrapRepository.save( scrapRequestDto.toEntity() ).getId();

            return scarpData;
        } else {
            throw new IllegalArgumentException(" 토큰의 유효성 및 만료일자 확인해주세요. ");
        }
    }

    @Transactional
    public Map<String,String> getUserRefund( String token ) throws ParseException {
        // 토큰 유효성 및 만료일자 확인
        if ( jwtTokenProvider.validateToken( token ) ) {

            // 일대일 관계로써 ScarpEntity -> scarpData 값을 idx기준으로 정보 가져온다.
            String userId = jwtTokenProvider.getUserId( token );
            UserEntity idx = userRepository.findByUserId( userId );

            TypedQuery<ScrapEntity> query = em.createQuery("" +
                    "SELECT p FROM ScrapEntity p WHERE p.id = :id", ScrapEntity.class);
            query.setParameter("id", idx.getId());

            // 환급액 관련 Json 출력 및 계산식, 이미 스크랩데이터는 문자열로 들어간 상태지만 편의를 위해 Json 파싱 진행후 데이터 조회
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(query.getSingleResult().getScarpData());
            JSONObject jsonList = (JSONObject)jsonObject.get("jsonList");

            // scrap_2
            JSONArray scrap002 = (JSONArray)jsonList.get("scrap002");
            JSONObject scrap002_data = (JSONObject)scrap002.get(0);

            // scrap_1
            JSONArray scrap001 = (JSONArray)jsonList.get("scrap001");
            JSONObject scrap001_data = (JSONObject)scrap001.get(0);

            // 근로소득 세액공제 한도 계산식
            int taxCreditLimit = 0;
            int totalPayment = Integer.parseInt( String.valueOf( scrap001_data.get("총지급액") ) );

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

            // 근로소득 세액공제 계산식
            int taxCredit = 0;
            int totalAmountUsed = Integer.parseInt( String.valueOf( scrap002_data.get("총사용금액") ) );

            if( totalAmountUsed <= 1300000 ) {
                taxCredit =  ( totalAmountUsed / 100 ) * 55;
            } else if( totalAmountUsed > 1300000 ) {
                taxCredit = (int) (715000 + ( ( totalAmountUsed - 1300000 ) * 0.3 ));
            }

            // 환급액
            int refund = Math.min(taxCreditLimit,taxCredit);
            String name = (String) scrap001_data.get("이름");

            // 응답 데이터 양식 맞추기 메소드 호출
            String taxCreditLimitChange = moneyUnitChange(taxCreditLimit);
            String taxCreditChange = moneyUnitChange(taxCredit);
            String refundChange = moneyUnitChange(refund);

            Map<String,String> rts = new LinkedHashMap<>();
            rts.put("이름",  name );
            rts.put("한도",  taxCreditLimitChange );
            rts.put("공제액", taxCreditChange );
            rts.put("환급액", refundChange );

            return rts;

        } else {
            throw new IllegalArgumentException(" 토큰의 유효성 및 만료일자 확인해주세요. ");
        }
    }

    /** 각금액의 대한 응답 데이터 양식 맞추기 위한 메소드 처리 */
    public String moneyUnitChange(int money) {
        String moneyChange = String.valueOf(money);

        String rtn = "";

        rtn = (String) moneyChange.subSequence(0,2);
        rtn += "만 ";
        rtn += (String) moneyChange.subSequence(1,2);
        rtn += "천원";

        return rtn;
    }

}
