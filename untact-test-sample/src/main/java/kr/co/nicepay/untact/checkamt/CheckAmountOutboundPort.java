package kr.co.nicepay.untact.checkamt;


import kr.co.nicepay.untact.checkamt.dto.CheckAmountRequest;
import kr.co.nicepay.untact.checkamt.dto.CheckAmountResponse;
import kr.co.nicepay.untact.common.domain.Id;
import org.springframework.data.util.Pair;

public interface CheckAmountOutboundPort {

    Pair<Integer, CheckAmountResponse> checkAmount(Id<String> tid, CheckAmountRequest checkAmountRequest);
}
