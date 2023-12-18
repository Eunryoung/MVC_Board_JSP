package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import action.MemberInfoAction;
import action.MemberJoinProAction;
import action.MemberLoginProAction;
import action.MemberLogoutAction;
import action.MemberModifyFormAction;
import action.MemberModifyProAction;
import action.MemberWithdrawProAction;
import vo.ActionForward;

@WebServlet("*.me")
public class MemberFrontController extends HttpServlet {
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("MemberFrontController");

		// 공통으로 사용할 변수 선언
		ActionForward forward = null; // 포워딩 정보 관리
		Action action = null; // XXXAction 클래스의 인스턴스 관리(업캐스팅에 사용될 부모 타입)
		
		// 서블릿 주소 추출하기
		String command = request.getServletPath();
		System.out.println("command : " + command);
		
		// 추출된 서블릿 주소(command)를 판별하여 각 주소에 따른 액션(작업) 요청
		if(command.equals("/MemberJoinForm.me")) {
			System.out.println("회원 가입 폼!");
			
			// 회원가입 폼 출력하는 뷰페이지(member/member_join_form.jsp) 로 포워딩
			// => 비즈니스 로직(= DB 작업 등) 불필요하므로 뷰페이지로 바로 이동
			// => Dispatch 방식(주소표시줄 URL 변경 X = MemberJoinForm.me 서블릿 주소 유지됨)
			// => 주의! 현재 서블릿 주소가 http://localhost:8080/MVC_Board/MemberJoinForm.me 이며
			//    컨텍스트 루트(/MVC_Board) 가 webapp 디렉토리를 가리키므로
			//    하위 디렉토리(member) 까지 포함하여 포워딩 경로를 지정해야한다!
//			RequestDispatcher dispatcher = 
//					request.getRequestDispatcher("member/member_join_form.jsp");
//			dispatcher.forward(request, response);
			// -----------------------------------------------------------------------
			// 공통 포워딩 작업을 위한 ActionForward 객체 활용
			// 1. ActionForward 객체 생성
			forward = new ActionForward();
			// 2. ActionForward 객체의 setPath() 메서드를 호출하여 이동할 URL 지정
			forward.setPath("member/member_join_form.jsp");
			// 3. ActionForward 객체의 setRedirect() 메서드를 호출하여 포워딩 방식 지정
			//    => 디스패치 방식 포워딩을 위해 false 값을 파라미터로 전달
			forward.setRedirect(false);
			// => boolean 타입의 기본값이 false이므로 생략 가능(but 쓰는 버릇 들이기!)
		} else if(command.equals("/MemberJoinPro.me")) {
			// 회원 가입 처리를 위한 비즈니스 로직 수행 필요
			// Action 클래스(MemberJoinProAction)의 인스턴스 생성 후 execute() 메서드 호출
			// => 파라미터 : HttpServletRequest 객체, HttpServletResponse 객체
			// => 리턴타입 : ActionForward(forward)
			action = new MemberJoinProAction(); // Action 타입으로 업캐스팅
			forward = action.execute(request, response); // 부모 인터페이스가 가진 메서드라서 호출 가능(업캐스팅 이후!)
		} else if(command.equals("/MemberLoginForm.me")) {
			System.out.println("회원 로그인 폼!");
			// 로그인 폼(member/member_login_form.jsp) 페이지 포워딩 => Dispatch
			// ActionForward 객체 생성 및 포워딩 정보 저장
			forward = new ActionForward();
			forward.setPath("member/member_login_form.jsp");
			forward.setRedirect(false);
			// => boolean 타입의 기본값이 false이므로 생략 가능(but 쓰는 버릇 들이기!)
		} else if(command.equals("/MemberLoginPro.me")) {
			// 회원 로그인 처리를 위한 비즈니스 로직 수행 필요
			// MemberLoginProAction 클래스 활용
			action = new MemberLoginProAction(); // Action 타입으로 업캐스팅
			forward = action.execute(request, response); // 부모인터페이스가 가진 메서드라서 호출 가능(업캐스팅 이후!)
		} else if(command.equals("/MemberLogout.me")) {
			// 회원 로그아웃 처리를 위한 비즈니스 로직 수행 필요
			action = new MemberLogoutAction();  
			forward = action.execute(request, response); 
		} else if(command.equals("/MemberInfo.me")) {
			// 회원 상세정보 조회를 위한 비즈니스 로직 수행 필요
			action = new MemberInfoAction(); 
			forward = action.execute(request, response); 
		} else if(command.equals("/MemberWithdrawForm.me")) {
			System.out.println("회원 탈퇴 폼!");
			// 회원탈퇴 폼(member/member_withdraw_form.jsp) 페이지 포워딩 => Dispatch
			forward = new ActionForward();
			forward.setPath("member/member_withdraw_form.jsp");
			forward.setRedirect(false);
		} else if(command.equals("/MemberWithdrawPro.me")) {
			// 회원탈퇴를 위한 비즈니스 로직 수행 필요
			action = new MemberWithdrawProAction();
			forward = action.execute(request, response);
		} else if(command.equals("/MemberModifyForm.me")) {
			// 회원 정보 수정 폼 
			// => 단, 회원 정보 조회 후 뷰페이지로 이동하므로
			//    회원 정보 조회를 위한 비즈니스 로직 수행 필요
			action = new MemberModifyFormAction();
			forward = action.execute(request, response);
		} else if(command.equals("/MemberModifyPro.me")) {
			// 회원 정보 수정 비즈니스 로직 처리 
			action = new MemberModifyProAction();
			forward = action.execute(request, response);
		}
		
		// -----------------------------------------------------------------------------
		// ActionForward 객체의 포워딩 정보를 활용하여 공통 포워딩 작업 처리
		// 1. ActionForward 객체가 null 이 아닐 경우 판별
		if(forward != null) {
			// 2. ActionForward 객체에 저장된 포워딩 방식 판별(Redirect vs Dispatch)
			if(forward.isRedirect()) { // 리다이렉트 방식
				// 3. 포워딩(포워딩 경로는 ActionForward 객체의 getPath() 메서드 활용)
				// 3-1. 리다이렉트 방식 포워딩 수행
				response.sendRedirect(forward.getPath());
			} else { // 디스패치 방식
				// 3. 포워딩(포워딩 경로는 ActionForward 객체의 getPath() 메서드 활용)
				// 3-2. 디스패치 방식 포워딩 수행
				RequestDispatcher dispatcher = 
						request.getRequestDispatcher(forward.getPath());
				dispatcher.forward(request, response);
			}
		} 
		// 만약, ActionForward 객체가 null 일 경우 아무 동작도 수행하지 않음(포워딩 X)
		// => 주의! 응답 정보가 전송되지 않는 것이 아니라 포워딩 동작을 수행하지 않는 것이다!(포워딩 안하고 자바스크립트 코드만 실행!) 

	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// POST 방식 요청일 때 한글 파라미터 인코딩 설정
		request.setCharacterEncoding("UTF-8");
		doProcess(request, response);
	}

}
