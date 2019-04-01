package com.abrutsze.tableview.connections;

import com.abrutsze.tableview.BuildConfig;
import com.abrutsze.tableview.models.NewsResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.abrutsze.tableview.utils.LogsClass.getLogsClassInstance;

public class ServerConnections {

    public static final int ERROR_SERVER_TIMEOUT = 4000;
    public static final int ERROR_NO_ROUTE_TO_HOST_EXCEPTION = 4001;
    public static final int ERROR_UNKNOWN = 4099;
    private static ServerConnections serverConnections;
    private final String BASE_URL = "http://citymani.ezrdv.org/";
    private int currentPage = 0, numberOfPages = -1;
    private ServerApi serverApi;
    private ServerResponse serverResponse;

    private ServerConnections() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //create Retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit = builder.build();

        //Get client and call object for the request
        serverApi = retrofit.create(ServerApi.class);
    }

    public static ServerConnections getServerConnectionsInstance() {
        if (serverConnections == null) {
            serverConnections = new ServerConnections();
        }
        return serverConnections;
    }

    public void getNews() {

        if (currentPage == numberOfPages || numberOfPages == 0) return;
        currentPage++;

        Call<NewsResponse> call = serverApi.getNews(currentPage);

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {

                String errorBody = "";
                String rawResponse = "";

                if (!response.isSuccessful()) {
                    try {
                        errorBody = response.errorBody() != null ? response.errorBody().string() : null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                getLogsClassInstance().i("message= " + response.message());
                getLogsClassInstance().i("body= " + response.body());
                getLogsClassInstance().i("code= " + response.code());
                getLogsClassInstance().i("errorBody= " + errorBody);
                getLogsClassInstance().i("rawResponse= " + rawResponse);

                if (serverResponse != null) {
                    numberOfPages = response.body().getTotalPages();
                    serverResponse.serverResponseStatus(response.code(), response.body(), errorBody);
                }

            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                if (serverResponse == null) return;

                if (t.getMessage().contains("connect timed out") || t.getMessage().contains("timeout")) {
                    serverResponse.serverResponseStatus(ERROR_SERVER_TIMEOUT, null, null);
                } else if (t.getMessage().contains("no rout")) {
                    serverResponse.serverResponseStatus(ERROR_NO_ROUTE_TO_HOST_EXCEPTION, null, null);
                } else if (t.getMessage().contains("No value for fields")) {
                    serverResponse.serverResponseStatus(ERROR_UNKNOWN, null, null);
                }
            }
        });
    }

    public void registerForServerResponse(ServerResponse serverResponse) {
        this.serverResponse = serverResponse;
    }

    public interface ServerResponse {
        void serverResponseStatus(int responseCode, Object responseMessage, String errorBody);
    }

}
