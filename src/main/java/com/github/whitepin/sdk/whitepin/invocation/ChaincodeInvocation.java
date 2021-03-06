package com.github.whitepin.sdk.whitepin.invocation;

import java.util.List;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;

import com.github.whitepin.sdk.whitepin.context.ConditionType;
import com.github.whitepin.sdk.whitepin.context.OrderType;
import com.github.whitepin.sdk.whitepin.context.ReportType;
import com.github.whitepin.sdk.whitepin.vo.ScoreVo;
import com.github.whitepin.sdk.whitepin.vo.TradeVo;
import com.github.whitepin.sdk.whitepin.vo.UserVo;

public interface ChaincodeInvocation {
    /**
     * 사용자 생성 
     * @param userTkn :: 사용자 토큰
     * @return
     */
    boolean addUser(Channel channel, HFClient client, String userTkn) throws Exception;

    /**
     * 사용자 조회
     * FN02
     * @param userTkn :: 사용자 토큰
     */
    UserVo queryUser(Channel channel, HFClient client, String userTkn) throws Exception;

    /**
     * 사용자 조회 ( 누적 거래 내역 조회 )
     * FN01
     */
    UserVo queryTotalUser(Channel channel, HFClient client) throws Exception;

    /**
     * 거래 생성 
     * @param tradeId     :: 거래 ID
     * @param serviceCode :: 서비스 Code ( 000000 : airbnb, 000001 : carrotmarket, .... )
     * @param sellerTkn   :: 판매자 토큰
     * @param buyerTkn    :: 구매자 토큰
     */
    boolean createTrade(Channel channel, HFClient client, String tradeId, String serviceCode,
                        String sellerTkn, String buyerTkn) throws Exception;

    /**
     * queryString으로 거래이력 조회.
     * @param queryString  :: 메소드로 제공하지 않는 기능들에 대해서 조회 가능 쿼리 작성
     * @return
     * @throws Exception
     */
    List<TradeVo> queryTradeWithQueryString(Channel channel, HFClient client, String queryString)
            throws Exception;

    /**
     * 사용자로 거래이력 조회.
     * @param userTkn      :: 사용자 정보
     * @param condition    :: 사용자의 조건 (판매 : "sell", 구매 : "buy", 둘다 : "all")
     * @param ordering     :: 시간에 대한 ordering. (default is desc.)
     * @param reportType   :: 일반조회, 페이지 조회(normal, page, default is normal)
     * @param pageSize     :: (optional) : 한 페이지 사이즈
     * @param pageNum      :: (optional) : 페이지 번호
     * @param bookmark     :: (optional) : 북마크 (첫 페이지 조회 또는 default는 "")
     * @return
     * @throws Exception
     */
    List<TradeVo> queryTradeWithUser(Channel channel, HFClient client, String userTkn,
                                     ConditionType conditionType, OrderType orderType,
                                     ReportType reportType, String pageSize,
                                     String pageNum, String bookmark) throws Exception;

    /**
     * 사용자로 거래이력 조회.
     * FN03
     * @param userTkn      :: 사용자 정보
     * @param condition    :: 사용자의 조건 (판매 : "sell", 구매 : "buy", 둘다 : "all")
     * @param ordering     :: 시간에 대한 ordering. (default is desc.)
     * @param pageSize     :: (optional) : 한 페이지 사이즈
     * @param pageNum      :: (optional) : 페이지 번호
     * @param bookmark     :: (optional) : 북마크 (첫 페이지 조회 또는 default는 "")
     * @return
     * @throws Exception
     */
    List<TradeVo> queryTradeWithUser(Channel channel, HFClient client, String userTkn,
                                     ConditionType conditionType, OrderType orderType, String pageSize,
                                     String pageNum, String bookmark)
            throws Exception;

    /**
     * // 사용자, 서비스 코드로 거래이력 조회. (판매, 구매, 모두 : sell, buy, all)
     * @param userTkn      :: 사용자 정보
     * @param serviceCode  :: 서비스 코드
     * @param condition    :: 사용자의 조건 (판매 : "sell", 구매 : "buy", 둘다 : "all")
     * @param ordering     :: 시간에 대한 ordering. (default is desc.)
     * @param reportType   :: 일반조회, 페이지 조회(normal, page, default is normal)
     * @param pageSize     :: (optional) : 한 페이지 사이즈
     * @param pageNum      :: (optional) : 한 페이지 사이즈
     * @param bookmark     :: (optional) : 북마크 (첫 페이지 조회 또는 default는 "")
     * @return
     * @throws Exception
     */
    List<TradeVo> queryTradeWithUserService(Channel channel, HFClient client, String userTkn,
                                            String serviceCode,
                                            ConditionType conditionType, OrderType orderType,
                                            ReportType reportType, String pageSize, String pageNum,
                                            String bookmark) throws Exception;

    /**
     * 거래 조회 (서비스 코드로 조회)
     * @param serviceCode  :: 서비스 코드
     * @param ordering     :: 시간에 대한 ordering. (default is desc.)
     * @param reportType   :: 일반조회, 페이지 조회(normal, page, default is normal)
     * @param pageSize     :: (optional) : 한 페이지 사이즈
     * @param pageNum      :: (optional) : 페이지 번호
     * @param bookmark     :: (optional) : 북마크 (첫 페이지 조회 또는 default는 "")
     * @return
     * @throws Exception
     */
    List<TradeVo> queryTradeWithService(Channel channel, HFClient client, String serviceCode,
                                        OrderType orderType,
                                        ReportType reportType, String pageSize, String pageNum,
                                        String bookmark) throws Exception;

    /**
     * 거래 조회
     * @param tradeId     :: 거래 ID
     */
    TradeVo queryTradeWithId(Channel channel, HFClient client, String tradeId) throws Exception;

    /**
     * 임시 평가정수 조회
     * @param scoreKey    :: 평가 키
     */
    ScoreVo queryScoreTemp(Channel channel, HFClient client, String scoreKey) throws Exception;

    /**
     * 임시평가점수 조회 query 작성 후 추가
     * @param tradeId     :: 거래 ID
     */
    ScoreVo queryScoreTempWithTradeId(Channel channel, HFClient client, String tradeId)
            throws Exception;

    /**
     * 유효 기간이 지난 임시평가점수 조회 query
     * @return
     */
    List<ScoreVo> queryScoreTempWithExpired(Channel channel, HFClient client) throws Exception;

    /**
     * 거래 완료 처리 (판매자 또는 구마재)
     * FN05
     * @param tradeId     :: 거래 ID
     * @param userTkn     :: 사용자 토큰
     */
    boolean closeTrade(Channel channel, HFClient client, String tradeId, String userTkn)
            throws Exception;

    /**
     * 임시 평가점수 등록 (판매자 또는 구마재)
     * @param tradeId     :: 거래 ID
     * @param userTkn     :: 사용자 토큰
     * @param scoreOrigin :: 평가 점수 e.g.) "[3,4,5]" 의 format
     * @param key         :: encryption에 사용될 string
     */
    boolean enrollTempScore(Channel channel, HFClient client, String tradeId, String userTkn,
                            String scoreOrigin, String key) throws Exception;

    /**
     * 임시 평가점수 등록 (판매자 또는 구마재)
     * FN04
     * @param tradeId     :: 거래 ID
     * @param scoreOrigin :: 평가 점수 e.g.) "[3,4,5]" 의 format
     * @param userTkn     :: 사용자 토큰
     */
    boolean enrollTempScore(Channel channel, HFClient client, String tradeId, String scoreOrigin,
                            String userTkn) throws Exception;

    /**
     * 거래 점수 등록 (판매자, 구매자 동시에)
     * @param tradeId     :: 거래 ID
     * @param key         :: 암호화 해제 키
     */
    boolean enrollScore(Channel channel, HFClient client, String tradeId, String key) throws Exception;
}
