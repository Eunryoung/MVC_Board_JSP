package svc;

import java.sql.Connection;

import dao.BoardDAO;
import db.JdbcUtil;
import vo.BoardBean;

public class BoardDetailService {

	// 게시물 상세정보 조회 요청 수행할 getBoard() 메서드
//	public BoardBean getBoard(int board_num) {
//		System.out.println("BoardDetailService - getBoard()");
//		BoardBean board = null;
//		
//		Connection con = JdbcUtil.getConnection();
//		BoardDAO dao = BoardDAO.getInstance();
//		dao.setConnection(con);
//		
//		// BoardDAO - selectBoard() 메서드 호출하여 글 상세정보 조회 작업 요청
//		// => 파라미터 : 글번호(board_num)   리턴타입 : BoardBean(board)
//		board = dao.selectBoard(board_num);
//		
//		// 게시물 조회 성공(리턴받은 BoardBean 객체가 null 이 아닐 때) 시
//		// 해당 게시물의 조회수(board_readcount) 증가 작업 요청
//		// BoardDAO - updateReadcount() 메서드 호출
//		// => 파라미터 : 글번호(board_num)   리턴타입 : int(updateCount)
//		if(board != null) {
//			int updateCount = dao.updateReadcount(board_num);
//			
//			// 작업 성공 시 commit 수행 및 BoardBean 객체의 readcount 값 1 증가시키고
//			// 아니면(실패 시) rollback 수행
//			if(updateCount > 0) { // 성공
//				JdbcUtil.commit(con);
//				board.setBoard_readcount(board.getBoard_readcount() + 1);
//			} else { // 실패
//				JdbcUtil.rollback(con);
//			}
//			
//		}
//		
//		JdbcUtil.close(con);
//		
//		return board;
//	}
	// 조회수 증가 여부를 함께 전달받을 경우
	public BoardBean getBoard(int board_num, boolean isIncreaseReadcount) {
		System.out.println("BoardDetailService - getBoard()");
		BoardBean board = null;
		
		Connection con = JdbcUtil.getConnection();
		BoardDAO dao = BoardDAO.getInstance();
		dao.setConnection(con);
		
		// BoardDAO - selectBoard() 메서드 호출하여 글 상세정보 조회 작업 요청
		// => 파라미터 : 글번호(board_num)   리턴타입 : BoardBean(board)
		board = dao.selectBoard(board_num);
		
		// 게시물 조회 성공(리턴받은 BoardBean 객체가 null 이 아닐 때) 시
		// 해당 게시물의 조회수(board_readcount) 증가 작업 요청
		// BoardDAO - updateReadcount() 메서드 호출
		// => 파라미터 : 글번호(board_num)   리턴타입 : int(updateCount)
		// => 단, 게시물 조회 성공 조건과 함께 조회수 증가 신호(isIncreaseReadcount) 값이
		//    true일 경우에만 조회수 증가 수행하도록 변경
		if(board != null && isIncreaseReadcount) {
			int updateCount = dao.updateReadcount(board_num);
			
			// 작업 성공 시 commit 수행 및 BoardBean 객체의 readcount 값 1 증가시키고
			// 아니면(실패 시) rollback 수행
			if(updateCount > 0) { // 성공
				JdbcUtil.commit(con);
				board.setBoard_readcount(board.getBoard_readcount() + 1);
			} else { // 실패
				JdbcUtil.rollback(con);
			}
			
		}
		
		JdbcUtil.close(con);
		
		return board;
	}

}
