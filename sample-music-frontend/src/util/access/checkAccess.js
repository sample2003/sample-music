import ACCESS_ENUM from "@/util/access/accessEnum";

/**
 * 检查权限（判断当前登录用户是否具有某个权限）
 * @param userDetail 当前登录用户
 * @param needAccess 需要有的权限
 * @return boolean 有无权限
 */
const checkAccess = (userDetail, needAccess) => {
    // 获取当前登录用户具有的权限（如果没有 loginUser，则表示未登录）
    const loginUserAccess = userDetail.role ? userDetail.role : ACCESS_ENUM.NOT_LOGIN;
    if (needAccess === ACCESS_ENUM.NOT_LOGIN) {
        return true;
    }
    // 如果用户登录才能访问
    if (needAccess === ACCESS_ENUM.USER) {
        // 如果用户没登录，那么表示无权限
        if (loginUserAccess === ACCESS_ENUM.NOT_LOGIN) {
            return false;
        }
    }
    // 如果需要管理员权限
    if (needAccess === ACCESS_ENUM.ADMIN) {
        // 如果不为管理员，表示无权限
        if (loginUserAccess !== ACCESS_ENUM.ADMIN) {
            return false;
        }
    }
    return true;
};

export default checkAccess;
