package Member;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class MemberController{

	private Log log = LogFactory.getLog(getClass());
	private final MembersDAO membersDAO;


	@Autowired
	public MemberController(MembersDAO membersDAO, ReservationDAO reservationDAO, EvaluationDAO evaluationDAO) {
		super();
		this.membersDAO = membersDAO;

	}

	// By Jay_회원가입 폼 호출 하기_20210405
	@RequestMapping(value="/user/register.do", method = RequestMethod.GET)
	public String form() {
		log.info("MemberController의 form()호출됨");
		return "user/registration";
	}

	// By Jay_회원가입 이용약관 호출 하기_20210405
	@RequestMapping(value="user/registration_term.do", method = RequestMethod.GET)
	public String registration_term() {
		log.info("MemberController의 registration_term()호출됨");
		return "user/registration_term";
	}

	// By Jay_회원 가입하기_20210405
	@RequestMapping(value="/user/register.do", method = RequestMethod.POST)
	@ResponseBody
	public String signUp(MembersDTO members) {
		log.info("MemberController의 signUp()호출됨");

		int result = membersDAO.idCheck(members);

		// 1이면 아이디 중복, 0이면 아이디 없음
		if(result == 1) {
			return "fail";
		} else if (result == 0) {
			membersDAO.userJoin(members);
		}
		return "success";
	}

	// By Jay_로그인 폼 호출 하기_20210406
	@RequestMapping(value="/user/login.do", method = RequestMethod.GET)
	public String login() {
		log.info("MemberController의 login()호출됨");
		return "user/user_login";
	}

	// By Jay_로그인 하기_20210406
	@RequestMapping(value="/user/login.do", method = RequestMethod.POST)
	@ResponseBody
	public String singIn (@ModelAttribute MembersDTO members, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("MemberController의 signIn()호출됨");

		// page_login.jsp에서 아이디 기억하기 name값(remember) 가져오기
		String user_check = request.getParameter("remember_userId");

		String result = null;
		MembersDTO userIdCheck = membersDAO.getOne(members);
		log.info(userIdCheck);
		if(userIdCheck == null) {
			result= "idFail";
			return result;
		}

		if(members.getMember_pwd().equals(userIdCheck.getMember_pwd())) {
			session.setAttribute("loginUser", userIdCheck);
			result = "success";

			if(user_check.equals("true")) {
				Cookie cookie = new Cookie("user_check", members.getMember_id());
				response.addCookie(cookie);
			} else {
				Cookie cookie = new Cookie("user_check", members.getMember_id());
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}

		} else {
			result = "pwdFail";
		}
		log.info(result);
		return result;
	}

	// By Jay_로그아웃 하기_20210408
	@RequestMapping(value="/user/logout.do", method = RequestMethod.GET)
	public String logout(HttpSession Session) throws IOException {
		log.info("MemberController의 logout()호출됨");
		Session.invalidate();
		return "user/logout";
	}

	// By Lsh_마이페이지에서 로그인한 회원정보 뿌리기_20210429
	@RequestMapping(value="/user/mypage_info.do", method = RequestMethod.GET)
	public String getMember(@RequestParam String member_id, Model model) {
	     log.info("MemberController의 ()호출됨");
	     MembersDTO member=membersDAO.getMember(member_id);
	     model.addAttribute("member", member);
	     return "user/mypage_info";
	}

	// By Lsh_회원 수정 하기 폼으로 이동_20210429
	@RequestMapping(value="/user/mypage_infoModify.do", method = RequestMethod.GET)
	public String Modify(@RequestParam String member_id, Model model) {
	     log.info("MemberController의 ()호출됨");

	     MembersDTO member=membersDAO.getMember(member_id);
	     model.addAttribute("member", member);
	     return "user/mypage_modify";
	}

	// By Lsh_회원 수정 하기_20210429
	@RequestMapping(value="/user/mypage_infoModify.do", method = RequestMethod.POST)
	@ResponseBody
	public String update(@ModelAttribute MembersDTO members) {
		membersDAO.updateMember(members);
	    return "success";
	}

	// By Lsh_회원 탈퇴 하기 폼으로 이동_20210429
	@RequestMapping(value="/user/mypage_infoDelete.do", method = RequestMethod.GET)
	public String deleteForm(@RequestParam String member_id, Model model) {
	    log.info("MemberController의 deleteForm()호출됨");

	    MembersDTO member=membersDAO.getMember(member_id);
	    model.addAttribute("member", member);
	    return "user/mypage_delete";
	}

	// By Lsh_회원 탈퇴 하기_20210429
	@RequestMapping(value="/user/mypage_infoDelete.do", method = RequestMethod.POST)
	@ResponseBody
	public String deleteUp(@ModelAttribute MembersDTO members, HttpSession session) {
	    log.info("MemberController의 deleteUp()호출됨");

	    String result = null;
	    MembersDTO userIdCheck = membersDAO.getOne(members);

	    //db에 담겨져있는 비밀번호와 입력한 비밀번호가 같다면
	    //success(탈퇴) 틀리다면 pwdFail떨어져서 다시입력해야된다
	    if(members.getMember_pwd().equals(userIdCheck.getMember_pwd())) {
	       session.invalidate();
	       membersDAO.deleteMember(members);
	       result = "success";
	    } else {
	       result = "pwdFail";
	    }
	    return result;
	   }


	// By Jay_회원 당 구매후기 관련 리스트로 이동_20210430
/*	@RequestMapping(value="user/mypage_evaluation.do", method = RequestMethod.GET)
	public String orderEvaluationList(@RequestParam String member_id, Model model) {
	   log.info("MemberController의 orderEvaluationList()호출됨");
	   List<MemberOrderListDTO> OrderList = reservationDAO.orderEvaluationList(member_id);
	   int count = reservationDAO.orderEvaluationNum(member_id);
	   List<TotalEvaluationDTO> evaluationList =  evaluationDAO.evaluation_list_byMemberId(member_id);
	   List<EvaluationReplyDTO> replyList = evaluationDAO.evaluation_reply_Entrylist();
	   reservationDAO.orderUsed();

	   model.addAttribute("OrderList", OrderList);
	   model.addAttribute("count", count);
	   model.addAttribute("evaluationList", evaluationList);
	   model.addAttribute("replyList", replyList);

	   return "user/mypage_evaluation";
	 }

	// By Jay_회원 당 구매후기 관련 작성 페이지로 이동_20210501
	@RequestMapping(value="user/mypage_evaluationWrite.do", method = RequestMethod.GET)
	public String EvaluationWriteForm(@RequestParam int reser_number, Model model) {
	   log.info("MemberController의 EvaluationWriteForm()호출됨");
	   MemberOrderListDTO memberOrder = reservationDAO.getOrderOneByReser_number(reser_number);
	   model.addAttribute("memberOrder", memberOrder);
	   return "user/mypage_evaluationWrite";
	 }

	// By Jay_회원 당 구매후기 관련 작성하기_20210501
	@RequestMapping(value="user/mypage_evaluationWrite.do", method = RequestMethod.POST)
	@ResponseBody
	public String EvaluationWrite(@ModelAttribute EvaluationDTO evaluationDTO) {
	   log.info("MemberController의 EvaluationWrite()호출됨");
	   evaluationDAO.evaluationWrite(evaluationDTO);
	   evaluationDAO.evaluationCheckChange(evaluationDTO.getReser_number());

	   return "success";
	 }*/

}



