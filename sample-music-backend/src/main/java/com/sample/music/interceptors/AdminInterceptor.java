package com.sample.music.interceptors;

import com.sample.music.annotation.AdminOnly;
import com.sample.music.annotation.Public;
import com.sample.music.exception.BusinessException;
import com.sample.music.service.UserService;
import com.sample.music.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

import static com.sample.music.constant.HttpStatusCode.*;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            // 注解 @AdminOnly，标记需要管理员权限的接口
            boolean methodHasAdminOnly = handlerMethod.getMethodAnnotation(AdminOnly.class) != null;
            boolean classHasAdminOnly = handlerMethod.getBeanType().isAnnotationPresent(AdminOnly.class);
            // 注解 @Public，开放接口
            boolean methodHasPublic = handlerMethod.getMethodAnnotation(Public.class) != null;
            boolean classHasPublic = handlerMethod.getBeanType().isAnnotationPresent(Public.class);
            // 如果有Public接口则放行
            if(methodHasPublic || classHasPublic){
                return true;
            }
            if (methodHasAdminOnly || classHasAdminOnly)
            {
                // 获取用户的登录信息
                Map<String, Object> map = ThreadLocalUtil.get();
                if(map == null) throw new BusinessException(UNAUTHORIZED, "未登录");
                // 查询是否为管理员身份
                Integer id = (Integer) map.get("id");
                Long userId = id.longValue();
                if (userService.isAdmin(userId)) {
                    System.out.println("isAdmin");
                    // 如果是管理员，放行
                    return true;
                } else {
                    System.out.println("noAdmin");
                    // 如果不是管理员，返回403 Forbidden
                    throw new BusinessException(FORBIDDEN, "未拥有权限");
                }
            }
        }
        // 对于没有标记 @AdminOnly 的接口，放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
        // 移除ThreadLocal中的数据
        ThreadLocalUtil.remove();
    }
}
