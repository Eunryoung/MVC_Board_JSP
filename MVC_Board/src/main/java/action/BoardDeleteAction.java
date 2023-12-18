package action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import svc.BoardDeleteService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardDeleteAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("BoardDeleteAction");
		
		ActionForward forward = null;
		
		// 세션 아이디 저장(단, 세션 아이디가 없을 경우 자바스크립트 오류 출력 및 이전페이지로 이동)
		// 세션 아이디 존재하지 않을 경우 처리
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
		
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		
		// 페이지 번호는 저장 할 필요가 없지만 파라미터 전달이 제대로 안될 경우를 대비하여 작성해도 된다(옵션)
		// 페이지번호는 작업 완료 후 페이지 이동 시 전달만 해도 되므로 변수 저장 생략 가능
		String pageNum = request.getParameter("pageNum");
		// 만약, 페이지번호가 null 또는 널스트링일 경우 기본값 "1" 설정
		if(pageNum == null || pageNum.equals("")) {
			pageNum = "1";
		}
		
		// BoardDeleteService - isBoardWriter() 메서드 호출하여 게시물 작성자 확인 요청
		// => 파라미터 : 글번호, 세션아이디   리턴타입 : boolean(isBoardWriter)
		BoardDeleteService service = new BoardDeleteService();
//		boolean isBoardWriter = service.isBoardWriter(board_num, id);
		
		// 글번호와 세션아이디를 BoardBean 객체에 저장(세션아이디 = 작성자)하는 경우
		// BoardDeleteService - isBoardWriter() 메서드 호출하여 게시물 작성자 확인 요청
		// => 파라미터 : BoardBean 객체   리턴타입 : boolean(isBoardWriter)
		BoardBean board = new BoardBean();
		board.setBoard_num(board_num);
		board.setBoard_name(id);
		
		boolean isBoardWriter = service.isBoardWriter(board);
//		System.out.println(isBoardWriter);
		
		// 확인 결과 판별
		// 게시물 작성자가 일치하지 않으면 
		// 자바스크립트 사용하여 "삭제 권한이 없습니다!" 출력 후 이전페이지로 돌아가기
		if(!isBoardWriter) {
			try {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('삭제 권한이 없습니다!');");
				out.println("history.back();");
				out.println("</script>");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		// BoardDeleteService - removeBoard() 메서드 호출하여 게시물 삭제 요청
		// => 파라미터 : 글번호(board_num)   리턴타입 : boolean(isDeleteSuccess)
		boolean isDeleteSuccess = service.removeBoard(board_num);
		
		// 삭제 결과 판별
		// 실패 시 자바스크립트로 "삭제 실패!" 출력 후 이전페이지로 돌아가기
		// 성공 시 BoardList.bo 서블릿 요청(페이지번호 전달)
		if(!isDeleteSuccess) {
			try {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('삭제 권한이 없습니다!');");
				out.println("history.back();");
				out.println("</script>");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		} 
		
		forward = new ActionForward();
		forward.setPath("BoardList.bo?pageNum=" + pageNum);
		forward.setRedirect(true);
		
		return forward;
	}
	
}












