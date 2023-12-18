package action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import svc.MemberInfoService;
import vo.ActionForward;
import vo.MemberBean;

public class MemberModifyFormAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("MemberModifyFormAction");
		ActionForward forward = null;
		
		// 세션에 저장된 세션 아이디(속성명 : sId") 가져와서 변수에 저장
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("sId");
//		System.out.println(id); // 아이디 출력 또는 null 값 출력
		
		// 세션 아이디가 존재하지 않을 경우(null)
		// 자바스크립트 사용하여 "잘못된 접근입니다!" 출력 후 메인페이지로 이동
		if(id == null) {
			try {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('잘못된 접근입니다!');");
				out.println("location.href = './';");
				out.println("</script>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { // 세션 아이디가 존재할 경우
			// MemberInfoService - getMember() 메서드 호출하여 회원 상세정보 조회 요청(재사용)
			// => 파라미터 : 아이디   리턴타입 : MemberBean(member)
			MemberInfoService service = new MemberInfoService();
			MemberBean member = service.getMember(id);
//			System.out.println(member); // 확인용 출력문
			
			// 회원 상세정보 조회 결과를 뷰페이지로 전달하기 위해 
			// MemberBean 객체를 request 객체에 저장(속성명 : member)
			request.setAttribute("member", member);
			
			// "member/member_modify_form.jsp" 페이지 포워딩 처리
			// => request 객체 및 서블릿 주소 유지를 위해 디스패치 방식 포워딩 설정
			forward = new ActionForward();
			forward.setPath("member/member_modify_form.jsp");
			forward.setRedirect(false);
		}
		
		return forward;
	}

}
