package action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import svc.BoardWriteProService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardWriteProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("BoardWriteProAction");
		
		ActionForward forward = null;

		// 세션 아이디가 존재하지 않을 경우(null)
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("sId");
		
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
			return null;
		}	
		
		// 폼 파라미터(작성자, 제목, 내용) 가져와서 BoardBean 객체에 저장
		BoardBean board = new BoardBean();
		board.setBoard_name(request.getParameter("board_name"));
		board.setBoard_subject(request.getParameter("board_subject"));
		board.setBoard_content(request.getParameter("board_content"));
		System.out.println(board); // toString() 생략
		
		// -------------------------------------------------------------------------------
		// 클라이언트의 IP 주소를 가져오는 경우
		board.setWriter_ip(request.getRemoteAddr());
//		System.out.println("작성자 IP주소 : " + board.getWriter_ip());
		// -----------------------------------------------------------
		// BoardWriteProService - registBoard() 메서드 호출하여 글쓰기 작업 요청
		// => 파라미터 : BoardBean 객체   리턴타입 : boolean(isWriteSuccess)
		BoardWriteProService service = new BoardWriteProService();
		boolean isWriteSuccess = service.registBoard(board);
		
		// 글쓰기 작업 요청 처리 결과 판별 - 리턴값(isWriteSuccess) 활용
		// => 실패 시 자바스크립트를 사용하여 "글쓰기 실패!" 출력 및 이전페이지로 돌아가기
		// => 성공 시 글목록 표시를 위해 BoardList.bo 서블릿 주소를 Redirect 방식으로 설정
		if(!isWriteSuccess) { // 실패
			try {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('글쓰기 실패!');");
				out.println("history.back();");
				out.println("</script>");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else { // 성공
			forward = new ActionForward();
			forward.setPath("BoardList.bo");
			forward.setRedirect(true); // Redirect
		}
		
		return forward;
	}

}
