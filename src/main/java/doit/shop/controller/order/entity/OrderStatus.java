package doit.shop.controller.order.entity;

public enum OrderStatus {
    // 주문 완료
    ORDERED,

    // 입금 완료
    PAID,

    // 준비중
    PROCESSING,

    // 출고 완료
    SHIPPED,

    // 배송 완료
    DELIVERED,

    // 완료
    COMPLETE,

    // 취소됨
    CANCELED,

    // 반품됨
    RETURNED,

    // 반품신청
    RETURNED_REQUESTED,

    // 취소신청
    CANCEL_REQUESTED,
}
