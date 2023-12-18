package action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardListService;
import vo.ActionForward;
import vo.BoardBean;
import vo.PageInfo;

public class BoardListAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("BoardListAction");
		
		ActionForward forward = null;
		
		// 비회원도 목록 조회가 가능하므로 세션 아이디 체크 생략
		// --------------------------------------------------------------------
		// 페이징 처리를 위해 목록 갯수 제한에 사용될 변수 선언
		// 파라미터로 전달받은 pageNum 값 가져오기
		// => 단, 전달받은 파라미터가 없을 경우(null) 기본값 1 로 지정
		int pageNum = 1;
		if(request.getParameter("pageNum") != null) {
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		}
		System.out.println("현재 페이지 : " + pageNum);
		
		// 한 페이지에서 표시할 글 목록 갯수 지정
		int listLimit = 3;
		
		// 조회 시작 행(레코드) 번호 계산
		// => 0번부터 시작하여 페이지 당 목록 갯수씩 증가해야함
		int startRow = (pageNum - 1) * listLimit;
		// --------------------------------------------------------------------
		// BoardListService - getBoardList() 메서드 호출하여 글목록 조회 요청
		// => 파라미터 : 시작행번호, 목록갯수   리턴타입 : java.util.List<BoardBean>(boardList)
		BoardListService service = new BoardListService();
		List<BoardBean> boardList = service.getBoardList(startRow, listLimit);
//		System.out.println(boardList);
		// --------------------------------------------------------------------
		// JSP 페이지에서의 페이징 처리
		// 한 페이지에서 표시할 페이지 목록(번호) 계산
		// 1) BoardListService - getBoardListCount() 메서드를 호출하여
		//    전체 게시물 수 조회(페이지 목록 계산에 활용)
		// => 파라미터 : 없음   리턴타입 : int(listCount)
		int listCount = service.getBoardListCount();
		System.out.println("전체 게시물 수 : " + listCount);
		
		// 2) 한 페이지에서 표시할 페이지 목록 갯수(페이지 번호 갯수) 설정
		int pageListLimit = 3; // 임시) 페이지 당 페이지 번호 갯수를 3개로 지정
		
		// 3) 전체 페이지 목록 갯수 계산
//		int maxPage = listCount / listLimit;
//		// 페이지 목록 갯수 계산 후 나머지가 0보다 크면 페이지 갯수 + 1 처리
//		if(listCount % listLimit > 0) {
//			maxPage++;
//		}
		// 위의 문장을 삼항연산자로 변환하여 동일한 작업 수행
		// => 10개씩 나눈 나머지가 0보다 크면 + 1 처리 추가
		int maxPage = listCount / listLimit + ((listCount % listLimit) > 0 ? 1 : 0);
		

//		System.out.println("전체 페이지 목록 갯수 : " + maxPage);
		
		// 4) 시작 페이지 번호 계산
		// ex) pageListLimit = 3 일 때
		//     1페이지 : (1 - 1) / 3 * 3 + 1 = 1, 2페이지 : (2 - 1) / 3 * 3 + 1 = 1, 3페이지 : (3 - 1) / 3 * 3 + 1 = 1 
		//     4페이지 : (4 - 1) / 3 * 3 + 1 = 4, 5페이지 : (5 - 1) / 3 * 3 + 1 = 4, 6페이지 : (6 - 1) / 3 * 3 + 1 = 4 
		int startPage = (pageNum - 1) / pageListLimit * pageListLimit + 1;
//		System.out.println("시작 페이지 번호 : " + startPage);
		
		// 5) 끝 페이지 번호 계산
		// 시작 페이지 번호와 한 페이지 당 페이지 번호 갯수를 더한 값 - 1
		int endPage = startPage + pageListLimit - 1;
		
		// 6) 만약, 끝 페이지 번호(endPage)가 전체(최대) 페이지 번호(maxPage) 보다 클 경우
		//    끝 페이지 번호를 최대 페이지 번호로 교체
		if(endPage > maxPage) {
			endPage = maxPage;
		}
//		System.out.println("끝 페이지 번호 : " + endPage);
		
		// 계산된 페이징 처리 관련 값을 PageInfo 객체에 저장
		PageInfo pageInfo = new PageInfo(listCount, pageListLimit, maxPage, startPage, endPage);
		
		// --------------------------------------------------------------------
		// 글목록(List 객체)과 페이징정보(PageInfo 객체)를 request 객체에 저장
		request.setAttribute("boardList", boardList);
		request.setAttribute("pageInfo", pageInfo);
		
		
		// ActionForward 객체를 사용하여 "board/board_list.jsp" 포워딩 정보 설정
		forward = new ActionForward();
		forward.setPath("board/board_list.jsp");
		forward.setRedirect(false);
		
		return forward;
	}

}









