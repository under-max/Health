package com.example.demo.response.membership;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PayApproveResponse {

    private String aid;
    private String tid;
    private String cid;
    private String sid;
    private String partner_order_id;
    private String partner_user_id;
    private String payment_method_type;
    private Amount amount;
    private CardInfo card_info;
    private String item_name;
    private String item_code;
    private Integer quantity;
    private LocalDateTime created_at;
    private LocalDateTime approved_at;
    private String payload;

    @Data
    public static class Amount {
        private String total;
        private String tax_free;
        private String vat;
        private String point;
        private String discount;
        private String green_deposit;
    }

    @Data
    public static class CardInfo {
        private String purchase_corp;
        private String purchase_corp_code;
        private String issuer_corp;
        private String issuer_corp_code;
        private String kakaopay_purchase_corp;
        private String kakaopay_purchase_corp_code;
        private String kakaopay_issuer_corp;
        private String kakaopay_issuer_corp_code;
        private String bin;
        private String card_type;
        private String install_month;
        private String approved_id;
        private String card_mid;
        private String interest_free_install;
        private String card_item_code;
    }

}