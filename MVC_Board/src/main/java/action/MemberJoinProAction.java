package action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.MemberJoinProService;
import vo.ActionForward;
import vo.MemberBean;

// FrontController 클래스와 연동하여 컨트롤러 역할을 수행할 Action 클래스 정의
// => 공통된 메서드 execute() 를 직접 정의하지 않고
//    Action 인터페이스를 상속받아 추상메서드 execute() 를 오버라이딩(구현)
public class MemberJoinProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("MemberJoinProAction");
		
		// 포워딩 정보를 저장할 ActionForward 타입 변수(forward) 선언
		ActionForward forward = null;
		
		// 회원가입 폼에서 입력받은 폼 파라미터 가져오기
		// => 데이터 전송 객체인 MemberBean 객체(member) 생성 후 폼 파라미터 데이터 저장하기
		// => 이메일(@), 주민번호(-), 주소(/)를 각각 하나의 멤버변수에 각각 문자열 결합 저장
		//    ex) email1("admin"), email2("naver.com")일 때 "admin@naver.com" 으로 결합
		// => 취미(hobby)는 복수개의 파라미터가 하나의 이름으로 전달되므로
		//    배열로 리턴받아 반복문을 통해 하나의 문자열로 결합(구분자 : /)
//		String name = request.getParameter("name");
		MemberBean member = new MemberBean();
//		member.setName(name);
		member.setName(request.getParameter("name"));
		member.setId(request.getParameter("id"));
		member.setPasswd(request.getParameter("passwd"));
//		member.setJumin(request.getParameter("jumin"));
//		member.setAddress(request.getParameter("address"));
//		member.setEmail(request.getParameter("email"));
//		member.setJob(request.getParameter("job"));		
		// 복수개의 파라미터가 존재하는 데이터는 문자열 결합 필요
		member.setJumin(request.getParameter("jumin1") + "-" + request.getParameter("jumin2"));
		member.setAddress(request.getParameter("postCode") + "/" + request.getParameter("address1") + "/" + request.getParameter("address2"));
		member.setEmail(request.getParameter("email1") + "@" + request.getParameter("email2"));

		// 단, 취미(hobby)는 동일한 이름의 파라미터가 복수개 존재하므로 배열 관리 필요
		// => request.getParameter() 대신 request.getParameterValues() 메서드 사용 필요
		// 단, 반복문 수행 전 MemberBean 객체의 hobby 멤버변수값을 널스트링("")으로 초기화
		member.setHobby("");
		
		// 배열을 저장할 별도의 변수 없이도 for문에 직접 파라미터 리턴값 전달하여 사용 가능
		// => 단, 체크박스가 하나도 체크되지 않으면 null 값이 리턴되므로
		//    null 값이 리턴되지 않는다는 보장 하에서만 사용(NullPointerException 발생 가능성 있음)
//		if(request.getParameterValues("hobby") != null) {
//			for(String hobby : request.getParameterValues("hobby")) {
//				// 취미를 차례대로 반복하면서 "/" 기호를 구분자로 사용하여 문자열 결합
//				// => 기존 데이터와 결합하므로 member.getHobby() 메서드 활용
//				member.setHobby(member.getHobby() + hobby + "/");
//			}
//		}
		
		String[] hobbies = request.getParameterValues("hobby");
		// NullPointerException 방지를 위해 null 값이 아닐 경우에만 반복문 처리
		if(hobbies != null) {
			for(String hobby : hobbies) {
				// 취미를 차례대로 반복하면서 "/" 기호를 구분자로 사용하여 문자열 결합
				// => 기존 데이터와 결합하므로 member.getHobby() 메서드 활용
				member.setHobby(member.getHobby() + hobby + "/");
			}
		}
		
		member.setJob(request.getParameter("job"));
		member.setGender(request.getParameter("gender"));
		member.setMotivation(request.getParameter("motivation"));
		
		System.out.println(member); // toString() 생략
		
		// ------------------------------------------------------------------
		// 회원 가입 비즈니스 로직(DB 작업) 요청 수행(XXXService 클래스 활용)
		// MemberJoinProService 클래스의 인스턴스 생성 후 registMember() 메서드 호출
		// => 파라미터 : MemberBean 객체   리턴타입 : boolean(isJoinSuccess)
		MemberJoinProService service = new MemberJoinProService();
		boolean isJoinSuccess = service.registMember(member);
		
		// 회원 가입 요청 결과 판별
		if(!isJoinSuccess) { // 실패
			try {
				// 자바스크립트 활용하여 "회원 가입 실패!" 출력 후 이전 페이지로 돌아가기
				// => 해당 자바스크립트 코드를 응답 데이터로 생성하기
				// => 응답 데이터 생성을 위한 reponse 객체를 활용하여 출력 데이터 저장
				// 1) 출력할 데이터의 문서 타입(contentType) 설정
				response.setContentType("text/html; charset=UTF=8");
				// 2) 출력 스트림 객체 가져오기
				PrintWriter out = response.getWriter(); // 1, 2, 3번 블록 설정 후 try/catch 처리 필요!
				// 3) PrintWriter 객체의 println() 메서드 호출하여 출력할 데이터 설정
				out.println("<script>");
				out.println("alert('회원 가입 실패!');");
				out.println("history.back();");
				out.println("</script>");
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 실패 시 자바스크립트 출력하려면 ActionForward 객체는 불필요하므로 생성 X
			// => null 값이 리턴됨

		} else { // 성공
			// ActionForward 클래스를 활용하여 포워딩 정보 저장
			// 1) 포워딩 정보를 관리할 ActionForward 객체 생성
			forward = new ActionForward();
			// 2) ActionForward 객체에 포워딩 경로(메인페이지 = "./") 저장 => setPath()
			forward.setPath("./");
			// 3) ActionForward 객체에 포워딩 방식 저장 => setRedirect()
			//    => URL 변경을 위해 리다이렉트 방식 지정(true)
			forward.setRedirect(true);
		}
		
		// 포워딩 정보가 저장된 ActionForward 객체 리턴
		return forward; // MemberFrontController 로 리턴
	}
	
}
