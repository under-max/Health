import { createRouter, createWebHistory } from 'vue-router'
import Search from "@/views/Search.vue";
import Exercise from "@/views/Exercise.vue";
import Membership from "@/views/Membership.vue";
import Community from "@/views/Community.vue"
import Main from "@/views/Main.vue";
import Login from "@/views/Login.vue";
import Information from "@/views/Information.vue";
import UserDetail from "@/views/UserDetail.vue";
import SignUpView from "@/views/SignUpView.vue";
import TrainerDetail from "@/views/TrainerDetail.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'main',
      component: Main
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
    },
    {
      path: '/exercise',
      name: 'exercise',
      component: Exercise
    },
    {
      path: '/userDetail/:userId',
      name: 'userDetail',
      component: UserDetail,
      props : true
    },
    {
      path: '/trainerDetail/:trainerId',
      name: 'trainerDetail',
      component: TrainerDetail,
      props : true
    },
    {
      path: '/signup',
      name: 'signup',
      component: SignUpView
    }
  ]
})

export default router
