export default {
    setUserToken(state, payload) {
        state.userToken = payload;
    },
    setLoginUser(state, payload) {
        state.userDetail = payload;
    }
}