package com.microservice.apt.util;

public class DataException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public  static class DefaultException extends  RuntimeException{
        private static final long serialVersionUID = 1L;
    }
    public static class AptDataNullException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    public static class AddressDataException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

}
