package kr.co.nicepay.untact.receipt;


import kr.co.nicepay.untact.common.domain.Id;
import kr.co.nicepay.untact.receipt.dto.ReceiptRetrieveRequest;
import kr.co.nicepay.untact.receipt.dto.ReceiptRetrieveResponse;
import org.springframework.data.util.Pair;

public interface ReceiptQueryOutboundPort {
    Pair<Integer, ReceiptRetrieveResponse> retrieve(Id<String> tid, ReceiptRetrieveRequest receiptSearchReq);
}
