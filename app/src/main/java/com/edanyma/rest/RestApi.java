package com.edanyma.rest;

import com.edanyma.model.ApiResponse;
import com.edanyma.model.AuthToken;
import com.edanyma.model.BootstrapModel;
import com.edanyma.model.ClientLocationModel;
import com.edanyma.model.ClientOrderModel;
import com.edanyma.model.ClientOrderResponse;
import com.edanyma.model.CompanyInfoModel;
import com.edanyma.model.Dishes;
import com.edanyma.model.ExistOrders;
import com.edanyma.model.FavoriteCompanyModel;
import com.edanyma.model.FeedbackModel;
import com.edanyma.model.LoginUser;
import com.edanyma.model.OurClientModel;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApi {

     String API_COMPANY = "api/company/";

     String API_CLIENT= "api/client/";

     String API_CLIENT_ORDER = "api/client/order/";

     String API_FEEDBACK = "api/feedback/";

     String API_GEOCODE = "https://geocode-maps.yandex.ru/1.x/";
     String API_KEY = "32b33c62-4d72-47a7-92a3-25aedf11e968";
     String FORMAT_JSON = "json";

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

    @GET(API_CLIENT+"sendSmsCode/{phone}")
    Call< ApiResponse > sendSmsCode( @Header("Authorization") String authorization,
                                           @Path("phone") String phone );



    @POST(API_CLIENT_ORDER+"createClientOrder")
    Call< ApiResponse< ClientOrderResponse > > createClientOrder( @Header("Authorization") String authorization,
                                                                  @Body ClientOrderModel clientOrderModel  );

    @POST(API_CLIENT+"updateClientInfo")
    Call< ApiResponse > updateClientInfo( @Header("Authorization") String authorization,
                                                      @Body OurClientModel ourClientModel  );

    @POST(API_CLIENT+"updateClientAddress")
    Call< ApiResponse > updateClientAddress( @Header("Authorization") String authorization,
                                          @Body ClientLocationModel clientLocationModel );

    @POST(API_CLIENT+"updateClientPayType")
    Call< ApiResponse > updateClientPayType( @Header("Authorization") String authorization,
                                             @Body OurClientModel ourClientModel  );

    @POST(API_CLIENT+"updateClientPassword")
    Call< ApiResponse > updateClientPassword( @Header("Authorization") String authorization,
                                          @Body OurClientModel ourClientModel  );

    @GET(API_CLIENT+"removeClient/{uuid}")
    Call< ApiResponse > removeClient( @Header("Authorization") String authorization,
                                     @Path("uuid") String uuid );


    @Multipart
    @POST(API_CLIENT+"updateClientAvatar")
    Call< ApiResponse > updateClientAvatar( @Header("Authorization") String authorization,
                                      @Part MultipartBody.Part file, @Part("description") String description);

    @GET(API_CLIENT_ORDER+"getClientOrders/{uuid}")
    Call< ApiResponse< ExistOrders > > getClientOrders( @Header("Authorization") String authorization,
                                                        @Path("uuid") String uuid );

    @POST(API_FEEDBACK+"saveFeedback")
    Call< ApiResponse > saveFeedback( @Header("Authorization") String authorization,
                                           @Body FeedbackModel feedbackModel  );

    @GET(API_FEEDBACK+"getCompanyFeedbacks/{companyId}")
    Call<ApiResponse< List<FeedbackModel> > > getCompanyFeedbacks( @Header("Authorization") String authorization,
                                                            @Path("companyId") Integer companyId );


    @GET(API_CLIENT+"sendEmailToUs/{message}")
    Call< ApiResponse> sendMessageToSupport( @Header("Authorization") String authorization,
                                                        @Path("message") String message );

}
