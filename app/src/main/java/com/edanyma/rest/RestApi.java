package com.edanyma.rest;

import com.edanyma.model.ApiResponse;
import com.edanyma.model.AuthToken;
import com.edanyma.model.BootstrapModel;
import com.edanyma.model.CompanyInfoModel;
import com.edanyma.model.FavoriteCompanyModel;
import com.edanyma.model.LoginUser;
import com.edanyma.model.OurClientModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApi {

    final String API_COMPANY = "api/company/";

    final String API_CLIENT= "api/client/";

    @POST("token/generate-token")
    Call< ApiResponse< AuthToken > > register( @Body LoginUser loginUser);


    @GET(API_COMPANY+"bootstrap/{latitude}/{longitude}")
    Call< BootstrapModel > fetchBootstrapData( @Header("Authorization") String authorization,
                                               @Path("latitude") String latitude,
                                               @Path("longitude") String longitude  );

    @GET(API_COMPANY+"{id}")
    Call< CompanyInfoModel > getCompanyDishes( @Header("Authorization") String authorization,
                                                 @Path("id") String id );

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


}
