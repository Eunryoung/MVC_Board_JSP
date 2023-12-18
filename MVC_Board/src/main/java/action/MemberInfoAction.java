package action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import svc.MemberInfoService;
import vo.ActionForward;
import vo.MemberBean;

public class MemberInfoAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("MemberInfoAction");
		
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
			// MemberInfoService - getMember() 메서드 호출하여 회원 상세정보 조회 요청
			// => 파라미터 : 아이디   리턴타입 : MemberBean(member)
			//    (member 테이블의 1개 레코드 정보를 관리하는 타입 : MemberBean)
			MemberInfoService service = new MemberInfoService();
			MemberBean member = service.getMember(id);
//			System.out.println(member); // 확인용 출력문
			
			// ------------------------------------------------------------------------
			// 주민번호(xxxxxx-yyyyyyy) 중 뒷자리 첫번째 숫자를 제외한 나머지 * 처리
			// ex) 991111-1234567 -> 991111-1******
			// => String 클래스 중 substring() 메서드 활용
			// => substring() 메서드를 주민번호 뒷자리 첫번째 숫자(7번 인덱스)까지 추출
//			System.out.println(member.getJumin().substring(0, 8)); // 0 ~ 8-1 까지 추출
			// 추출된 문자열 뒤의 6자리를 * 기호로 문자열 결합
//			System.out.println(member.getJumin().substring(0, 8) + "******");
			
			// 가공 처리된 주민번호를 MemberBean 객체에 저장
			member.setJumin(member.getJumin().substring(0, 8) + "******");
			// ------------------------------------------------------------------------
			
			// 회원 상세정보 조회 결과를 뷰페이지로 전달하기 위해 
			// MemberBean 객체를 request 객체에 저장(속성명 : member)
			request.setAttribute("member", member);
			
			// "member/member_info.jsp" 페이지 포워딩 처리
			// => request 객체 및 서블릿 주소 유지를 위해 디스패치 방식 포워딩 설정
			forward = new ActionForward();
			forward.setPath("member/member_info.jsp");
			forward.setRedirect(false);
		}
		
		return forward;
	}

}












