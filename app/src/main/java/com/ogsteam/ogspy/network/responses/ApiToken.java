package com.ogsteam.ogspy.network.responses;

import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;

/**
 * Created by jpspa on 17/10/2016.
 */
public class ApiToken implements OgspyResponse {
    private String status;
    private String api_token;
    private LocalDateTime date;

    public ApiToken() {
        this.date = new LocalDateTime();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public boolean isValid() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Minutes minutes = Minutes.minutesBetween(date, localDateTime);
        return minutes.isLessThan(Minutes.minutes(61));
    }
}
