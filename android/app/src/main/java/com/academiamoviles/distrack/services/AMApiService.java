package com.academiamoviles.distrack.services;

import com.academiamoviles.distrack.models.RegEstado;
import com.academiamoviles.distrack.models.ResponseEntregaEspecial;
import com.academiamoviles.distrack.models.ResponseEstadoCobranzaList;
import com.academiamoviles.distrack.models.ResponseEstadoList;
import com.academiamoviles.distrack.models.ResponseFirma;
import com.academiamoviles.distrack.models.ResponseLogin;
import com.academiamoviles.distrack.models.ResponsePedidosPendientes;
import com.academiamoviles.distrack.models.ResponseQR;
import com.academiamoviles.distrack.models.ResponseRegEstado;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface AMApiService {
    @FormUrlEncoded
    @POST("ws/user/login")
    Call<ResponseLogin> loginUser(@Field("usuario") String usuario,
                                  @Field("password") String password,
                                  @Field("latitud") String latitud,
                                  @Field("longitud") String longitud);

    @FormUrlEncoded
    @POST("ws/entregaEspecial/regEntrega")
    Call<ResponseEntregaEspecial> entregaUser(@Field("pedido") String pedido,
                                              @Field("nombrePedido") String nombrePedido,
                                              @Field("usuario") String usuario,
                                              @Field("Fecha") String Fecha,
                                              @Field("latitud") String latitud,
                                              @Field("longitud") String longitud,
                                              @Field("idProducto") String idProducto,
                                              @Field("descProducto") String descProducto,
                                              @Field("codigoPromotor") String codigoPromotor);

    @FormUrlEncoded
    @POST("ws/user/logout")
    Call<ResponseFirma> logoutUser(@Field("idusuario") Integer idusuario,
                                   @Field("usuario") String usuario,
                                   @Field("latitud") double latitud,
                                   @Field("longitud") double longitud);

    @GET("ws/entregaEspecial/consultaEstado")
    Call<ResponseEstadoList> consultaEstado();


    @GET("ws/estado/getList")
    Call<ResponseEstadoList> getEstadoList();

    @GET("ws/cobranza/estado/getList")
    Call<ResponseEstadoCobranzaList> getEstadoCobranzaList();

    @GET("ws/pedido/getList")
    Call<ResponsePedidosPendientes> getPedidos(@Query("usuario") String usuario);

    @GET("/ws/pedido/buscar-por-documento")
    Call<ResponseQR> getQR(@Query("documento") String documento);

    @Headers("Content-Type: application/json")
    @POST("ws/regestado/update")
    Call<List<ResponseRegEstado>> sendRegEstado(@Body HashMap<String, List<RegEstado>> body);

    @Multipart
    @POST("ws/upload/foto")
    Call<ResponseBody> sendFotoPedido(
            @Part MultipartBody.Part fileImage,
            @Part("idusuario") RequestBody idusuario,
            @Part("usuario") RequestBody usuario,
            @Part("idpedido") RequestBody idpedido,
            @Part("documento") RequestBody documento,
            @Part("estado") RequestBody estado,
            @Part("fecha") RequestBody fecha,
            @Part("hora") RequestBody hora,
            @Part("latitud") RequestBody latitud,
            @Part("longitud") RequestBody longitud
    );

    @Multipart
    @POST("ws/upload/firma")
    Call<ResponseFirma> sendFirmaPedido(
            @Part MultipartBody.Part fileImage,
            @Part("idusuario") RequestBody idusuario,
            @Part("indice") RequestBody indice,
            @Part("documento") RequestBody documento,
            @Part("usuario") RequestBody usuario,
            @Part("estado") RequestBody estado,
            @Part("fecha") RequestBody fecha,
            @Part("hora") RequestBody hora,
            @Part("latitud") RequestBody latitud,
            @Part("longitud") RequestBody longitud
    );

}
