package com.shivi.trainticket.service;

import com.shivi.trainticket.exception.UserNotFoundException;
import com.shivi.trainticket.model.PurchaseRequest;
import com.shivi.trainticket.model.Receipt;
import com.shivi.trainticket.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrainTicketServiceTest {

    private TrainTicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketService = new TrainTicketServiceImpl();
    }

    @Test
    void purchaseTicket() {
        PurchaseRequest request = getPurchaseRequestUtil();
        User user = new User();
        user.setFirstName("ABC");
        user.setLastName("Tester");
        user.setEmail("ABC.Tester@example.com");
        request.setUser(user);

        Receipt receipt = ticketService.purchaseTicket(request);

        assertNotNull(receipt);
        assertEquals("London", receipt.getFrom());
        assertEquals("France", receipt.getTo());
        assertEquals("ABC", receipt.getUser().getFirstName());
        assertEquals("Tester", receipt.getUser().getLastName());
        assertEquals("ABC.Tester@example.com", receipt.getUser().getEmail());
        assertEquals(20.0, receipt.getPricePaid());
    }

    private static PurchaseRequest getPurchaseRequestUtil() {
        PurchaseRequest request = new PurchaseRequest();

        request.setFrom("London");
        request.setTo("France");
        request.setPricePaid(20.0);
        return request;
    }

    @Test
    void getReceipt() {
        PurchaseRequest request = getPurchaseRequestUtil();
        User user = new User();
        user.setFirstName("shivangi");
        user.setLastName("agrawal");
        user.setEmail("shivangi.agrawal@example.com");
        request.setUser(user);
        Receipt receipt = ticketService.purchaseTicket(request);

        Receipt retrievedReceipt = ticketService.getReceipt("shivangi.agrawal@example.com");
        assertNotNull(retrievedReceipt);
        assertEquals(receipt.getFrom(), retrievedReceipt.getFrom());
        assertEquals(receipt.getTo(), retrievedReceipt.getTo());
        assertEquals(receipt.getPricePaid(), retrievedReceipt.getPricePaid());
        assertEquals(receipt.getUser().getFirstName(), retrievedReceipt.getUser().getFirstName());
        assertEquals(receipt.getUser().getLastName(), retrievedReceipt.getUser().getLastName());
        assertEquals(receipt.getUser().getEmail(), retrievedReceipt.getUser().getEmail());

    }

    @Test
    void getUsersBySection() {
        PurchaseRequest request1 = new PurchaseRequest();
        User user1 = new User();
        user1.setFirstName("ABC");
        user1.setLastName("Tester");
        user1.setEmail("ABC.Tester@example.com");
        request1.setUser(user1);
        ticketService.purchaseTicket(request1);

        PurchaseRequest request2 = new PurchaseRequest();
        User user2 = new User();
        user2.setFirstName("shivangi");
        user2.setLastName("agrawal");
        user2.setEmail("shivangi.agrawal@example.com");
        request2.setUser(user2);
        ticketService.purchaseTicket(request2);

        Map<String, User> usersInSectionA = ticketService.getUsersBySection("A");
        assertEquals(1, usersInSectionA.size());
        assertTrue(usersInSectionA.containsKey("A1"));
        assertEquals(user1, usersInSectionA.get("A1"));

        Map<String, User> usersInSectionB = ticketService.getUsersBySection("B");
        assertEquals(1, usersInSectionB.size());
        assertTrue(usersInSectionB.containsKey("B1"));
        assertEquals(user2, usersInSectionB.get("B1"));
    }

    @Test
    void removeUser() {
        PurchaseRequest request = new PurchaseRequest();
        User user = new User();
        user.setFirstName("ABC");
        user.setLastName("Tester");
        user.setEmail("ABC.Tester@example.com");
        request.setUser(user);
        ticketService.purchaseTicket(request);

        ticketService.removeUser("ABC.Tester@example.com");
        assertThrows(UserNotFoundException.class, () -> ticketService.getReceipt("ABC.Tester@example.com"));

    }

    @Test
    void modifyUserSeat() {
        PurchaseRequest request = new PurchaseRequest();
        User user = new User();
        user.setFirstName("ABC");
        user.setLastName("Tester");
        user.setEmail("ABC.Tester@example.com");
        request.setUser(user);
        ticketService.purchaseTicket(request);

        PurchaseRequest request2 = new PurchaseRequest();
        User user2 = new User();
        user2.setFirstName("shivangi");
        user2.setLastName("agrawal");
        user2.setEmail("shivangi.agrawal@example.com");
        request2.setUser(user2);
        ticketService.purchaseTicket(request2);

        ticketService.modifyUserSeat("ABC.Tester@example.com", "A2");
        ticketService.modifyUserSeat("shivangi.agrawal@example.com", "A1");

        assertThrows(UserNotFoundException.class, () -> ticketService.modifyUserSeat("fake.email@example.com", "A3"));

    }
}
