package com.shivi.trainticket.controller;
// TrainTicketController.java

import com.shivi.trainticket.model.PurchaseRequest;
import com.shivi.trainticket.model.Receipt;
import com.shivi.trainticket.model.User;
import com.shivi.trainticket.exception.UserNotFoundException;
import com.shivi.trainticket.service.TrainTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/train")
public class TrainTicketController {

    @Autowired
    private TrainTicketService ticketService;

    @PostMapping("/purchase")
    public ResponseEntity<Receipt> purchaseTicket(@RequestBody PurchaseRequest request) {
        Receipt receipt = ticketService.purchaseTicket(request);
        return new ResponseEntity<>(receipt, HttpStatus.CREATED);
    }

    @GetMapping("/receipt/{email}")
    public ResponseEntity<Receipt> getReceipt(@PathVariable String email) {
        Receipt receipt = ticketService.getReceipt(email);
        return new ResponseEntity<>(receipt, HttpStatus.OK);
    }

    @GetMapping("/users/{section}")
    public ResponseEntity<Map<String, User>> getUsersBySection(@PathVariable String section) {
        Map<String, User> users = ticketService.getUsersBySection(section);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{email}")
    public ResponseEntity<String> removeUser(@PathVariable String email) {
        ticketService.removeUser(email);
        return new ResponseEntity<>("User removed from the train.", HttpStatus.OK);
    }

    @PutMapping("/modify/{email}")
    public ResponseEntity<String> modifyUserSeat(@PathVariable String email, @RequestParam String newSeat) {
        ticketService.modifyUserSeat(email, newSeat);
        return new ResponseEntity<>("User's seat modified successfully.", HttpStatus.OK);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
