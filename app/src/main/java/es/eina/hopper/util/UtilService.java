package es.eina.hopper.util;

import java.util.List;

import es.eina.hopper.models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by jorge on 3/31/17.
 */

public interface UtilService {
    @GET("user")
    Call<String> getUser(@Header("Authorization") String name);
}
