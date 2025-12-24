package com.preshow.auth.client;

import com.preshow.auth.dto.CreateUserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @PostMapping("/users/internal")
    void createUser(@RequestBody CreateUserRequest request);

}
