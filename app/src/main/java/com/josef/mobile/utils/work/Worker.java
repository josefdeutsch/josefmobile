package com.josef.mobile.utils.work;

public interface Worker {

     String WORKREQUEST_KEYTASK_ERROR = "onError";
     String WORKREQUEST_KEYTASK_SUCCESS = "onSuccess";
     String WORKREQUEST_INDICATOR = "id";

     int CONNECT_TIMEOUT = 15;
     int READ_TIMEOUT = 15;
     int WRITE_TIMEOUT = 15;
     int MAX_VALID_CASES = 301;
}
