package com.microservice.apttrade.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

public class DataException {

    @SuppressWarnings("serial")
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public static class DefaultException extends RuntimeException {
        String message;

        public DefaultException(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }

    public static class AddressDataException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    public static class AddressDataNotFound extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    public static class AptTransactionDataException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    public static class AptTransactionDataNullException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
}
