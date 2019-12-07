import Vue from 'vue';
import router from './router';
import store from './store';
// progress bar
import NProgress from 'nprogress';
// progress bar style
import 'nprogress/nprogress.css';
import notification from 'ant-design-vue/es/notification';
import { ACCESS_TOKEN } from '@/store/mutation-types';
import { generateIndexRouter } from "@/utils/util";

// NProgress Configuration
NProgress.configure({ showSpinner: false });

// no redirect whitelist
const whiteList = ['/user/login', '/user/register', '/user/register-result','/user/alteration'];

router.beforeEach((to, from, next) => {
  // start progress bar
  NProgress.start();

  if (Vue.ls.get(ACCESS_TOKEN)) {
    /* has token */
    if (to.path === '/user/login') {
      next({ path: '/dashboard/workplace' });
      NProgress.done();
    } else {
      if (store.getters.permissionList.length === 0) {
        store.dispatch('GetPermissionList').then(res => {
              const menuData = res.result.menu;
              console.log(res.message);
              if (menuData === null || menuData === "" || menuData === undefined) {
                return;
              }
              let constRoutes = [];
              constRoutes = generateIndexRouter(menuData);
              // 添加主界面路由
              store.dispatch('UpdateAppRouter',  { constRoutes }).then(() => {
                // 根据roles权限生成可访问的路由表
                // 动态添加可访问路由表
                router.addRoutes(store.getters.addRouters);
                const redirect = decodeURIComponent(from.query.redirect || to.path);
                if (to.path === redirect) {
                  // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
                  next({ ...to, replace: true });
                } else {
                  // 跳转到目的路由
                  next({ path: redirect });
                }
              });
            })
          .catch(() => {
           /* notification.error({
              message: '系统提示',
              description: '请求用户信息失败，请重试！'
            })*/
            store.dispatch('Logout').then(() => {
              next({ path: '/user/login', query: { redirect: to.fullPath } });
            });
          });
      } else {
        next();
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next();
    } else {
      next({ path: '/user/login', query: { redirect: to.fullPath } });
      // if current page is login will not trigger afterEach hook, so manually handle it
      NProgress.done();
    }
  }
});

router.afterEach(() => {
  // finish progress bar
  NProgress.done();
});
