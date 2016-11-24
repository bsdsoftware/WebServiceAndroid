package it.bsdsoftware.webservice.library;

/**
 * Created by Simone on 30/06/16.
 */
public interface WebServiceTaskListener<T> {
    void onWebServiceTaskCompletato(WebServiceTaskResult<T> result);
}
