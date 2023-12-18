package svc;

import java.sql.Connection;

import dao.MemberDAO;
import db.JdbcUtil;
import vo.MemberBean;
import vo.MemberStatus;

public class MemberModifyProService {
	
	// 회원 정보 수정 요청을 위한 modifyMember() 메서드
	public boolean modifyMember(MemberBean member) {
		boolean isModifySuccess = false;
		
		Connection con = JdbcUtil.getConnection();
		MemberDAO dao = MemberDAO.getInstance();
		dao.setConnection(con);
		
		// MemberDAO - updateMember() 메서드 호출하여 회원 정보 수정 요청
		// => 파라미터 : MemberBean 객체   리턴타입 : int(updateCount)
		int updateCount = dao.updateMember(member);
		
		// 탈퇴 처리 결과 판별
		// => 성공 시 commit 수행 및 isModifySuccess 를 true 로 변경
		// => 실패 시 rollback 수행
		if(updateCount > 0) { // 성공
 			JdbcUtil.commit(con);
 			isModifySuccess = true;
		} else { // 실패
			JdbcUtil.rollback(con);
//			isModifySuccess = false;
		}
		
		JdbcUtil.close(con);
		
		return isModifySuccess;
	}

}
