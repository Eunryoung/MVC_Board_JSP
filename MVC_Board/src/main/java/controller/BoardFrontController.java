package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import action.BoardDeleteAction;
import action.BoardDetailAction;
import action.BoardListAction;
import action.BoardModifyFormAction;
import action.BoardModifyProAction;
import action.BoardReplyFormAction;
import action.BoardReplyProAction;
import action.BoardWriteProAction;
import vo.ActionForward;

@WebServlet("*.bo")
public class BoardFrontController extends HttpServlet {
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("BoardFrontController");
		
		// 공통 변수 선언
		Action action = null;
		ActionForward forward = null;
		
		// 서블릿 주소 추출하기
		String command = request.getServletPath();
		System.out.println("command : " + command);
		
		// URL 매핑
		if(command.equals("/BoardWriteForm.bo")) {
			// 뷰페이지로 바로 이동
			forward = new ActionForward();
			forward.setPath("board/board_write_form.jsp");
			forward.setRedirect(false); // Dispatch
		} else if(command.equals("/BoardWritePro.bo")) {
			// 비즈니스 로직 처리
			action = new BoardWriteProAction();
			forward = action.execute(request, response);
		} else if(command.equals("/BoardList.bo")) {
			// 비즈니스 로직 처리
			action = new BoardListAction();
			forward = action.execute(request, response);
		} else if(command.equals("/BoardDetail.bo")) {
			// 비즈니스 로직 처리
			action = new BoardDetailAction();
			forward = action.execute(request, response);
		} else if(command.equals("/BoardDelete.bo")) {
			// 비즈니스 로직 처리
			action = new BoardDeleteAction();
			forward = action.execute(request, response);
		} else if(command.equals("/BoardModifyForm.bo")) {
			// 비즈니스 로직 처리
			action = new BoardModifyFormAction();
			forward = action.execute(request, response);
		} else if(command.equals("/BoardModifyPro.bo")) {
			// 비즈니스 로직 처리
			action = new BoardModifyProAction();
			forward = action.execute(request, response);
		} else if(command.equals("/BoardReplyForm.bo")) {
			action = new BoardReplyFormAction();
			forward = action.execute(request, response);
		} else if(command.equals("/BoardReplyPro.bo")) {
			action = new BoardReplyProAction();
			forward = action.execute(request, response);
		}
		
		
		// -----------------------------------------------------------------
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









