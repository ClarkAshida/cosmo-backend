package com.cosmo.cosmo.dto;

import java.util.Date;

public record TokenDTO(String email, Boolean authenticated, Date created, Date expiration, String accessToken, String refreshToken) {}
