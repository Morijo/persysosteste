package br.com.principal.helper;

import java.util.*;

public class TimeZoneHelper {
  private static final String TIMEZONE_ID_PREFIXES =
      "^(America)/.*";
  private static List<String> timeZones;
  public static List<String> getTimeZones() {
    if (timeZones == null) {
      timeZones = new ArrayList<String>();
      final String[] timeZoneIds = TimeZone.getAvailableIDs();
      for (final String id : timeZoneIds) {
        if (id.matches(TIMEZONE_ID_PREFIXES)) {
          timeZones.add(id);
        }
      }
      Collections.sort(timeZones, new Comparator<String>() {
        public int compare(final String t1, final String t2) {
          return t1.compareTo(t2);
        }
      });
    }
    return timeZones;
  }
  
  public static String getName(TimeZone timeZone) {
    return timeZone.getID().replaceAll("_", " ") + " - " + timeZone.getDisplayName();
    
  }
  }