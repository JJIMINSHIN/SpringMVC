package hello.servlet.basic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest requset, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("hellllo");

        String username = requset.getParameter("username");
        System.out.println("username : "+username);

        // 응답 메세지 보내기
        //text/plain : 단순 문자
        //header정보에 들어감
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        //getWriter() : http 바디에 메세지 들어감
        response.getWriter().write("hello "+username);
    }
}
