import Vue from "vue";
import Vuex from "vuex";
import musicActions from "@/store/music/actions";
import musicMutations from "@/store/music/mutations";
import musicGetters from "@/store/music/getters";
import musicState from "@/store/music/state";

import userState from "@/store/user/state";
import userMutations from "@/store/user/mutations";
import userActions from "@/store/user/actions"
import state from "@/store/music/state";

Vue.use(Vuex);

const store = new Vuex.Store({
    modules: {
        music: {
            namespaced: true,
            actions: musicActions,
            mutations: musicMutations,
            getters: musicGetters,
            state: musicState,
        },
        user: {
            namespaced: true,
            actions: userActions,
            mutations: userMutations,
            state: userState,
        }
    },
});
export default store;