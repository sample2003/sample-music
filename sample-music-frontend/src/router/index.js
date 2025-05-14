import VueRouter from "vue-router";
import Vue from "vue";
import store from "@/store";

import MusicMainHome from "@/pages/music/MusicMainHome.vue";
import MusicMainSearch from "@/pages/music/MusicMainSearch.vue";
import MusicMainList from "@/pages/music/MusicMainList.vue";
import MusicMainPlay from "@/pages/music/MusicMainPlay.vue";
import MusicMainSet from "@/pages/music/MusicMainSet.vue";
import MusicLayout from "@/base/music/MusicLayout.vue";
import MusicDetail from "@/pages/music/MusicDetail.vue";

import UserEnter from "@/base/user/UserEnter.vue";
import UserCollect from "@/pages/user/UserCollect.vue";
import UserCenter from "@/base/user/UserCenter.vue";
import userCollectMusic from "@/pages/user/UserCollectMusic.vue";
import UserSuggest from "@/pages/user/UserSuggest.vue";
import UserInfo from "@/pages/user/UserInfo.vue";
import AdminManage from "@/pages/user/AdminManage.vue";
import UserMusicDetail from "@/pages/user/UserMusicDetail.vue";

import ErrorPage from "@/components/ErrorPage.vue";
import ServerClose from "@/components/ServerClose.vue";

import ACCESS_ENUM from "@/util/access/accessEnum";
import checkAccess from "@/util/access/checkAccess";
import AIChat from "@/pages/user/AIChat.vue";
import UserPlaylistManage from "@/pages/music/UserPlaylistManage.vue";
import AdminUpload from "@/pages/user/AdminUpload.vue";
import MusicPublicity from "@/pages/music/MusicPublicity.vue";

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
          component: AdminManage
        }, {
          name: "upload",
          path: "/user/center/upload",
          props: (route) => ({
            arrayParam: route.query.arrayParam // 从 query 映射到 props
          }),
          meta: {
            access: ACCESS_ENUM.ADMIN,
          },
          component: AdminUpload
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
          path: '/music/search/:condition?/:params?/:sortType?/:pageNum?/:pageSize?',
          component: MusicMainSearch,
          props: route => ({
            condition: route.params.condition || '',
            params: route.params.params || '',
            sortType: route.params.sortType || '',
            pageNum: Number(route.params.pageNum) || 1,
            pageSize: Number(route.params.pageSize) || 10
          }) // 启用路由参数传递为props
        },
        {
          name: "list",
          path: "/music/list",
          component: MusicMainList,
          children: [
            {
              name: "listMusicDetail",
              path: '/music/list/detail/:detailType/:id/:condition?/:params?/:sortType?/:pageNum?/:pageSize?',
              props: route => ({
                condition: route.params.condition || '',
                params: route.params.params || '',
                sortType: route.params.sortType || '',
                pageNum: Number(route.params.pageNum) || 1,
                pageSize: Number(route.params.pageSize) || 10
              }), // 启用路由参数传递为props
              component: UserMusicDetail,
              meta: {
                access: ACCESS_ENUM.USER,
              },
            }, {
              name: "listMusicManage",
              path: '/music/list/manage/playlist/:id',
              component: UserPlaylistManage,
              meta: {
                access: ACCESS_ENUM.USER,
              },
              props: true
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
          name: "AI",
          path: "/music/ai",
          component: AIChat
        },
        {
          name: "publicity",
          path: "/music/publicity/:id",
          component: MusicPublicity
        }
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

router.beforeEach(async (to, from, next) => {
  if (to.name === 'ServerClose') {
    next();
    return;
  }

  let userDetail = store.state.user.userDetail;

  try {
    if (!userDetail?.role) {
      await store.dispatch("user/getLoginUser");
      userDetail = store.state.user.userDetail;
    }

    const needAccess = to.meta.access ?? ACCESS_ENUM.NOT_LOGIN;

    if (needAccess !== ACCESS_ENUM.NOT_LOGIN) {
      if (!userDetail?.role || userDetail.role === ACCESS_ENUM.NOT_LOGIN) {
        Vue.prototype.$message("未登录");
        return;
      }

      if (!checkAccess(userDetail, needAccess)) {
        next("/");
        return;
      }
    }
    next();
  } catch (error) {
    console.error("服务器可能已关闭:", error);
  }
});
export default router;