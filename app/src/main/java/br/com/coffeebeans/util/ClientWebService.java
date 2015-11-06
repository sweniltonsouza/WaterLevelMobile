package br.com.coffeebeans.util;

import android.os.AsyncTask;
import android.util.Log;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.spi.service.ServiceFinder;
import javax.ws.rs.core.MediaType;

import br.com.coffeebeans.exception.ClientWebServiceException;

/**
 * Created by AndréFillipe on 06/11/2015.
 */
public class ClientWebService {
    private WebResource webResource;
    private ClientResponse response;
    //public static String output;
    String output;

    //metodo para teste com as classes do prof wesley
    public void exemploGetUsers() throws ClientWebServiceException {
        try {
            ServiceFinder.setIteratorProvider(new AndroidServiceIteratorProvider());
            Client client = Client.create();
            webResource = client.resource("http://10.0.2.2:8080/ExemploWebService/user/all");
            ThreadResponse threadResponse = new ThreadResponse();
            threadResponse.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } catch (Exception e) {
            throw new ClientWebServiceException(e);
        }
    }

    public class ThreadResponse extends AsyncTask<Void, Void, String> {
        //TODO //como tratar exceções nesse método?
        @Override
        protected String doInBackground(Void... params) {
            try {

                response = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
                output = response.getEntity(String.class);

            } catch (Exception e) {
                Log.i("", "getMessage :" + e.getMessage());
                Log.i("", "getCause" + String.valueOf(e.getCause()));
                Log.i("", "getStrackTrace " + String.valueOf(e.getStackTrace()));
                e.printStackTrace();
            }
            return "sucesso na execucao da thread";
        }

        @Override
        protected void onPostExecute(String message) {

            Log.i("", message);
            Log.i("", "Resposta do servidor: " + output);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }
        }
    }

}
