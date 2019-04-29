package com.edanyma.rest;

import com.edanyma.model.ApiResponse;
import com.edanyma.model.AuthToken;
import com.edanyma.model.BootstrapModel;
import com.edanyma.model.CompanyInfoModel;
import com.edanyma.model.Dishes;
import com.edanyma.model.FavoriteCompanyModel;
import com.edanyma.model.LoginUser;
import com.edanyma.model.OurClientModel;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApi {

    final String API_COMPANY = "api/company/";

    final String API_CLIENT= "api/client/";

    final String API_GEOCODE = "https://geocode-maps.yandex.ru/1.x/";
    final String API_KEY = "32b33c62-4d72-47a7-92a3-25aedf11e968";
    final String FORMAT_JSON = "json";

    @POST("token/generate-token")
    Call< ApiResponse< AuthToken > > register( @Body LoginUser loginUser);


    @GET(API_COMPANY+"bootstrap/{latitude}/{longitude}")
    Call< BootstrapModel > fetchBootstrapData( @Header("Authorization") String authorization,
                                               @Path("latitude") String latitude,
                                               @Path("longitude") String longitude  );

    @GET(API_COMPANY+"{id}")
    Call< CompanyInfoModel > getCompanyDishes( @Header("Authorization") String authorization,
                                                 @Path("id") String id );

    @GET(API_COMPANY+"dishes/{deliveryCity}/{categoryId}")
    Call< ApiResponse<Dishes> > getDishes( @Header("Authorization") String authorization,
                                             @Path("deliveryCity") String deliveryCity,
                                             @Path("categoryId") int categoryId );

    @POST(API_CLIENT+"registerMobileClient")
    Call< ApiResponse<OurClientModel> > signUp( @Header("Authorization") String authorization,
                                               @Body OurClientModel ourClientModel  );

    @POST(API_CLIENT+"authorizationMobileClient")
    Call< ApiResponse<OurClientModel> > signIn( @Header("Authorization") String authorization,
                                               @Body OurClientModel ourClientModel  );

    @POST(API_CLIENT+"validateAndSendEmail")
    Call< ApiResponse<String> > validateAndSendEmail( @Header("Authorization") String authorization,
                                @Body OurClientModel ourClientModel  );

    @POST(API_CLIENT+"addToFavorite")
    Call< ApiResponse > addToFavorite( @Header("Authorization") String authorization,
                                                @Body FavoriteCompanyModel favoriteCompanyModel  );

    @POST(API_CLIENT+"removeFromFavorite")
    Call< ApiResponse > removeFromFavorite( @Header("Authorization") String authorization,
                                       @Body FavoriteCompanyModel favoriteCompanyModel  );

    @GET(API_GEOCODE)
    Call< Object > getLocationAddress( @Query("apikey") String apikey,
                                           @Query("format") String format,
                                           @Query("geocode") String geocode);

}
