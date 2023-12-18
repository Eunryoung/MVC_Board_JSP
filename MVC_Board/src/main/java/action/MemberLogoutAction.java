package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vo.ActionForward;

public class MemberLogoutAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("MemberLogoutAction");
		
		ActionForward forward = null;
		
		// HttpSession 객체를 리턴받아 세션 초기화 후 메인페이지로 리다이렉트
		HttpSession session = request.getSession();
		session.invalidate();
		
		forward = new ActionForward();
		forward.setPath("./");
		forward.setRedirect(true);
		
		return forward;
	}

}
