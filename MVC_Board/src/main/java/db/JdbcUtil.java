package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

// 데이터베이스 작업 준비 및 해제(= 자원반환) 작업을 공통으로 수행할
// 공통 메서드를 갖는 JdbcUtil 클래스 정의
public class JdbcUtil {
	// 1. 데이터베이스 접근을 통해 Connection 객체를 생성 및 외부로 리턴하는 
	//	  getConnection() 메서드 정의(DB 작업 1단계 & 2단계에 해당)
	// => 파라미터 : 없음		 리턴타입 : java.sql.Connection
	//    (정보를 미리 context.xml에 저장해놓았으므로 파라미터 불필요!)
	// => 단, JdbcUtil 클래스의 인스턴스 생성 없이도 메서드 호출이 가능하도록
	//	  static 메서드로 정의
	public static Connection getConnection() {
		// context.xml에 설정된 DBCP(커넥션풀)로부터 Connection 객체 가져와서 외부로 리턴
		Connection con = null; // DB 연결 객체를 관리할 Connection 타입 변수 선언
		
		try {
			// 1. 톰캣으로부터 톰캣 객체에서 관리하는 context.xml 파일에 대한 정보 관리를 수행하는
			//    Context 객체 가져오기(context.xml 파일 내의 <Context> 태그에 해당하는 부분)
			// => InitialContext 객체 생성 후 Context 타입으로 업캐스팅하여 저장
			Context initCtx = new InitialContext(); // Unhandled exception type NamingException
			
			// 2. 생성된 Context 객체(initCtx)로부터 context.xml 파일에 정의된
			//    <Resource> 태그 부분 가져오기
			// => 1번에서 생성된 Context 객체의 lookup() 메서드를 호출하여 
			//    파라미터로 "java:comp/env" 문자열 전달
			// => 리턴타입 Object 인 객체를 Context 타입으로 다운캐스팅
//			Context envCtx = (Context)initCtx.lookup("java:comp/env");

			// 3. Resource 태그 내의 name 속성값(= 리소스 이름 jdbc/MySQL) 가져오기 위해
			//    2번에서 생성된 Context 객체(envCtx)의 lookup() 메서드를 호출하여 
			//    파라미터로 리소스 이름 전달
			// => 리턴타입 Object 인 객체를 context.xml 내의 type 속성에 지정된
			//    객체(javax.sql.DataSource) 타입으로 다운캐스팅
//			DataSource ds = (DataSource)envCtx.lookup("jdbc/MySQL");
			// ------- < 참고 > 2번 & 3번 과정을 하나의 문장으로 결합 가능 ---------------------
			DataSource ds = (DataSource)initCtx.lookup("java:comp/env/jdbc/MySQL");
			// ---------------------------------------------------------------------------------
			// 3번까지의 과정이 완료되면 커넥션풀을 관리하는 DataSource 객체가 리턴됨
			// 4. DataSource 객체(= 커넥션풀)로부터 미리 생성된 Connection 객체 가져오기
			con = ds.getConnection(); // Unhandled exception type SQLException 예외 발생
			// => Add catch clause to surrounding try 실행
			
			// 5. 자동 커밋(Auto Commit) 기능 해제(선택사항)
			// => 기본적으로 JDBC 사용 시 Auto Commit 기능이 동작되도록 설정되어 있음(기본값)
			// => Connection 객체(con)의 setAutoCommit() 메서드를 호출하여 설정 변경 가능
			//    (true : Auto Commit 설정(기본값), false : Auto Commit 해제)
			con.setAutoCommit(false); // 자동 커밋 기능 해제
			// => 이제부터 DB 에서 조작을 수행하는 DML 등이 즉시 반영되지 않음
			//    반드시 커밋 작업을 수행해야 이전 작업들이 모두 반영되고
			//    롤백 작업을 통해 이전 상태로 되돌리는 작업도 가능해진다!
			//    (commit, rollback 작업을 수행할 메서드 정의 필요)
			
			// 6. 현재커넥션 정보 확인(옵션)
			// => DataSource 객체를 BasicDataSource 타입으로 다운캐스팅하여 메서드 호출
//			BasicDataSource bds = (BasicDataSource) ds;
//			System.out.println("MaxTotal : " + bds.getMaxTotal()); // 최대 커넥션 수
//			System.out.println("Active : " + bds.getNumActive()); // 현재 사용중인 커넥션 수
//			System.out.println("Idle : " + bds.getNumIdle()); // 유휴 상태 커넥션 수
//			bds.close();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("DB 연결 실패!");
			e.printStackTrace();
		}
		
		// 커넥션풀로부터 리턴받은 데이터베이스 연결 객체가 저장된 Connection 객체 리턴
		return con;
	}
	
	// --------------------------------------------------------------------------------------------
	// 데이터베이스 작업에 대한 Commit, Rollback 기능을 수행할 메서드 정의
	// => 파라미터 : Connection 객체(con)   리턴타입 : void
	public static void commit(Connection con) {
		try {
			// Connection 객체의 commit() 메서드 호출하여 Commit 작업 수행
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void rollback(Connection con) {
		try {
			// Connection 객체의 rollback() 메서드 호출하여 Rollback 작업 수행
			con.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// --------------------------------------------------------------------------------------------
	// 데이터베이스 자원 반환(close())을 공통으로 수행할 close() 메서드 정의
	// => 파라미터 : Connection 타입(con), PreparedStatement 타입(pstmt), ResultSet 타입(rs)		
	// => 리턴타입 : void
	// => 각각의 파라미터를 따로 전달받아 각각 close() 작업을 수행하도록 메서드 정의
	//	  이 때, 각 메서드의 이름은 모두 close()로 통일하고 파라미터만 다르게 정의
	//	  = 메서드 오버로딩(Method Overloading)
	// => 인스턴스 생성 없이도 메서드 호출이 가능하도록 static 메서드 정의
	public static void close(Connection con) {
		try {
			// 전달받은 Connection 객체 반환
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(PreparedStatement pstmt) {
		try {
			// 전달받은 PreparedStatement 객체 반환
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet rs) {
		try {
			// 전달받은 ResultSet 객체 반환
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
