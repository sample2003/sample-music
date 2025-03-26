<template>
  <div id="userEnter">
    <div class="login">
      <div class="container">
        <div class="box">
          <span class="s1" @click="loginInput">登录</span>
          <span class="s2" @click="registerInput">注册</span>
          <div class="content">
            <h2>欢迎您</h2>
            <p style="color: #fff">{{ pContent }}</p>
            <!-- 根据 isHovering 的值显示输入框 -->
            <div v-show="isLoginForm">
              <!-- 输入框 -->
              <div class="search flex">
                <div class="inputBox">
                  <input
                      type="text"
                      required="required"
                      v-model="UserLoginData.text"
                  >
                  <span>请输入用户名或邮箱</span>
                </div>
              </div>
              <!-- 输入框 -->
              <div class="search flex">
                <div class="inputBox">
                  <input
                      type="password"
                      required="required"
                      v-model="UserLoginData.password"
                  >
                  <span>请输入密码</span>
                </div>
              </div>
              <a @click="handleLogin">{{ aContent }}</a>
            </div>
            <div v-show="isRegisterForm">
              <!-- 输入框 -->
              <div class="search flex">
                <div class="inputBox">
                  <input
                      type="text"
                      required="required"
                      v-model="UserRegisterData.username"
                  >
                  <span>请输入用户名</span>
                </div>
              </div>
              <!-- 输入框 -->
              <div class="search flex">
                <div class="inputBox">
                  <input
                      type="text"
                      required="required"
                      v-model="UserRegisterData.email"
                  >
                  <span>请输入邮箱</span>
                </div>
              </div>
              <!-- 输入框 -->
              <div class="search flex">
                <div class="inputBox">
                  <input
                      type="password"
                      required="required"
                      v-model="UserRegisterData.password"
                  >
                  <span>请输入密码</span>
                </div>
              </div>
              <!-- 输入框 -->
              <div class="search flex">
                <div class="inputBox">
                  <input
                      type="text"
                      required="required"
                      v-model="UserRegisterData.invitationCode"
                  >
                  <span>请输入邀请码</span>
                </div>
              </div>
              <a @click="handleRegister">{{ aContent }}</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import userService from "@/api/service/userService";
import {UserLoginData, UserRegisterData} from "@/api/pojo/UserSubmit";
import store from "@/store";

export default {
  name: "UserEnter",
  data() {
    return {
      // 添加一个标志位来控制是否显示输入框
      isLoginForm: false,
      isRegisterForm: false,
    }
  },
  methods: {
    showLogin() {
      this.isRegisterForm = false;
      this.isLoginForm = true;
    },
    showRegister() {
      this.isLoginForm = false;
      this.isRegisterForm = true;
    },
    async handleLogin() {
      try {
        const res = await userService.userLogin(UserLoginData);
        console.log(res)
        if (res === 'empty') {
          this.$message("请完整填写登录信息")
        } else if (res === 401) {
          this.$message("密码错误")
        } else if (res === 403) {
          this.$message("该用户已被封禁")
        } else if (res === 404) {
          this.$message("该用户未注册")
        } else {
          // 登录成功的处理逻辑
          await store.dispatch('user/getLoginUser')
          this.$message("登录成功")
          // 登录成功的处理逻辑
          await this.$router.push('/music/home');
        }
      } catch (error) {
        // 登录失败的处理逻辑
        console.error(error);
      }
    },
    async handleRegister() {
      try {
        await userService.userRegister(UserRegisterData);
        // 注册成功的处理逻辑
        this.UserLoginData.text = UserRegisterData.username;
        this.UserLoginData.password = UserRegisterData.password;
        this.showLogin();
      } catch (error) {
        // 注册失败的处理逻辑
        console.error(error);
      }
    },
    loginInput() {
      if (this.UserLoginData !== null) {
        this.showLogin();
      }
    },
    registerInput() {
      if (this.UserRegisterData !== null) {
        this.showRegister();
      }
    },
  },
  computed: {
    UserRegisterData() {
      return UserRegisterData
    },
    UserLoginData() {
      return UserLoginData
    },
    pContent() {
      if (this.isLoginForm || this.isRegisterForm) {
        return '基本信息';
      } else {
        return '样本音乐提供了超多无损歌曲在线收听，祝你有个好的体验';
      }
    },
    aContent() {
      if (this.isLoginForm) {
        return '登录'
      } else if (this.isRegisterForm) {
        return '注册'
      } else {
        return '登录'
      }
    }
  }
}
</script>

<style scoped>
#userEnter {
  width: 100vw;
  height: 100vh;
  background-color: var(--fourth-color);
  overflow: hidden;
}

/*大容器*/
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  padding: 40px 0;
  line-height: 20px;
}

/*具体盒子*/
.box {
  position: relative;
  display: flex;
  width: 350px;
  height: 400px;
  justify-content: center;
  align-items: center;
  margin: 8vh;
  transition: 0.5s;
}

/*内容部分，层级1*/
.content {
  width: 400px;
  position: relative;
  left: 0;
  padding: 30px;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  z-index: 1;
  transition: 0.5s;
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: center;
}

.content div {
  width: 100%;
}

.search {
  width: 80%;
  height: 35px;
  padding-top: 10px;

}

.inputBox {
  font-size: var(--fontSize);
}

.content h2 {
  color: #fff;
  font-size: 1.1em;
  margin-bottom: 10px;
  line-height: 1.4em;
}

.content a {
  cursor: pointer;
  display: inline-block;
  font-size: 1.1em;
  color: #111;
  background: #fff;
  border-radius: 4px;
  text-decoration: none;
  font-weight: 700;
  transition: 0.2s;
  padding: 5px 10px;
  margin-top: 10px;
}

/*悬浮在盒子上，内容变化*/
.box:hover .content {
  left: -25px;
  padding: 15px 40px;
}

.content a:hover {
  background: #222;
  color: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 15px #222;
}

/*创建前面和后面的色带*/
.box::before,
.box::after {
  content: "";
  position: absolute;
  background: #fff;
  top: 0;
  left: 25%;
  width: 50%;
  height: 100%;
  text-decoration: none;
  border-radius: 8px;
  transform: skewX(15deg);
  transition: 0.5s ease;
  background-size: 300% 100%;
}

/*创建色带后面的模糊效果*/
.box::after {
  filter: blur(30px);
}

/*悬浮时使色带旋转*/
.box:hover::before,
.box:hover::after {
  left: 20px;
  width: calc(100% - 90px);
  transform: skewX(0deg);
}

/*给色带上色*/
.box:nth-child(1)::before,
.box:nth-child(1)::after {
  animation: changeColor 4s ease infinite running;
  background-image: linear-gradient(315deg, var(--main-color), var(--second-color));
}

/*给span定义位置*/
.s1, .s2 {
  cursor: pointer;
  line-height: 100px;
  border: 1px solid black;
  z-index: 6;
}

/*创建左上悬浮的小方块，不悬浮时隐藏*/
.box .s1 {
  content: "";
  position: absolute;
  background: rgba(255, 255, 255, 0.1);;
  top: 0;
  left: 0;
  width: 0;
  height: 0;
  opacity: 0;
  transition: 0.3s;
  backdrop-filter: blur(10px);
  border-radius: 8px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
  animation: animate 2s ease-in-out infinite;
}

/*定义方块大小，悬浮时显示*/
.box .hv1, .box:hover .s1 {
  opacity: 1;
  top: -50px;
  left: 0;
  width: 100px;
  height: 100px;
}

/*创建右下悬浮的小方块，不悬浮时隐藏*/
.box .s2 {
  content: "";
  position: absolute;
  background: rgba(255, 255, 255, 0.1);
  bottom: 0;
  right: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  transition: 0.5s;
  backdrop-filter: blur(10px);
  border-radius: 8px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
  animation: animate 2s ease-in-out infinite;
  animation-delay: -1s;
}

/*定义方块大小，悬浮时显示*/
.box .hv2, .box:hover .s2 {
  opacity: 1;
  bottom: -50px;
  right: 50px;
  width: 100px;
  height: 100px;
}

.inputBox {
  width: 100%;
  height: 100%;
  font-size: 1.1em;
  position: relative;
  text-align: center;
}

.inputBox input {
  width: 100%;
  height: 100%;
  text-indent: 1em;
  border-radius: 10px;
  transition: 0.5s;
  background: var(--second-color);
  cursor: pointer;
  outline: none;
  border: 1px solid var(--fourth-color);
  box-sizing: border-box;
  box-shadow: inset 2px 2px 4px #b3b3b3,
  inset -2px -2px 4px #ffffff;
}

.inputBox > span {
  width: 100%;
  left: 0;
  top: 0.3em;
  color: #999999;
  letter-spacing: 0.1em;
  border-radius: 3px;
  transition: all 0.5s ease;
  position: absolute;
  pointer-events: none;
}

.inputBox input:valid ~ span,
.inputBox input:focus ~ span {
  line-height: 1.5em;
  top: -0.6em;
  letter-spacing: 0.2em;
  color: var(--main-color);
  opacity: 0;
}

.inputBox input:valid,
.inputBox input:focus {
  box-shadow: inset 2px 2px 4px #b3b3b3,
  inset -2px -2px 4px #ffffff;
  border: 1px solid var(--main-color);
}

/*给两个方块的动画*/
@keyframes animate {
  0%,
  100% {
    transform: translateY(10px);
  }
  50% {
    transform: translate(-10px);
  }
}

@keyframes changeColor {
  0% {
    background-position: 0;
  }
  50% {
    background-position: 100%;
  }
  100% {
    background-position: 0;
  }
}
</style>