package svc;

import java.sql.Connection;

import dao.BoardDAO;
import db.JdbcUtil;
import vo.BoardBean;

public class BoardModifyProService {

	// 게시물 작성자 확인 요청을 수행하는 isBoardWriter() 메서드 정의
	public boolean isBoardWriter(BoardBean board) {
		boolean isBoardWriter = false;
		
		Connection con = JdbcUtil.getConnection();
		BoardDAO dao = BoardDAO.getInstance();
		dao.setConnection(con);
		
		// MemberDAO - isBoardWriter() 메서드 호출하여 글목록 조회 작업 요청
		// => 파라미터 : BoardBean 객체   리턴타입 : boolean(isBoardWriter)
		// (BoardDAO 클래스의 isBoardWriter() 메서드 재사용)
		isBoardWriter = dao.isBoardWriter(board);
		
		JdbcUtil.close(con);
		
		return isBoardWriter;
	}

	// 글 수정 작업 요청을 수행하는 modifyBoard() 메서드 정의
	public boolean modifyBoard(BoardBean board) {
		boolean isModifySuccess = false;
		
		Connection con = JdbcUtil.getConnection();
		BoardDAO dao = BoardDAO.getInstance();
		dao.setConnection(con);
		
		// BoardDAO - updateBoard() 메서드 호출하여 글 수정 작업 요청
		// => 파라미터 : BoardBean 객체   리턴타입 : int(updateCount)
		int updateCount = dao.updateBoard(board);
		
		// 트랜잭션 처리(글 삭제 결과 판별)
		// => 성공 시 커밋 작업 수행 및 isModifySuccess 를 true 로 변경
		//    실패 시 롤백 작업 수행
		if(updateCount > 0) {
			JdbcUtil.commit(con);
			isModifySuccess = true;
		} else {
			JdbcUtil.rollback(con);
		}
		
		JdbcUtil.close(con);
		
		return isModifySuccess;
	}

}









