<template>
  <div id="userEnter">
    <div class="enter">
      <div class="container">
        <div class="box">
          <div class="content">
            <h2>欢迎您</h2>
            <p style="color: #000">{{ pContent }}</p>
            <!-- 根据 isHovering 的值显示输入框 -->
            <div>
              <TextInput
                  message="请输入账号或邮箱"
                  @inputChange="handleInputParam"
                  param-key="text"
                  :user-icon="true"
                  :cancel-icon="true"
                  style="height: 30%" />
              <TextInput
                  message="请输入密码"
                  type="password"
                  @inputChange="handleInputParam"
                  param-key="password"
                  :user-icon="true"
                  :cancel-icon="true"
                  style="height: 30%" />
              <!-- 输入框 -->
<!--              <div class="search flex">
                <div class="inputBox">
                  <input
                      type="password"
                      required="required"
                      v-model="UserLoginData.password"
                  >
                  <span>请输入密码</span>
                </div>
              </div>-->
              <a @click="handleLogin">{{ aContent }}</a>
            </div>
<!--            <div v-show="isRegisterForm">
              &lt;!&ndash; 输入框 &ndash;&gt;
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
              &lt;!&ndash; 输入框 &ndash;&gt;
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
              &lt;!&ndash; 输入框 &ndash;&gt;
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
              &lt;!&ndash; 输入框 &ndash;&gt;
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
            </div>-->
          </div>
        </div>
        <div class="select">
          <ButtonSelect :button-list="btn"></ButtonSelect>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
import userService from "@/api/service/userService";
import {UserLoginData, UserRegisterData} from "@/api/pojo/UserSubmit";
import store from "@/store";
import {nanoid} from "nanoid";
import Icon from "@/util/common/Icon";
import ButtonSelect from "@/pages/common/ButtonSelect.vue";
import TextInput from "@/pages/common/TextInput.vue";

export default {
  name: "UserEnter",
  components: {TextInput, ButtonSelect},
  data() {
    const btn = [
      {
        btnID: nanoid(),
        btnName: "info",
        name: "邮箱/账号",
        btnIcon: Icon.userIcon
      },
      {
        btnID: nanoid(),
        btnName: "suggest",
        name: "微信登录",
        btnIcon: Icon.suggestIcon
      },
      {
        btnID: nanoid(),
        btnName: "music",
        name: "用户注册",
        btnIcon: Icon.homeIcon
      },
    ]
    return {
      // 添加一个标志位来控制是否显示输入框
      currentBtn: '',
      btn: btn
    }

  },
  methods: {
    handleInputParam(param, value) {
      UserLoginData[param] = value;
    },
    // 用户登录
    async handleLogin() {
      try {
        const res = await userService.userLogin(UserLoginData);
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
    // 用户注册
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
        return '请填写基本信息';
      } else {
        return '祝你有个好的体验';
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
  flex-direction: column;
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
  height: 60%;
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
  color: #000;
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