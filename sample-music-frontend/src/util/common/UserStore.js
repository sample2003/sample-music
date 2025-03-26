import {mapActions, mapMutations, mapState} from "vuex";

export default {
    computed: {
        ...mapState({
            userToken: state => state.user.userToken,
            userDetail: state => state.user.userDetail,
        }),
    },
    methods: {
        ...mapActions(['']),
        ...mapMutations({
            setUserToken: 'user/setUserToken',
            setUserDetail: 'user/setLoginUser',
        }),
    }
}