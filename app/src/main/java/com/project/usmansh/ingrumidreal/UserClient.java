package com.project.usmansh.ingrumidreal;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserClient {

    @POST("register/")
    Call<User> createAccount(@Body User user);

}
