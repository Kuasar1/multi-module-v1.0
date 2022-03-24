package mn.easybuy.authservice.controller;//package mn.ezbuy.authservice.controller;

import lombok.RequiredArgsConstructor;
import mn.easybuy.authservice.service.RefreshTokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController {

	private final RefreshTokenService refreshTokenService;

	@GetMapping("/refresh")
	public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
		refreshTokenService.getRefreshToken(request,response);
	}

}
