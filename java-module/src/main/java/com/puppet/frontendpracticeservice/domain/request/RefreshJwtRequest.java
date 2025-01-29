package com.puppet.frontendpracticeservice.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshJwtRequest {

    public String refreshToken;

}
