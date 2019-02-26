package com.edanyma.rest;

import com.edanyma.model.ApiResponse;
import com.edanyma.model.AuthToken;
import com.edanyma.model.LoginUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EdaNymaApi {

    @POST("/token/generate-token")
    Call< ApiResponse< AuthToken > > register( @Body LoginUser loginUser);
}
