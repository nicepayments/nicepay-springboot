package kr.co.nicepay.untact.subscribe.dto;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kr.co.nicepay.untact.common.domain.Id;
import kr.co.nicepay.untact.subscribe.domain.EncData;
import kr.co.nicepay.untact.subscribe.domain.EncMode;
import kr.co.nicepay.untact.subscribe.domain.EncryptStrategyFactory;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SubscribeCreateRequest {

    // Mandatory
    private final Id<String> orderId;
    private final String encData;

    // Optional
    private final String buyerName;
    private final String buyerEmail;
    private final String buyerTel;

    // null: AES128, A2: AES256
    @Enumerated(EnumType.STRING)
    private final EncMode encMode;

    // signData 사용 시 ediDate 필수
    private final String ediDate;
    private final String signData;
    // default : utf-8
    private final String returnCharSet;

    private SubscribeCreateRequest(Builder builder) {
        this.orderId = builder.orderId;
        this.buyerName = builder.buyerName;
        this.buyerEmail = builder.buyerEmail;
        this.buyerTel = builder.buyerTel;
        this.encMode = builder.encMode;
        this.encData = EncryptStrategyFactory.of(encMode)
                .encrypt(builder.encData, builder.secretKey);
        this.ediDate = builder.ediDate;
        this.signData = builder.signData;
        this.returnCharSet = builder.returnCharSet;
    }

    public static class Builder {

        // Mandatory
        private final Id<String> orderId;

        // Optional
        private String buyerName;
        private String buyerEmail;
        private String buyerTel;

        // null: AES128, A2: AES256
        private EncMode encMode;
        private String encData;
        private String secretKey;


        // signData 사용 시 ediDate 필수
        private String ediDate;
        private String signData;
        // default : utf-8
        private String returnCharSet;

        public Builder(Id<String> orderId) {
            this.orderId = orderId;
        }

        public Builder encData(EncData data, String secretKey) {
            this.encData = data.getValue();
            this.secretKey = secretKey;
            return this;
        }

        public Builder buyerName(String buyerName) {
            this.buyerName = buyerName;
            return this;
        }

        public Builder buyerEmail(String buyerEmail) {
            this.buyerEmail = buyerEmail;
            return this;
        }

        public Builder buyerTel(String buyerTel) {
            this.buyerTel = buyerTel;
            return this;
        }

        public Builder encMode(EncMode encMode) {
            this.encMode = encMode;
            return this;
        }

        public Builder ediDate(String ediDate) {
            this.ediDate = ediDate;
            return this;
        }

        public Builder signData(String signData) {
            this.signData = signData;
            return this;
        }

        public Builder returnCharSet(String returnCharSet) {
            this.returnCharSet = returnCharSet;
            return this;
        }

        public SubscribeCreateRequest build() {
            return new SubscribeCreateRequest(this);
        }
    }

}
