package click.gudrb33333.metaworldapi.util;

import click.gudrb33333.metaworldapi.entity.Member;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionUtil {
//  // Unused
//  public HttpSession getSession() {
//    ServletRequestAttributes attr =
//        (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//    return attr.getRequest().getSession();
//  }

  public Member getCurrentMember() {
    ServletRequestAttributes attr =
        (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

    return (Member) attr.getRequest().getSession().getAttribute("current-member");
  }
}
