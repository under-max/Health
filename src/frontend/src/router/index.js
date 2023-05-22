import { createRouter, createWebHistory } from 'vue-router'
import Search from "@/views/Search.vue";
import Exercise from "@/views/Exercise.vue";
import Membership from "@/views/Membership.vue";
import Community from "@/views/Community.vue"
import Main from "@/views/Main.vue";
import Login from "@/views/Login.vue";
import Information from "@/views/Information.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'main',
      component: Main
    },
    {
      path: '/exercise',
      name: 'exercise',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: Exercise
    },
    {
      path: '/search',
      name: 'search',
      component : Search
    },
    {
      path: '/membership',
      name: 'membership',
      component: Membership
    },
    {
      path: '/community',
      name: 'community',
      component: Community
    },
    {
      path: '/login',
      name: 'login',
      component : Login
    },
    {
      path: '/info',
      name: 'info',
      component: Information
    }
  ]
})

export default router
