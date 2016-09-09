package it.bsdsoftware.webservice.library;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by Simone on 09/09/16.
 */
public class WebServiceSyncTask {

    public WebServiceTaskResult getResult(Operation operation){
        HttpURLConnection urlConnection = null;
        WebServiceTaskResult result;
        try {
            urlConnection = operation.buildUrlConnection();
            urlConnection.connect();
            Integer statuCode = urlConnection.getResponseCode();
            if (operation.validStatusCodes().contains(statuCode)) {
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                result = operation.onSuccess(statuCode, inputStream);
            } else {
                Throwable t = null;
                if (statuCode == 404) {
                    t = new Throwable("");
                }
                InputStream errorStream = urlConnection.getErrorStream();
                result = operation.onFail(statuCode, t, errorStream);
            }
        } catch (Throwable t) {
            InputStream errorStream = null;
            if(urlConnection!=null)
                errorStream = urlConnection.getErrorStream();
            result = operation.onFail(-1, t, errorStream);
        } finally {
            if(urlConnection!=null)
                urlConnection.disconnect();
        }
        return result;
    }

}
