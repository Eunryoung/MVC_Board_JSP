package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import svc.BoardDetailService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardDetailAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("BoardDetailAction");
		
		ActionForward forward = null;
		
		int board_num = Integer.parseInt(request.getParameter("board_num"));
//		System.out.println("글번호 : " + board_num);
		// 페이지번호는 현재 작업에서 실제로 활용될 데이터가 아니므로
		// 다음 페이지로 이동 시 전달하는 용도로만 사용하므로 파라미터 가져오기 불필요
		
		// ------------------- 조회수 조작 방지를 위한 대책 ---------------------
		// 게시물의 글번호를 세션 객체내의 readBoardList 속성에 저장
		// => 양식은 xx/yy/zz/
		// => 단, 기존의 세션 객체 "readBoardList" 값이 존재하지 않을 경우(null)
		//    조회 성공한 게시물 번호("yy/")를 세션에 저장하고
		//    아니면, 기존의 게시물 번호("xx/") 뒤에 새 게시물 번호("yy/")를 연결
		//    ex) 기존 세션값 : "xx/" => 연결 후 세션값 : "xx/yy/"
		// => 또한, 기존의 목록 중 새로 등록할 글번호가 이미 존재할 경우 추가 X
		//    (기존 문자열 내에 원하는 문자열이 포함되는지 판별 : contains() 메서드 활용)
		HttpSession session = request.getSession(); // 세션 객체 가져오기
		// 저장된 조회 글 번호 목록 값 가져와서 변수에 저장
		String readBoardList = (String)session.getAttribute("readBoardList");
		System.out.println("기존 readBoardList : " + readBoardList);
		String newReadBoardNum = board_num + "/";
		
		// BoardDetailService - getBoard() 메서드 호출하여 게시물 상세정보 조회 작업 요청
		// => 파라미터 : 글번호   리턴타입 : BoardBean(board)
		// => 추가사항) 글 수정, 답글 작성과 구분하여 조회수 증가도 함께 수행하기 위해
		//    boolean 타입 값을 신호로 전달(true : 조회수 증가, false : 조회수 미증가)
		BoardDetailService service = new BoardDetailService();
		
		// 조회수 증가여부를 저장할 변수 선언(true : 증가, false : 미증가)
		boolean isIncreaseReadcount = false;
		
		// 기존 세션값 판별
		if(readBoardList == null) { // 세션값이 없을 경우(기존에 조회하지 않은 게시물)
			// 새로운 글 번호를 세션값에 저장(덮어쓰기)하기 위해 글목록 값에 새 글 번호 저장
			readBoardList = newReadBoardNum;
			isIncreaseReadcount = true; // 조회수 증가 작업 수행하도록 표시
		} else if(readBoardList != null) { // 기존 조회 게시물 목록 존재할 경우
			// 목록에 게시물 번호가 1개만 존재할 때, 해당 번호가 일치하는지 검사(equals)
			// 또는, 번호 앞에 "/" 기호 붙여서 "/xx/" 번호가 포함되는지 검사(contains)
			if(readBoardList.split("/").length == 1 && readBoardList.equals(newReadBoardNum) 
					|| !readBoardList.contains("/" + newReadBoardNum)) {
				// 새로운 글 번호를 세션값 뒤에 문자열 결합하여 세션에 저장
				readBoardList += newReadBoardNum;
				isIncreaseReadcount = true; // 조회수 증가 작업 수행하도록 표시
			}
		}
		
		// 조회수 증가 여부 변수값에 따라 조회 과정에서 조회수 증가 여부가 달라짐 
		BoardBean board = service.getBoard(board_num, isIncreaseReadcount);
//		System.out.println(board);
		
		// 조회된 게시물이 존재할 경우에만 세션값 저장(결합된 목록 저장)
		if(board != null) {
			session.setAttribute("readBoardList", readBoardList);
			System.out.println(session.getAttribute("readBoardList"));
			// request 객체에 BoardBean 객체 저장
			request.setAttribute("board", board);
		}
		
		
		
		// ActionForward 객체를 사용하여 "board/board_view.jsp" 포워딩 정보 설정
		forward = new ActionForward();
		forward.setPath("board/board_view.jsp");
		forward.setRedirect(false);
		
		return forward;
	}

}









