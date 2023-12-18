package svc;

import java.sql.Connection;

import dao.MemberDAO;
import db.JdbcUtil;
import vo.MemberBean;

public class MemberInfoService {

	// 회원 상세정보 조회 요청을 수행하는 getMember() 메서드
	public MemberBean getMember(String id) {
		System.out.println("MemberInfoService");
		
		MemberBean member = null;
		
		Connection con = JdbcUtil.getConnection();
		MemberDAO dao = MemberDAO.getInstance();
		dao.setConnection(con);
		
		// MemberDAO - selectMember() 메서드 호출하여 회원 상세정보 조회 요청
				// => 파라미터 : 아이디   리턴타입 : MemberBean(member)
				member = dao.selectMember(id);
				
				JdbcUtil.close(con);
				
				return member;
			}

		}








