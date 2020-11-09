package cl.paulina.yotrabajoconpecs.ServicioAPI;

import java.util.List;

import cl.paulina.yotrabajoconpecs.modelo.Pictograma;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ArasaacAPI {
    @GET("pictograms/{locale}/search/{searchText}")
    Call<List<Pictograma>> obtenerListaArasaac(
            @Path("locale") String locale,
            @Path("searchText") String searchText);

    @GET("pictograms/{locale}/search/{searchId}")
    Call<List<Pictograma>> obtenerListaId(
            @Path("locale") String locale,
            @Path("searchId") int searchId);
}
