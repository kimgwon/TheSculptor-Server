package backend.sculptor.global.oauth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        System.out.println("Session ID = " + request.getSession().getId());
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        // 세션 ID 가져오기
        String sessionId = request.getSession().getId();

        // JSON 데이터 구성
        String jsonResponse = String.format(
                "{\"code\": 200, \"message\": \"로그인 성공\", \"data\": {\"sessionId\": \"%s\"}}",
                sessionId
        );

        // JSON 응답 전송
        System.out.println("============");
        PrintWriter writer = response.getWriter();
        writer.write(jsonResponse);
        System.out.println("============");

        // 스트림 닫기 및 응답 완료
        writer.flush();
        writer.close();
        //response.sendRedirect("/");
    }
}
