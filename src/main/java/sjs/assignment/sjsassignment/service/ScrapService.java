package sjs.assignment.sjsassignment.service;

import lombok.RequiredArgsConstructor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sjs.assignment.sjsassignment.dto.ScrapRequestDto;
import sjs.assignment.sjsassignment.jwt.JwtTokenProvider;
import sjs.assignment.sjsassignment.model.ScrapEntity;
import sjs.assignment.sjsassignment.model.UserEntity;
import sjs.assignment.sjsassignment.repository.ScrapRepository;
import sjs.assignment.sjsassignment.repository.UserRepository;

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

    @Transactional
    @Cacheable(value="data")
    public ResponseEntity<String> getUserScrap( String token, Map<String,String> parma ) {

        // 토큰 유효성 및 만료일자 및 해당 아이디만 조회
        // 변경 : 인증토큰으로 해당 유저네임이랑 같은경우 + 기존 URL 조회시 캐싱 속도 개선
        if ( jwtTokenProvider.validateToken( token ) && jwtTokenProvider.getUserName( token ).equals( parma.get("name") ) ) {

            // API 스크랩 정보를 담은 후 scarpResponseDto 저장 후 JSON 전문 DB 저장
            ResponseEntity<String> scarpData = new RestTemplate().postForEntity( URL, parma, String.class );

            // json 담을 객체 생성
            ScrapRequestDto scrapRequestDto = new ScrapRequestDto();

            // API 스크랩 정보 저장
            scrapRequestDto.setScarpData( scarpData.getBody() );
            scrapRepository.save( scrapRequestDto.toEntity() ).getId();

            return scarpData;
        } else {
            throw new IllegalArgumentException(" 토큰의 유효성 및 만료일자 및 유저이름을 확인해주세요. ");
        }
    }

    @Transactional
    public Map<String,String> getUserRefund( String token ) throws ParseException {
        // 토큰 유효성 및 만료일자 확인
        if ( jwtTokenProvider.validateToken( token ) ) {

            // 토큰을 이용하여 UserId를 얻어오고 그후 UserEntity에서 토큰의 UserId와 대응하는 키값을 가져온 후 스크랩데이터를 가져온다.
            String userId = jwtTokenProvider.getUserId( token );
            UserEntity idx = userRepository.findByUserId( userId );
            // 변경 : jpql을 서비스 레이어에서 제거 -> 구조자체 문제 , 서비스 레이어에는 비즈니스 로직만 존재해야함.
            ScrapEntity scrapData = scrapRepository.findByScarpData( idx.getId() );

            // 환급액 관련 Json 출력 및 계산식, 이미 스크랩데이터는 문자열로 들어간 상태지만 편의를 위해 Json 파싱 진행후 데이터 조회
            Map<String,String> scrapDatas = jsonReturn( scrapData.getScarpData() );

            // 근로소득 세액공제 한도 -> 함수화
            int totalPayment = Integer.parseInt( String.valueOf( scrapDatas.get("총지급액") ) );
            int taxCreditLimit = getTaxCreditLimit( totalPayment );

            // 근로소득 세액공제 -> 함수화
            int totalAmountUsed = Integer.parseInt( String.valueOf( scrapDatas.get("총사용금액") ) );
            int taxCredit = getTotalAmountUsed( totalAmountUsed );

            // 환급액
            int refund = Math.min(taxCreditLimit,taxCredit);
            String name = (String) scrapDatas.get("이름");

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

    /** 스크랩 데이터 리턴 함수 */
    public Map<String,String> jsonReturn( String scrapData ) throws ParseException {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse( scrapData );
        JSONObject jsonList = (JSONObject)jsonObject.get("jsonList");

        // scrap_2
        JSONArray scrap002 = (JSONArray)jsonList.get("scrap002");
        JSONObject scrap002_data = (JSONObject)scrap002.get(0);

        // scrap_1
        JSONArray scrap001 = (JSONArray)jsonList.get("scrap001");
        JSONObject scrap001_data = (JSONObject)scrap001.get(0);

        Map<String,String> scarpData = new LinkedHashMap<>();
        scarpData.put("이름", String.valueOf(scrap001_data.get("이름")));
        scarpData.put("총지급액", String.valueOf(scrap001_data.get("총지급액")));
        scarpData.put("총사용금액", String.valueOf(scrap002_data.get("총사용금액")));

        return scarpData;
    }

    /** 근로소득 세액공제 한도 계산식 */
    public int getTaxCreditLimit( int totalPayment ) {

        int limitMoney = 0;

        if( totalPayment <= 33000000 ) {
            limitMoney = 740000;
        } else if( totalPayment > 33000000 && totalPayment <= 70000000 ) {
            limitMoney = (int) (740000 - ( ( totalPayment - 33000000 ) * 0.008 ));
            if( limitMoney < 660000 ) {
                limitMoney = 660000;
            }
        } else if(  totalPayment > 70000000 ) {
            limitMoney = (int) (660000 - ( ( totalPayment - 70000000 ) *  0.5 ));
            if( limitMoney < 500000 ) {
                limitMoney = 500000;
            }
        }
        return limitMoney;
    }

    /** 근로소득 세액공제 계산식 */
    public int getTotalAmountUsed( int totalAmountUsed ) {

        int deducted = 0;

        if( totalAmountUsed <= 1300000 ) {
            deducted =  ( totalAmountUsed / 100 ) * 55;
        } else if( totalAmountUsed > 1300000 ) {
            deducted = (int) (715000 + ( ( totalAmountUsed - 1300000 ) * 0.3 ));
        }

        return deducted;

    }

}
