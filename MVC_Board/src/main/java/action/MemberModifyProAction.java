package action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import svc.MemberModifyProService;
import vo.ActionForward;
import vo.MemberBean;

public class MemberModifyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("MemberModifyProAction");
		
		ActionForward forward = null;
		

		// 세션에 저장된 세션 아이디(속성명 : sId") 가져와서 변수에 저장
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("sId");
		
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
			
			// 세션 아이디 없음 처리 정보 설정 후 메서드 수행 종료 후 복귀하려면 null 값 리턴
			return null; // => null 리턴 시 else 문 필요없음
		}
		
		// 세션 아이디가 있을 경우
		// 회원가입 폼에서 입력받은 폼 파라미터 가져오기
		MemberBean member = new MemberBean();
		member.setName(request.getParameter("name"));
		
		// 파라미터로 전달받은 아이디가 아닌 세션 아이디를 MemberBean 객체에 저장
//		member.setId(request.getParameter("id"));
		member.setId(id);
		
		member.setPasswd(request.getParameter("passwd"));
		// 복수개의 파라미터가 존재하는 데이터는 문자열 결합 필요
		member.setAddress(request.getParameter("postCode") + "/" + request.getParameter("address1") + "/" + request.getParameter("address2"));
		member.setEmail(request.getParameter("email1") + "@" + request.getParameter("email2"));
		
		member.setHobby("");
		String[] hobbies = request.getParameterValues("hobby");
		if(hobbies != null) {
			for(String hobby : hobbies) {
				member.setHobby(member.getHobby() + hobby + "/");
			}
		}
		
		member.setJob(request.getParameter("job"));
		member.setGender(request.getParameter("gender"));
		member.setMotivation(request.getParameter("motivation"));
		
//		System.out.println(member); // toString() 생략
		
		// MemberModifyProService - modifyMember() 메서드 호출하여 회원 정보 수정 요청
		// => 파라미터 : MemberBean 객체   리턴타입 : boolean(isModifySuccess)
		MemberModifyProService service = new MemberModifyProService();
		boolean isModifySuccess = service.modifyMember(member);
		
		// 회원 정보 수정 요청 처리 결과 판별
		// => 실패 시 자바스크립트 사용 "회원 정보 수정 실패!" 출력 후 이전페이지 돌아가기
		// => 성공 시 MemberInfo.me 서블릿 요청
		if(!isModifySuccess) {
			try {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('회원 정보 수정 실패!');");
				out.println("history.back();");
				out.println("</script>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { // 로그인 성공 시
			// MemberInfo.me 서블릿 요청(리다이렉트)
			forward = new ActionForward();
			forward.setPath("MemberInfo.me");
			forward.setRedirect(true);
		}
		
		return forward;
	}

}














