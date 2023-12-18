package action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import svc.MemberLoginProService;
import vo.ActionForward;
import vo.MemberBean;

public class MemberLoginProAction implements Action {
	
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("MemberLoginProAction");
		
		// 포워딩 정보를 저장할 ActionForward 타입 변수(forward) 선언
		ActionForward forward = null;
		
		// 파라미터로 전달받은 아이디, 패스워드, 로그인 상태유지 체크박스 값 가져오기
				// => MemberBean 객체에 저장(체크박스값은 제외)
				MemberBean member = new MemberBean();
				member.setId(request.getParameter("id"));
				member.setPasswd(request.getParameter("passwd"));
//				System.out.println(member); // toString() 메서드 호출 생략되어 있음
				
				// MemberLoginProService - isCorrectUser() 메서드 호출하여 로그인 판별 요청
				// => 파라미터 : MemberBean 객체   리턴타입 : boolean(isCorrectUser)
				MemberLoginProService service = new MemberLoginProService();
				boolean isCorrectUser = service.isCorrectUser(member);
				
				// 로그인 성공 여부 판별
				if(!isCorrectUser) { // 로그인 실패(아이디 없음 또는 패스워드 틀림) 시
					try {
						// 자바스크립트를 사용하여 "로그인 실패!" 출력 및 이전페이지로 돌아가기
						response.setContentType("text/html; charset=UTF-8");
						PrintWriter out = response.getWriter();
						out.println("<script>");
						out.println("alert('로그인 실패!');");
						out.println("history.back();");
						out.println("</script>");
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else { // 로그인 성공 시
					// 세션 객체에 로그인 성공한 아이디를 "sId" 라는 속성명으로 저장
					// => 단, 서블릿 클래스에서는 세션 객체가 내장 객체로 제공되지 않음(JSP 만 가능)
					//    따라서, request 객체로부터 세션 객체를 얻어와야함 = getSession() 메서드 활용
					//    (리턴타입 : HttpSession)
					HttpSession session = request.getSession();
					session.setAttribute("sId", member.getId());
					
					// 메인페이지(./)로 리다이렉트
					// => ActionForward 객체 생성 후 경로 설정("./")과 리다이렉트 방식 설정
					forward = new ActionForward();
					forward.setPath("./");
					forward.setRedirect(true);
				}
				
				return forward; // MemberFrontController 로 리턴
			}

		}

















