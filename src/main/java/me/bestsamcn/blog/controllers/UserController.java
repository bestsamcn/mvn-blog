package me.bestsamcn.blog.controllers;

import me.bestsamcn.blog.services.FactoryService;
import me.bestsamcn.blog.utils.SessionUtils;
import me.bestsamcn.blog.utils.Utils;
import net.sf.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * @Author: Sam
 * @Date: 2018/9/12 21:39
 */
@WebServlet(urlPatterns = {"/user/*"})
public class UserController extends BaseController {

    //新增用户
    public void add(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String account = req.getParameter("account");
        String password = req.getParameter("password");
        JSONObject ret = FactoryService.getUserService().add(account, password);
        resp.getWriter().println(ret);
    }

    //删除用户
    public void delete (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        JSONObject ret = FactoryService.getUserService().delete(id);
        resp.getWriter().println(ret);
    }

    //获取用户
    public void getById (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        JSONObject ret = (JSONObject) FactoryService.getUserService().getById(id, false);
        resp.getWriter().println(ret);
    }

    //编辑用户
    public void edit (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String email = req.getParameter("email");
        String mobile = req.getParameter("mobile");
        JSONObject ret = FactoryService.getUserService().edit(id, email, mobile);
        resp.getWriter().println(ret);
    }

    //登陆
    public void login(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        Cookie cookie = Utils.getCookie(req, "JSESSIONID");

        //重复登陆隔离
        if(cookie != null){
            String jsessionid = (String) cookie.getValue();
            HttpSession session = SessionUtils.getSession(jsessionid);
            if(session != null){
                String userId = (String) session.getAttribute("userId");
                if(userId != null && userId.length() == 32 ){
                    JSONObject ret = Utils.setResponse(-1, "你已登录", "null");
                    resp.getWriter().println(ret);
                    return;
                }
            }
        }
        String account = req.getParameter("account");
        String password = req.getParameter("password");
        JSONObject ret =  FactoryService.getUserService().login(account, password);

        //设置session,会自动设置cookie,getSession(boolean)false为不自动创建
        req.getSession().setAttribute("userId", ret.get("data"));
        Cookie retcookie = new Cookie("JSESSIONID", req.getSession().getId());

        //保持登陆一天
        retcookie.setMaxAge(3600);
        retcookie.setPath("/");
        resp.addCookie(retcookie);
        resp.getWriter().println(ret);
    }

    //获取当前用户信息
    public void getInfo(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        String userId = (String) req.getSession().getAttribute("userId");

        //未登陆隔离
        if(userId == null || userId.isEmpty()){
            JSONObject ret = Utils.setResponse(-1, "请先登陆", "null");
            resp.getWriter().println(ret);
            return;
        }
        JSONObject ret = (JSONObject) FactoryService.getUserService().getById(userId, false);
        resp.getWriter().println(ret);
    }

    //退出
    public void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        req.getSession().removeAttribute("userId");
        req.getSession().invalidate();
        Cookie cookie = Utils.getCookie(req, "JESSIONID");
        if(cookie !=null){
            cookie.setMaxAge(0);
            resp.addCookie(cookie);
        }
        JSONObject ret = Utils.setResponse(-1, "你已退出", "null");
        resp.getWriter().println(ret);
    }
}
