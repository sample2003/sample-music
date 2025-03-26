import service from "@/api/request";

const userService = {
    // 用户登录
    userLogin: function (userLoginData) {
        if (!userLoginData.text || !userLoginData.password) {
            return 'empty';
        }
        return service({
            method: "POST",
            url: "/api/user/login",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            data: {
                text: userLoginData.text,
                password: userLoginData.password
            }
        }).then(response => {
            // 处理响应
            localStorage.setItem("userToken", `Bearer ${response.data}`);
            return response;
        }).catch(error => {
            // 处理错误
            console.error(error);
        });
    },
    // 用户注册
    userRegister: function (userRegisterData) {
        return service({
            method: "POST",
            url: "/api/user/register",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            data: {
                username: userRegisterData.username,
                email: userRegisterData.email,
                password: userRegisterData.password,
                invitationCode: userRegisterData.invitationCode,
                verificationCode: userRegisterData.verificationCode,
            }
        }).then(response => {
            // 处理响应
            return response;
        }).catch(error => {
            // 处理错误
            console.error(error);
            throw error;
        });
    },
    // 用户信息
    userInfo() {
        return service({
            method: "Get",
            url: "/api/user/info",
        }).then(response => {
            // 处理响应
            return response.data;
        }).catch(error => {
            // 处理错误
            throw error;
        });
    },
    updateUserExp(exp) {
        return service({
            method: "PATCH",
            url: `/api/user/updateUserExp`,
            data: exp
        }).then(response => {
            return response.data;
        }).catch(error => {
            // 处理错误
            throw error;
        });
    }
};
export default userService;