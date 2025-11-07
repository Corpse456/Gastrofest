package by.gastrofest.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AbstractException extends RuntimeException {

    int status;

    AbstractException(final String message, final int status) {
        super(message);
        this.status = status;
    }

    AbstractException(final Exception e, final int status) {
        super(e);
        this.status = status;
    }
}
