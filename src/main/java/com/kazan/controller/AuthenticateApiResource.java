package com.kazan.controller;

import com.kazan.dto.ResponseDto;
import com.kazan.dto.UserDto;
import com.kazan.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/kazan/api/authenticate")
public class AuthenticateApiResource {

    @Autowired
    UserService userService;

    @RequestMapping(method= RequestMethod.POST, path="/login")
    @ResponseBody
    @ApiOperation("Api login kanzan with aws cognito")
    public ResponseEntity<ResponseDto<UserDto>> login(@RequestBody UserDto userDto) {
        ResponseDto<UserDto> response = new ResponseDto<>();
        if (!userDto.validation()) {
            response.setErrorMessage("Username or Password wrong");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        response.setContent(userService.login(userDto));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
