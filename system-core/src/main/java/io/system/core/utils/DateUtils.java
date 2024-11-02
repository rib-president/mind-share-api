package io.system.core.utils;

import java.sql.Timestamp;

public class DateUtils {
  public static Timestamp getFirstDateTime(String date) {
    Timestamp result = null;
    if(date != null) {
      result = Timestamp.valueOf(date + " 00:00:00");
    }
    return result;
  }

  public static Timestamp getLastDateTime(String date) {
    Timestamp result = null;
    if(date != null) {
      result = Timestamp.valueOf(date + " 23:59:59");
    }
    return result;
  }
}
