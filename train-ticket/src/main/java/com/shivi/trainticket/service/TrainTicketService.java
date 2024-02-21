package com.shivi.trainticket.service;
// TrainTicketService.java


import com.shivi.trainticket.model.PurchaseRequest;
import com.shivi.trainticket.model.Receipt;
import com.shivi.trainticket.model.User;

import java.util.Map;

public interface TrainTicketService {
    Receipt purchaseTicket(PurchaseRequest request);

    Receipt getReceipt(String email);

    Map<String, User> getUsersBySection(String section);

    void removeUser(String email);

    void modifyUserSeat(String email, String newSeat);
}
