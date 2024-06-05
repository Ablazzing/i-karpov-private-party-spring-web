package org.javaacademy.party.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.javaacademy.party.dto.GuestDto;
import org.javaacademy.party.mapper.GuestMapper;
import org.javaacademy.party.repository.GuestRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuestService {
    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    public GuestDto save(GuestDto guestDto, String dbUser, String dbPassword) {
        try {
            return guestMapper.toGuestDto(guestRepository.save(guestDto, dbUser, dbPassword));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<GuestDto> findAll(String dbUser, String dbPassword) {
        try {
            return guestMapper.toGuestsDto(guestRepository.findAll(dbUser, dbPassword));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
