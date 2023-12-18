package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import svc.BoardDetailService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardDetailAction_Backup implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("BoardDetailAction");
		
		ActionForward forward = null;
		
		int board_num = Integer.parseInt(request.getParameter("board_num"));
//		System.out.println("글번호 : " + board_num);
		// 페이지번호는 현재 작업에서 실제로 활용될 데이터가 아니므로
		// 다음 페이지로 이동 시 전달하는 용도로만 사용하므로 파라미터 가져오기 불필요
		
		// BoardDetailService - getBoard() 메서드 호출하여 게시물 상세정보 조회 작업 요청
		// => 파라미터 : 글번호   리턴타입 : BoardBean(board)
		BoardDetailService service = new BoardDetailService();
		BoardBean board = service.getBoard(board_num, true);
//		System.out.println(board);
		
		request.setAttribute("board", board);
		
		// ActionForward 객체를 사용하여 "board/board_view.jsp" 포워딩 정보 설정
		forward = new ActionForward();
		forward.setPath("board/board_view.jsp");
		forward.setRedirect(false);
		
		return forward;
	}

}









