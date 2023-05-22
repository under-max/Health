import { createRouter, createWebHistory } from 'vue-router'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'main',
      component: ()=> import('../views/Main.vue')
    },    
    {
      path: '/excercise',
      name: 'excercise',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/Excercise.vue')
    },
    {
      path: '/search',
      name: 'search',
      component: ()=> import('../views/Search.vue')
    },
    {
      path: '/ticket',
      name: 'ticket',
      component: () => import('../views/Ticket.vue')
    },
    {
      path: '/community',
      name: 'community',
      component: () => import('../views/Community.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: ()=> import('../views/Login.vue')
    },
    {
      path: '/mypage',
      name: 'mypage',
      component: ()=> import('../views/MyPage.vue')
    }
  ]
})

export default router
