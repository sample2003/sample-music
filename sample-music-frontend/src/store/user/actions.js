import userService from "@/api/service/userService";

export default {
    async getLoginUser({commit}) {
        const res = await userService.userInfo();
        commit("setLoginUser", res);
    },
}