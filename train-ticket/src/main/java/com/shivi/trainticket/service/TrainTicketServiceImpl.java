package com.shivi.trainticket.service;
// trainticketTicketServiceImpl.java


import com.shivi.trainticket.exception.UserNotFoundException;
import com.shivi.trainticket.model.PurchaseRequest;
import com.shivi.trainticket.model.Receipt;
import com.shivi.trainticket.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TrainTicketServiceImpl implements TrainTicketService {

    private final Map<String, User> users = new HashMap<>();
    private final Map<String, String> seatMap = new HashMap<>();

    @Override
    public Receipt purchaseTicket(PurchaseRequest request) {
        String seat = assignSeat();
        User user = request.getUser();
        users.put(seat, user);
        seatMap.put(user.getEmail(), seat);
        return new Receipt(request.getFrom(), request.getTo(), user, request.getPricePaid());
    }

    @Override
    public Receipt getReceipt(String email) {
        String seat = seatMap.get(email);
        if (seat == null)
            throw new UserNotFoundException("User with email " + email + " not found.");
        User user = users.get(seat);
        return new Receipt("London", "France", user, 20.0); // Assuming fixed price for all tickets
    }

    @Override
    public Map<String, User> getUsersBySection(String section) {
        Map<String, User> usersBySection = new HashMap<>();
        for (Map.Entry<String, User> entry : users.entrySet()) {
            if (entry.getKey().startsWith(section.toUpperCase())) {
                usersBySection.put(entry.getKey(), entry.getValue());
            }
        }
        return usersBySection;
    }

    @Override
    public void removeUser(String email) {
        String seat = seatMap.remove(email);
        if (seat == null)
            throw new UserNotFoundException("User with email " + email + " not found.");
        users.remove(seat);
    }

    @Override
    public void modifyUserSeat(String email, String newSeat) {
        String oldSeat = seatMap.get(email);
        if (oldSeat == null)
            throw new UserNotFoundException("User with email " + email + " not found.");
        User user = users.get(oldSeat);
        seatMap.put(email, newSeat);
        users.remove(oldSeat);
        users.put(newSeat, user);
    }

    private String assignSeat() {
        int sectionACount = 0;
        int sectionBCount = 0;
        for (String seat : users.keySet()) {
            if (seat.startsWith("A")) {
                sectionACount++;
            } else {
                sectionBCount++;
            }
        }
        if (sectionACount <= sectionBCount) {
            return "A" + (sectionACount + 1);
        } else {
            return "B" + (sectionBCount + 1);
        }
    }
}
