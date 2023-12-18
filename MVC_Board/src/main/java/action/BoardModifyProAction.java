package action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import svc.BoardModifyProService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardModifyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("BoardModifyProAction");
		
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
		
		// 폼 파라미터 데이터 가져오기
//		int board_num = ;
//		String board_subject = request.getParameter("board_subject");
//		String board_content = request.getParameter("board_content");
//		System.out.println(board_num + ", " + board_subject + ", " + board_content);
		
		// 폼 파라미터 데이터(글번호, 제목, 내용, 세션 아이디)를 BoardBean 객체에 저장
		// => 세션아이디는 board_name 에 저장
		BoardBean board = new BoardBean();
		board.setBoard_num(Integer.parseInt(request.getParameter("board_num")));
		board.setBoard_subject(request.getParameter("board_subject"));
		board.setBoard_content(request.getParameter("board_content"));
		board.setBoard_name(id);
//		System.out.println(board);
		
		// BoardModifyProService - isBoardWriter() 메서드 호출하여 작성자 확인 작업 요청
		// => 파라미터 : BoardBean 객체   리턴타입 : boolean(isBoardWriter)
		BoardModifyProService service = new BoardModifyProService();
		boolean isBoardWriter = service.isBoardWriter(board);
		
		// 확인 결과 판별
		// 게시물 작성자가 일치하지 않으면 
		// 자바스크립트 사용하여 "수정 권한이 없습니다!" 출력 후 이전페이지로 돌아가기
		// => 단, 현재 상세 페이지에서 수정 버튼을 권한이 없을 경우 제거했으므로
		//    사용자가 일치하지 않는지 판별 여부는 생략해도 무관함
		//    (다만, 강제로 POST 방식 요청 데이터를 생성하여 요청도 가능하므로 가급적 추가)
		if(!isBoardWriter) {
			try {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('수정 권한이 없습니다!');");
				out.println("history.back();");
				out.println("</script>");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		// BoardModifyProService - modifyBoard() 메서드 호출하여 글 수정 작업 요청
		// => 파라미터 : BoardBean 객체   리턴타입 : boolean(isModifySuccess)
		boolean isModifySuccess = service.modifyBoard(board);
		
		// 수정 작업 요청 결과 판별
		// => 실패 시 자바스크립트 활용 "글 수정 실패!" 출력 후 이전페이지로 돌아가기
		//    성공 시 글 상세정보 조회("BoardDetail.bo") 서블릿 주소 요청
		//    (파라미터 : 글번호, 페이지번호)
		if(!isModifySuccess) {
			try {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('글 수정 실패!');");
				out.println("history.back();");
				out.println("</script>");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		// 페이지번호 없을 경우 기본값 처리
		String pageNum = request.getParameter("pageNum");
		// 만약, 페이지번호가 null 또는 널스트링 일 경우 기본값 "1" 설정
		if(pageNum == null || pageNum.equals("")) {
			pageNum = "1";
		}
		
		forward = new ActionForward();
		forward.setPath("BoardDetail.bo?board_num=" + board.getBoard_num() + "&pageNum=" + pageNum);
		forward.setRedirect(true);
		
		return forward;
	}

}















