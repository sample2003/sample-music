import VueRouter from "vue-router";
import Vue from "vue";
import store from "@/store";

import MusicMainHome from "@/pages/music/MusicMainHome.vue";
import MusicMainSearch from "@/pages/music/MusicMainSearch.vue";
import MusicMainList from "@/pages/music/MusicMainList.vue";
import MusicMainPlay from "@/pages/music/MusicMainPlay.vue";
import MusicMainSet from "@/pages/music/MusicMainSet.vue";
import MusicLayout from "@/components/music/MusicLayout.vue";
import MusicDetail from "@/pages/music/MusicDetail.vue";

import UserEnter from "@/components/user/UserEnter.vue";
import UserCollect from "@/pages/user/UserCollect.vue";
import UserCenter from "@/components/user/UserCenter.vue";
import userCollectMusic from "@/pages/user/UserCollectMusic.vue";
import UserSuggest from "@/pages/user/UserSuggest.vue";
import UserInfo from "@/pages/user/UserInfo.vue";
import AdminManage from "@/pages/user/AdminManage.vue";
import UserMusicDetail from "@/pages/user/UserMusicDetail.vue";

import PlaylistManage from "@/pages/manage/PlaylistManage.vue";

import ErrorPage from "@/pages/common/ErrorPage.vue";
import ServerClose from "@/pages/common/ServerClose.vue";

import ACCESS_ENUM from "@/util/access/accessEnum";
import checkAccess from "@/util/access/checkAccess";
import AIChat from "@/pages/user/AIChat.vue";

Vue.use(VueRouter);

const router = new VueRouter({
    mode: 'history', // 使用HTML5 History模式
    routes: [
        {
            path: "/",
            redirect: "/music/home"
        },
        {
            name: "enter",
            path: "/user/enter",
            component: UserEnter
        },
        {
            name: "center",
            path: "/user/center",
            redirect: "/user/center/collect/music/playlist",
            component: UserCenter,
            meta: {
                access: ACCESS_ENUM.USER,
            },
            children: [
                {
                    name: "collect",
                    path: "/user/center/collect",
                    redirect: "/user/center/collect/music/playlist",
                    component: UserCollect,
                    children: [
                        {
                            name: "cPlaylist",
                            path: "/user/center/collect/music/:type",
                            component: userCollectMusic,
                            meta: {
                                access: ACCESS_ENUM.USER,
                            },
                        }
                    ]
                }, {
                    name: "suggest",
                    path: "/user/center/suggest",
                    component: UserSuggest,
                    meta: {
                        access: ACCESS_ENUM.USER,
                    },
                }, {
                    name: "info",
                    path: "/user/center/info",
                    component: UserInfo,
                    meta: {
                        access: ACCESS_ENUM.USER,
                    },
                }, {
                    name: "manage",
                    path: "/user/center/manage",
                    meta: {
                        access: ACCESS_ENUM.ADMIN,
                    },
                    component: AdminManage,
                }
            ]
        },
        {
            name: "music",
            path: "/music",
            redirect: "/music/home",
            component: MusicLayout,
            children: [
                {
                    name: "home",
                    path: "/music/home",
                    component: MusicMainHome
                },
                {
                    name: 'search',
                    path: '/music/search',
                    component: MusicMainSearch,
                    props: true // 启用路由参数传递为props
                },
                {
                    name: "list",
                    path: "/music/list",
                    component: MusicMainList,
                    children: [
                        {
                            name: "listMusicDetail",
                            path: '/music/list/detail/:detailType/:id',
                            component: UserMusicDetail,
                            meta: {
                                access: ACCESS_ENUM.USER,
                            },
                        }, {
                            name: "listMusicManage",
                            path: '/music/list/manage/playlist/:id',
                            component: PlaylistManage,
                            meta: {
                                access: ACCESS_ENUM.USER,
                            },
                        }
                    ]
                },
                {
                    name: "play",
                    path: "/music/play",
                    component: MusicMainPlay
                },
                {
                    name: "set",
                    path: "/music/set",
                    component: MusicMainSet
                },
                {
                    name: "musicDetail",
                    path: '/music/detail/:detailType/:id',
                    component: MusicDetail
                },
                {
                    name: "ai",
                    path: "/music/ai",
                    component: AIChat
                },
            ]
        },
        {
            path: '/:pathMatch(.*)*',
            name: 'ErrorPage',
            component: ErrorPage
        },
        {
            path: '/server-close',
            name: 'ServerClose',
            component: ServerClose
        },
    ]
});
/*router.beforeEach(async (to, from, next) => {
    let userDetail = store.state.user.userDetail;
    // 自动登录
    if (!userDetail || !userDetail.role) {
        // await: 为了用户登录成功之后，再执行后续的代码
        await store.dispatch("user/getLoginUser");
        userDetail = store.state.user.userDetail;
    }
    const needAccess = to.meta.access ? to.meta.access : ACCESS_ENUM.NOT_LOGIN;
    // 要跳转的页面必须需要登录
    if (needAccess !== ACCESS_ENUM.NOT_LOGIN) {
        // 如果没登陆，跳转到登录界面
        if (
            !userDetail ||
            !userDetail.role ||
            userDetail.role === ACCESS_ENUM.NOT_LOGIN
        ) {
            Vue.prototype.$message("未登录")
            return;
        }
        // 如果登录了，但是权限不足，那么跳转到无权限页面
        if (!checkAccess(userDetail, needAccess)) {
            next("/");
            return;
        }
    }
    next();
});*/
router.beforeEach(async (to, from, next) => {
    let userDetail = store.state.user.userDetail;
    // 自动登录
    try {
        if (!userDetail ||!userDetail.role) {
            // 为了用户登录成功之后，再执行后续的代码
            await store.dispatch("user/getLoginUser");
            userDetail = store.state.user.userDetail;
        }
        const needAccess = to.meta.access? to.meta.access : ACCESS_ENUM.NOT_LOGIN;
        // 要跳转的页面必须需要登录
        if (needAccess!== ACCESS_ENUM.NOT_LOGIN) {
            // 如果没登陆，跳转到登录界面
            if (
                !userDetail ||
                !userDetail.role ||
                userDetail.role === ACCESS_ENUM.NOT_LOGIN
            ) {
                // 显示未登录消息
                Vue.prototype.$message("未登录")
                return;
            }
            // 如果登录了，但是权限不足，那么跳转到无权限页面
            if (!checkAccess(userDetail, needAccess)) {
                next("/");
                return;
            }
        }
        next();
    } catch (error) {
        console.error("路由导航时发生错误:", error);
        next({ name: 'ServerClose' }); // 跳转到错误页面
    }
});
export default router;