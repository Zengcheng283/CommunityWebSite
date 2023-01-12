package life.zengc.community.community.common;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Random;

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

    /**
     * 实现ID通过随机字符串自动生成
     * @return
     */
    public String randomId() {
        String key = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int keyLength = key.length();
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            stringBuilder.append(key.charAt(random.nextInt(keyLength)));
        }
        log.info(stringBuilder.toString());
        return stringBuilder.toString();
    }
}
