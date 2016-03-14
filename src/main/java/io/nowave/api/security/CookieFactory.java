package io.nowave.api.security;

import javax.servlet.http.Cookie;

/**
 * Created by olivier on 10/03/16.
 */
public interface CookieFactory {
    public Cookie create(String value);
}
