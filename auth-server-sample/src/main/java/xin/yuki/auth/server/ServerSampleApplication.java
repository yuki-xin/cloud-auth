package xin.yuki.auth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SpringBootApplication
@Controller
public class ServerSampleApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ServerSampleApplication.class, args);
    }

    @RequestMapping("/test")
    public void post(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final ServletContext servletContext = request.getServletContext();
        final RequestDispatcher rd = servletContext.getRequestDispatcher("/error");
        rd.forward(request,response);
    }
}
