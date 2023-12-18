package svc;

import java.sql.Connection;

import dao.MemberDAO;
import db.JdbcUtil;
import vo.MemberStatus;

public class MemberWithdrawProService {

	// 회원 탈퇴 요청 수행할 withdrawMember() 메서드
	public boolean withdrawMember(String id) {
		boolean isWithdrawSuccess = false;
		
		Connection con = JdbcUtil.getConnection();
		MemberDAO dao = MemberDAO.getInstance();
		dao.setConnection(con);
		
		// MemberDAO - updateMemberStatus() 메서드 호출하여 회원 상태 수정 요청
		// => 파라미터 : 아이디, 3(탈퇴)   리턴타입 : int(updateCount)
		// => 탈퇴 상태를 직접 정수로 입력하지 않고 MemberStatus 클래스의 상수 활용
		int updateCount = dao.updateMemberStatus(id, MemberStatus.WITHDRAW);
		
		// 탈퇴 처리 결과 판별
		// => 성공 시 commit 수행 및 isWithdrawSuccess 를 true 로 변경
		// => 실패 시 rollback 수행
		if(updateCount > 0) { // 성공
 			JdbcUtil.commit(con);
 			isWithdrawSuccess = true;
		} else { // 실패
			JdbcUtil.rollback(con);
//			isWithdrawSuccess = false;
		}
		
		JdbcUtil.close(con);
		
		return isWithdrawSuccess;
	}

}










