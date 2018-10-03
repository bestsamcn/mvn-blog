package me.bestsamcn.blog.controllers;

import me.bestsamcn.blog.services.FactoryService;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: Sam
 * @Date: 2018/9/12 21:39
 */
@WebServlet(urlPatterns = {"/tag/*"})
public class TagController extends BaseController{

    //获取单条数据
    public void getById(HttpServletRequest req, HttpServletResponse reps) throws ServletException, IOException{
        String id = req.getParameter("id");
        JSONObject ret = FactoryService.getTagService().getById(id);
        reps.getWriter().println(ret);

    }

    //新增
    public void add(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        String name = req.getParameter("name");
        JSONObject ret = FactoryService.getTagService().add(name);
        resp.getWriter().println(ret);

    }

    //修改
    public void edit(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String clickNum = req.getParameter("clickNum");
        JSONObject ret = FactoryService.getTagService().edit(id, name, clickNum);
        resp.getWriter().println(ret);
    }

    //删除
    public void delete(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        String id = req.getParameter("id");
        JSONObject ret = FactoryService.getTagService().delete(id);
        resp.getWriter().println(ret);
    }

    //分页
    public void getList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject ret = null;
        String pageIndex = req.getParameter("pageIndex");
        String pageSize = req.getParameter("pageSize");
        ret = FactoryService.getTagService().getList(pageIndex, pageSize);
        resp.getWriter().println(ret);
    }

    //获取全部
    public void getAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject ret = null;
        ret = FactoryService.getTagService().getAll();
        resp.getWriter().println(ret);
    }
}