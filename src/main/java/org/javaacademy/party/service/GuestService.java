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

    public void save(GuestDto guestDto, String dbUser, String dbPassword) {
        guestRepository.save(guestDto, dbUser, dbPassword);
    }

    public List<GuestDto> findAll(String dbUser, String dbPassword) {
        return guestMapper.toGuestsDto(guestRepository.findAll(dbUser, dbPassword));
    }
}
