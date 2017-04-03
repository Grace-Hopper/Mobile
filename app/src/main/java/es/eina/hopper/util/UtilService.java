package es.eina.hopper.util;

import java.io.Serializable;
import java.util.List;

import es.eina.hopper.models.Recipe;
import es.eina.hopper.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface UtilService {
    @GET("user")
    Call<User> getUser(@Header("Authorization") String name);

    @POST("user/login")
    Call<User> login(@Body User user);

    @POST("user/signup")
    Call<User> sigin(@Body User user);

    @GET("recipes")
    Call<List<Recipe>> getAllRecipes(@Header("Authorization") String name);

    @GET("recipe")
    Call<Recipe> getRecipe(@Header("Authorization") String name, @Query("id") long id);
   }