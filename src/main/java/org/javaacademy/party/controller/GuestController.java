package org.javaacademy.party.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.javaacademy.party.dto.GuestDto;
import org.javaacademy.party.service.GuestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GuestController {
    private final GuestService guestService;

    @PostMapping("/add-guest")
    public ResponseEntity<?> addGuest(@RequestHeader("db-user") String dbUser,
                                             @RequestHeader("db-password") String dbPassword,
                                             @RequestBody GuestDto guestDto) {
        guestService.save(guestDto, dbUser, dbPassword);
        return ResponseEntity.status(202).build();
    }

    @GetMapping("/all-guest")
    public ResponseEntity<List<GuestDto>> getGuests(@RequestHeader("db-user") String dbUser,
                                                    @RequestHeader("db-password") String dbPassword) {
        return ResponseEntity.ok(guestService.findAll(dbUser, dbPassword));
    }
}
