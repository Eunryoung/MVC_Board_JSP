package svc;

import java.sql.Connection;

import dao.BoardDAO;
import db.JdbcUtil;
import vo.BoardBean;

public class BoardDeleteService {

	// 글 삭제를 위한 본인 확인 작업 요청하는 isBoardWriter() 메서드 정의
//	public boolean isBoardWriter(int board_num, String id) {
//		boolean isBoardWriter = false;
//		
//		Connection con = JdbcUtil.getConnection();
//		BoardDAO dao = BoardDAO.getInstance();
//		dao.setConnection(con);
//		
//		// MemberDAO - isBoardWriter() 메서드 호출하여 글목록 조회 작업 요청
//		// => 파라미터 : 글번호, 아이디   리턴타입 : boolean(isBoardWriter)
//		isBoardWriter = dao.isBoardWriter(board_num, id);
//		
//		JdbcUtil.close(con);
//		
//		return isBoardWriter;
//	}

	// 글 삭제를 위한 본인 확인 작업 요청하는 isBoardWriter() 메서드 정의
	// => 단, 파라미터를 BoardBean 타입으로 변경
	public boolean isBoardWriter(BoardBean board) {
		boolean isBoardWriter = false;
		
		Connection con = JdbcUtil.getConnection();
		BoardDAO dao = BoardDAO.getInstance();
		dao.setConnection(con);
		
		// BoardDAO - isBoardWriter() 메서드 호출하여 글목록 조회 작업 요청
		// => 파라미터 : BoardBean 객체   리턴타입 : boolean(isBoardWriter)
		isBoardWriter = dao.isBoardWriter(board);
		
		JdbcUtil.close(con);
		
		return isBoardWriter;
	}
	
	// 글 삭제 작업 요청을 수행하는 removeBoard() 메서드 정의
	public boolean removeBoard(int board_num) {
		boolean isDeleteSuccess = false;
		
		Connection con = JdbcUtil.getConnection();
		BoardDAO dao = BoardDAO.getInstance();
		dao.setConnection(con);
		
		// BoardDAO - deleteBoard() 메서드 호출하여 글 삭제 작업 요청
		// => 파라미터 : 글번호   리턴타입 : int(deleteCount)
		int deleteCount = dao.deleteBoard(board_num);
		
		// 글 삭제 결과 판별
		// => 성공 시 커밋 작업 수행 및 isDeleteSuccess 를 true 로 변경
		//    실패 시 롤백 작업 수행
		if(deleteCount > 0) {
			JdbcUtil.commit(con);
			isDeleteSuccess = true;
		} else {
			JdbcUtil.rollback(con);
		}
		
		JdbcUtil.close(con);
		
		return isDeleteSuccess;
	}


}










