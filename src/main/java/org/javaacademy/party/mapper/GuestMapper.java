package org.javaacademy.party.mapper;

import java.util.List;
import org.javaacademy.party.dto.GuestDto;
import org.javaacademy.party.entity.Guest;
import org.springframework.stereotype.Component;

@Component
public class GuestMapper {
    public List<GuestDto> toGuestsDto(List<Guest> guests) {
        return guests.stream()
                .map(this::toGuestDto)
                .toList();
    }

    public GuestDto toGuestDto(Guest guest) {
        GuestDto guestDto = new GuestDto();
        guestDto.setName(guest.getName());
        guestDto.setPhone(guest.getPhone());
        guestDto.setEmail(guest.getEmail());
        return guestDto;
    }
}
