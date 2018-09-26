package com.kazan.rest;

import com.kazan.dto.KazanUserDto;
import com.kazan.dto.ResponseDto;
import com.kazan.model.KazanUser;
import com.kazan.service.KazanUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/kazan/api")
public class KazanUserApiResource {

    @Autowired
    private KazanUserService kazanUserService;

    @RequestMapping(method= RequestMethod.POST, path="/user")
    public ResponseEntity<ResponseDto<KazanUserDto>> createUser(
            @RequestBody KazanUserDto kazanUserDto) {
        ResponseDto<KazanUserDto> response = new ResponseDto<>();

        response.setContent(kazanUserService.createUser(kazanUserDto));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(method= RequestMethod.PUT, path="/user")
    public ResponseEntity<ResponseDto<KazanUserDto>> updateUser(
            @RequestBody KazanUserDto kazanUserDto) {
        ResponseDto<KazanUserDto> response = new ResponseDto<>();

        if (kazanUserService.findById(kazanUserDto.getUserId()) == null) {
            response.setErrorMessage("User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.setContent(kazanUserService.update(kazanUserDto));

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @RequestMapping(method= RequestMethod.GET, path="/user/{userId}")
    public ResponseEntity<ResponseDto<KazanUserDto>> getByMemberId(@PathVariable("userId") int userId) {
        ResponseDto<KazanUserDto> response = new ResponseDto<>();

        response.setContent(kazanUserService.findById(userId));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
