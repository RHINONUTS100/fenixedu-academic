/*
 * InterceptingServiceException.java
 *
 * March 2nd, 2003, Sometime in the afternoon
 */
 
package ServidorAplicacao.Servico.exceptions;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
import ServidorAplicacao.FenixServiceException;

public class InterceptingRoomsServiceException extends FenixServiceException {

    public InterceptingRoomsServiceException() {
    }
    
	public InterceptingRoomsServiceException(Throwable cause) {
		super(cause);
	}

	public InterceptingRoomsServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}