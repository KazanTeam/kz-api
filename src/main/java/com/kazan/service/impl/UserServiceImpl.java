package com.kazan.service.impl;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.RespondToAuthChallengeResult;
import com.kazan.cognito.AuthenticationHelper;
import com.kazan.cognito.CognitoJWTParser;
import com.kazan.dto.UserDto;
import com.kazan.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService{

    @Value("${aws.cognito.user.pools.web.client.id}")
    private String clientId;

    @Value("${aws.cognito.user.pools.id}")
    private String poolId;

    @Autowired
    private AWSCognitoIdentityProvider cognitoClient;

    @Override
    public UserDto login(UserDto userDto) {
        AuthenticationHelper helper = new AuthenticationHelper(poolId, clientId, cognitoClient);
        RespondToAuthChallengeResult result = helper.gerformSRPAuthentication(userDto.getUsername(), userDto.getPassword());
        JSONObject jsonObject = CognitoJWTParser.getPayload(result.getAuthenticationResult().getIdToken());
        userDto.setToken(result.getAuthenticationResult().getIdToken());
        userDto.setRefreshToken(result.getAuthenticationResult().getRefreshToken());
        userDto.setEmail(jsonObject.getString("email"));
        userDto.setTokenExpire(result.getAuthenticationResult().getExpiresIn());

        return userDto;
    }

}
