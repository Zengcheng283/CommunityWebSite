package life.zengc.community.community.common;

import life.zengc.community.community.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@Component
public class CommonMethods
{
    /**
     * 根据参数request和需要的tokenName获取相应的cookie
     * @param request
     * @param requestTokenName
     * @return
     */
    public Cookie getTokenCookie(HttpServletRequest request, String requestTokenName) {
        Cookie[] cookies = request.getCookies();
        log.info(Arrays.toString(cookies));
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie: cookies) {
            if (cookie.getName().equals(requestTokenName)) {
                return cookie;
            }
        }
        return null;
    }
}
