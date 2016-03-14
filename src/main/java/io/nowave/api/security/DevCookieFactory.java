package io.nowave.api.security;

import javax.servlet.http.Cookie;

/**
 * Created by olivier on 10/03/16.
 */
public class DevCookieFactory implements CookieFactory {
    @Override
    public Cookie create(String value) {
        final Cookie cookie = new Cookie("jwt", value);
        cookie.setPath("/");
        //7 days
        cookie.setMaxAge(60*60*24*7);
        return cookie;
    }
}
