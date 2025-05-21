<template>
  <div id="userEnter">
    <div class="enter">
      <div class="container">
        <div class="box" data-text="Sample -Music">
          <div v-show="this.isShowLogin1" class="content">
            <p style="color: #000"><span style="font-size: 1.1em;line-height: 1.4em;">欢迎你</span>，请填写基本信息</p>
            <form @submit.prevent="handleLogin" style="width: 100%">
              <TextInput
                  message="请输入账号或邮箱"
                  @loginInput="handleLoginParam"
                  :value="loginParam.text"
                  param-key="text"
                  :user-icon="true"
                  :cancel-icon="true"
                  style="height: 30%"/>
              <TextInput
                  message="请输入密码"
                  type="password"
                  @loginInput="handleLoginParam"
                  :value="loginParam.password"
                  param-key="password"
                  :password-icon="true"
                  :cancel-icon="true"
                  style="height: 30%"/>
              <button class="handleButton" type="submit">登录</button >
            </form>
          </div>
          <div v-show="this.isShowLogin2" class="content qrcode">
            <p style="color: #000"><span style="font-size: 1.1em;line-height: 1.4em;">欢迎你</span>，请微信扫一扫扫码登录
            </p>
            <div>
              <img src="../../assets/static/qrcode.png" alt="">
            </div>
          </div>
          <div v-show="this.isShowRegister" class="content">
            <p style="color: #000"><span style="font-weight: bolder;">欢迎</span>，请填写基本信息</p>
            <form @submit.prevent="handleRegister" style="width: 100%">
              <TextInput
                  message="请输入注册邮箱"
                  @registerInput="handleRegisterParam"
                  :value="registerParam.email"
                  param-key="email"
                  :email-icon="true"
                  :cancel-icon="true"
                  style="height: 30%"/>
              <TextInput
                  message="请输入用户名"
                  @registerInput="handleRegisterParam"
                  :value="registerParam.username"
                  param-key="username"
                  :user-icon="true"
                  :cancel-icon="true"
                  style="height: 30%"/>
              <TextInput
                  message="请输入密码"
                  type="password"
                  @registerInput="handleRegisterParam"
                  :value="registerParam.password"
                  param-key="password"
                  :password-icon="true"
                  :cancel-icon="true"
                  style="height: 30%"/>
              <TextInput
                  message="请输入验证码"
                  @registerInput="handleRegisterParam"
                  :value="registerParam.verificationCode"
                  v-show="this.registerParam.email"
                  param-key="verificationCode"
                  :verify-icon="true"
                  style="height: 30%;width: 70%;margin: 2%;"/>
              <a class="msg"
                 @click="handleSendVerifyCode"
                 :class="{'disabled' : countdown > 0}"
                 v-show="registerParam.email">{{ countdownText }}</a>
              <TextInput
                  message="请输入邀请码"
                  @registerInput="handleRegisterParam"
                  :value="registerParam.invitationCode"
                  param-key="invitationCode"
                  :invitation-icon="true"
                  :cancel-icon="true"
                  style="height: 30%"/>
              <button class="handleButton" type="submit">注册</button>
            </form>
          </div>
        </div>
        <div class="select">
          <ButtonSelect
              :button-list="btn"
              :is-active="(item) => currentBtn === item.btnName"
              :key-field="'btnID'"
              :icon-field="'btnIcon'"
              :click-param-field="'btnName'"
              @button-click="show"
          ></ButtonSelect>
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
import ButtonSelect from "@/components/ButtonSelect.vue";
import TextInput from "@/components/TextInput.vue";
import UserService from "@/api/service/userService";

export default {
  name: "UserEnter",
  components: {TextInput, ButtonSelect},
  data() {
    const btn = [
      {
        btnID: nanoid(),
        btnName: "emailOrAccountLogin",
        name: "邮箱/账号",
        btnIcon: Icon.userIcon
      },
      {
        btnID: nanoid(),
        btnName: "wechatLogin",
        name: "微信登录",
        btnIcon: Icon.wechatIcon
      },
      {
        btnID: nanoid(),
        btnName: "emailRegister",
        name: "用户注册",
        btnIcon: Icon.registerIcon
      },
    ]
    return {
      currentBtn: 'emailOrAccountLogin',
      btn: btn,
      countdown: 0,
      timer: null,
      isShowLogin1: true,
      isShowLogin2: false,
      isShowRegister: false,
      loginParam: UserLoginData,
      registerParam: UserRegisterData
    }
  },
  computed: {
    countdownText() {
      return this.countdown > 0 ? `${this.countdown}s` : '发送验证码'
    }
  },
  methods: {
    show(param) {
      if (param === this.currentBtn) return
      this.currentBtn = param;
      if (param === this.btn[0].btnName) {
        this.isShowLogin2 = false;
        this.isShowRegister = false;
        this.isShowLogin1 = true;
      }
      if (param === this.btn[1].btnName) {
        this.isShowRegister = false;
        this.isShowLogin1 = false;
        this.isShowLogin2 = true;
      }
      if (param === this.btn[2].btnName) {
        this.isShowLogin1 = false;
        this.isShowLogin2 = false;
        this.isShowRegister = true;
      }
    },
    handleLoginParam(param, value) {
      this.loginParam[param] = value;
    },
    // 用户登录
    async handleLogin() {
      for (const key in this.loginParam) {
        if (this.loginParam[key] === '') {
          this.$message("请完整填写登录信息")
          return;
        }
      }
      
      try {
        const res = await userService.loginUser(this.loginParam);
        if (res.code === 401) {
          this.$message("密码错误")
        } else if (res.code === 403) {
          this.$message("该用户已被封禁")
        } else if (res.code === 404) {
          this.$message("该用户未注册")
        } else {
          // 登录成功的处理逻辑
          await store.dispatch('user/getLoginUser')
          this.loginParam = this.clear(this.loginParam)
          this.$message("登录成功")
          // 登录成功的处理逻辑
          await this.$router.push('/music/home');
        }
      } catch (error) {
        // 登录失败的处理逻辑
        console.error(error);
      }
    },
    handleRegisterParam(param, value) {
      this.$set(this.registerParam, param, value)
    },
    // 用户注册
    async handleRegister() {
      console.log(this.registerParam)
      try {
        // 使用本地响应式数据提交
        const res = await userService.registerUser({...this.registerParam})
        console.log(res)
        if (res === '404') {
          this.$message("请完整填写登录信息")
        } else if (res === 401) {
          this.$message("该邮箱已被注册")
        } else if (res === 403) {
          this.$message("该用户已被封禁")
        } else if (res === 404) {
          this.$message("该用户未注册")
        } else {
          // 注册成功后清空数据
          this.registerParam = this.clear(this.registerParam)
          this.showLogin()
        }
      } catch (error) {
        console.error(error)
      }
    },
    // 发送验证码
    async handleSendVerifyCode() {
      // 如果正在倒计时，阻止操作
      if (this.countdown > 0) return

      try {
        // 调用发送验证码接口
        this.$message("验证码已发送")
        await this.sendVerifyCode()

        // 开始倒计时
        this.countdown = 60
        this.timer = setInterval(() => {
          this.countdown--
          if (this.countdown <= 0) {
            clearInterval(this.timer)
          }
        }, 1000)
      } catch (error) {
        console.error('发送失败:', error)
      }
    },
    async sendVerifyCode() {
      const emailVerify = {
        email: this.registerParam.email
      }
      await UserService.sendVerifyCode(emailVerify);
    },
    clear(params) {
      Object.keys(params).forEach(key => {
        params[key] = ''
      })
      return params;
    },
  }
}
</script>

<style scoped>
#userEnter {
  width: 100vw;
  height: 100vh;
  background-color: var(--second-color);
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
  position: relative;
  left: 0;
  padding: 30px;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
  z-index: 1;
  transition: 0.5s;
  display: flex;
  flex-wrap: wrap;
  justify-content: start;
  align-items: center;
}

.qrcode img {
  width: 60%;
}

.content div {
  width: 100%;
}

.msg {
  width: 23%;
  cursor: pointer;
  display: inline-block;
  font-size: 0.8em;
  background: #222;
  color: #fff;
  box-shadow: 0 2px 15px #222;
  transition: 0.2s all ease;
  border-radius: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  text-decoration: none;
  padding: 5px 0;
}

.msg:hover {
  color: #222;
  background: #fff;
  box-shadow: 0 2px 15px #fff;
}

.handleButton {
  width: 100%;
  cursor: pointer;
  display: inline-block;
  font-size: 1.1em;
  background: #222;
  color: #fff;
  box-shadow: 0 2px 15px #222;
  border-radius: 5px;
  text-decoration: none;
  font-weight: 700;
  transition: 0.2s all ease;
  padding: 5px 0;
  margin-top: 10px;
  border: none;
}

.handleButton:hover {
  color: #222;
  background: #fff;
  box-shadow: 0 2px 15px #fff;
}

/*创建前面和后面的色带*/
.box[data-text]::before,
.box[data-text]::after {
  content: attr(data-text);
  position: absolute;
  background: #fff;
  top: 0;
  left: .5em;
  width: 50%;
  height: 100%;
  /* 新增文字样式 */
  display: flex;
  align-items: center;
  justify-content: center;
  text-decoration: none;
  white-space: nowrap;
  font-size: 10em;
  color: #222;
  letter-spacing: 10px;
  word-spacing: 2em;

  border-radius: 8px;
  transform: skewX(10deg);
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
  left: 40px;
  width: calc(100% - 90px);
  transform: skewX(0deg);
}

/*给色带上色*/
.box:nth-child(1)::before,
.box:nth-child(1)::after {
  animation: changeColor 4s ease infinite running;
  background-image: linear-gradient(315deg, var(--main-color), var(--second-color));
}

.select {
  padding: 10px;
}

.disabled {
  cursor: not-allowed;
  opacity: 0.6;
  pointer-events: none; /* 禁用鼠标事件 */
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