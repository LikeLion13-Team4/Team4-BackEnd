package com.project.team4backend.domain.auth.service;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplateBuilder {
    /**
     * 임시 비밀번호 발급 이메일 HTML 템플릿
     */
    public String buildTempPasswordHtml(String tempPassword) {
        return """
            <html>
              <body style="font-family: Arial, sans-serif;">
                <h2>임시 비밀번호 발급 안내</h2>
                <p>임시 비밀번호는 아래와 같습니다. 로그인 후 반드시 비밀번호를 변경해주세요.</p>
                <div style="font-size: 20px; font-weight: bold; color: #dc3545; margin-top: 10px;">
                  %s
                </div>
                <p style="margin-top: 10px;">보안을 위해 임시 비밀번호는 1회성입니다.</p>
              </body>
            </html>
        """.formatted(tempPassword);
    }

    public String buildVerifyEmailHtml(String verifyCode) {
        return """
            <html>
              <body style="font-family: Arial, sans-serif;">
                <h2>이메일 인증용 코드 발급 안내</h2>
                <p>이메일 인증 확인을 위한 코드입니다.</p>
                <div style="font-size: 20px; font-weight: bold; color: #dc3545; margin-top: 10px;">
                  %s
                </div>
                <p style="margin-top: 10px;">보안을 위해 코드는 1회성입니다.</p>
              </body>
            </html>
        """.formatted(verifyCode);
    }
}
