package kr.co.nicepay.untact.terms;


import kr.co.nicepay.untact.terms.dto.event.CardEventRequest;
import kr.co.nicepay.untact.terms.dto.event.CardEventResponse;
import kr.co.nicepay.untact.terms.dto.interestfree.CardInterestFeeRequest;
import kr.co.nicepay.untact.terms.dto.interestfree.CardInterestFreeResponse;
import kr.co.nicepay.untact.terms.dto.term.TermsRequest;
import kr.co.nicepay.untact.terms.dto.term.TermsResponse;
import org.springframework.data.util.Pair;

public interface TermsQueryOutboundPort {
    Pair<Integer, TermsResponse> terms(TermsRequest termsRequest);

    Pair<Integer, CardEventResponse> cardEvent(CardEventRequest cardEventRequest);

    Pair<Integer, CardInterestFreeResponse> cardInterestFree(CardInterestFeeRequest cardInterestFeeRequest);
}
