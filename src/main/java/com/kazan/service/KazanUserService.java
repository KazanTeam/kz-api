package com.kazan.service;

import com.kazan.dto.KazanUserDto;

public interface KazanUserService {

    KazanUserDto createUser(KazanUserDto kazanUserDto);

    KazanUserDto update(KazanUserDto kazanUserDto);

    KazanUserDto findById(Integer userId);

}
