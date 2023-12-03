package by.gastrofest.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ConnectionException extends AbstractException {

    public ConnectionException(final String message, final int status) {
        super(message, status);
    }

    public ConnectionException(final Exception e) {
        super(e, 409);
    }
}
