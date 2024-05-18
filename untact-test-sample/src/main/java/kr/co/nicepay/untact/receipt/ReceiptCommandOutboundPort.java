package kr.co.nicepay.untact.receipt;


import kr.co.nicepay.untact.common.domain.Id;
import kr.co.nicepay.untact.receipt.dto.ReceiptCancelRequest;
import kr.co.nicepay.untact.receipt.dto.ReceiptCancelResponse;
import kr.co.nicepay.untact.receipt.dto.ReceiptCreateRequest;
import kr.co.nicepay.untact.receipt.dto.ReceiptCreateResponse;
import org.springframework.data.util.Pair;

public interface ReceiptCommandOutboundPort {

    Pair<Integer, ReceiptCreateResponse> issue(ReceiptCreateRequest receiptCreateRequest);

    Pair<Integer, ReceiptCancelResponse> cancel(Id<String> tid, ReceiptCancelRequest receiptCancelRequest);
}
