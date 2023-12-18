package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vo.ActionForward;

// XXXAction 클래스(컨트롤러)에서 수행할 작업에 대한 메서드를
// 공통 형식으로 제공하기 위한 추상메서드를 포함하는 인터페이스를 정의
// => 각 액션 클래스에서 상속에 대한 제약을 덜어주기 위해 인터페이스로 정의(= 다중 구현 가능)
// => XXXAction 클래스에서는 Action 인터페이스를 상속받아 추상메서드를 구현해야한다!
//    (구현의 강제성을 부여하여, 코드(메서드 선언부)의 통일성 유지 가능)
public interface Action {
	// 공통 메서드 execute() 정의(= 추상메서드)
	// => 파라미터 : HttpServletRequest(request), HttpServletResponse(response)
	// => 리턴타입 : vo.ActionForward
	// => 인터페이스 내의 모든 메서드는 추상메서드이며, public abstract 가 고정되어 있음(생략 가능)
	public abstract ActionForward execute(HttpServletRequest request, HttpServletResponse response); 
}
