import service from "@/api/request";

const userService = {

  /**
   * 用户注册
   */
  registerUser: function (userRegisterData) {
    return service({
      method: "POST",
      url: "/api/user/register",
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      data: userRegisterData
    }).then(response => {
      // 处理响应
      return response;
    }).catch(error => {
      // 处理错误
      console.error(error);
      throw error;
    });
  },

  /**
   * 发送验证码
   */
  sendVerifyCode: function (emailVerify) {
    return service({
      method: "POST",
      url: "/api/user/sendVerifyCode",
      data: {
        email: emailVerify.email
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

  /**
   * 用户登录
   */
  loginUser: function (userLoginData) {
    if (!userLoginData.text || !userLoginData.password) {
      return;
    }
    return service({
      method: "POST",
      url: "/api/user/login",
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      data: userLoginData
    }).then(response => {
      // 处理响应
      localStorage.setItem("userToken", `Bearer ${response.data}`);
      return response;
    }).catch(error => {
      // 处理错误
      console.error(error);
    });
  },

  /**
   * 生成二维码
   */
  generateQrcode: function (type) {
    return service({})
  },

  /**
   * 用户登出
   */
  logoutUser() {
    return service({
      method: "GET",
      url: "/api/user/logout"
    }).then(response => {
      // 处理响应
      return response;
    }).catch(error => {
      // 处理错误
      console.error(error);
      throw error;
    });
  },

  /**
   * 用户注销
   */
  cancelUser() {
    return service({
      method: "GET",
      url: "/api/user/cancel"
    }).then(response => {
      // 处理响应
      return response;
    }).catch(error => {
      // 处理错误
      console.error(error);
      throw error;
    });
  },


  /**
   * 用户更新个人信息
   */
  updateUser: function (userUpdateVO) {
    return service({
      method: "PUT",
      url: "/api/user/updateUser",
      data: userUpdateVO
    }).then(response => {
      // 处理响应
      return response;
    }).catch(error => {
      // 处理错误
      console.error(error);
      throw error;
    });
  },

  /**
   * 用户更新密码
   */
  updateUserPwd: function (email, code, newPassword) {
    return service({
      method: "PATCH",
      url: "/api/user/updateUserPwd",
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      data: {
        email: email,
        code: code,
        newPassword: newPassword
      }
    }).then(response => {
      return response;
    }).catch(error => {
      console.error(error);
      throw error;
    })
  },

  /**
   * 用户更新头像
   */
  updateUserAvatar: function (image) {
    return service({
      method: "PATCH",
      url: "/api/user/updateUserAvatar",
      headers: {'Content-Type': ''},
      data: image
    }).then(response => {
      return response;
    }).catch(error => {
      console.error(error);
      throw error;
    })
  },

  /**
   * 用户经验增加
   */
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
  },

  /**
   * 用户查询个人信息
   */
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

};
export default userService;