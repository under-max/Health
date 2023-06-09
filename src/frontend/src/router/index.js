import {createRouter, createWebHistory} from 'vue-router'
import Center from "@/views/Center.vue";
import CenterDetail from "../components/center/container/CenterDetail.vue";

import Exercise from "@/views/Exercise.vue";
import Membership from "@/views/Membership.vue";
import Community from "@/views/Community.vue"
import Main from "@/views/Main.vue";
import Login from "@/views/Login.vue";
import Information from "@/views/Information.vue";
import UserDetail from "@/views/UserDetail.vue";
import SignUpView from "@/views/SignUpView.vue";
import KakaoLogin from "@/views/KakaoLogin.vue";
import AddInfo from "@/views/AddInfo.vue";
import TrainerDetail from "@/views/TrainerDetail.vue";
import SuccessPage from "@/views/payment/SuccessPage.vue";
import CancelPage from "@/views/payment/CancelPage.vue";
import TrainerCalendar from "../views/TrainerCalendar.vue";
import CreateBoard from "@/views/board/CreateBoard.vue";
import GetBoard from "@/views/board/GetBoard.vue";
import ModifyBoard from "@/views/board/ModifyBoard.vue";

import CenterRegister from "@/views/AdminCenterRegister.vue"
import TrainerRegister from "@/views/AdminTrainerRegister.vue"

import ErrorPage from '../components/center/container/NotFoundPage.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'main',
            component: Main
        },
        {
            path: '/center',
            name: 'center',
            component: Center,
        },
        {
            path: '/center/:id',
            name: 'centerDetail',
            component: CenterDetail,
        },
        {
            path: '/center/centerRegister',
            name: 'centerRegister',
            component: CenterRegister
        },
        {
            path: '/center/trainerRegister',
            name: 'trainerRegister',
            component: TrainerRegister
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
            component: Login
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
            props: true
        },
        {
            path: '/trainerCalendar/:trainerId',
            name: 'trainerCalendar',
            component: TrainerCalendar,
            props: true
        },
        {
            path: '/trainerDetail/:trainerId',
            name: 'trainerDetail',
            component: TrainerDetail,
            props: true
        },
        {
            path: '/signup',
            name: 'signup',
            component: SignUpView
        },
        {
            path: '/kakaologin',
            name: 'kakaologin',
            component: KakaoLogin
        },
        {
            path: '/addinfo',
            name: 'addinfo',
            component: AddInfo
        },
        {
            path: '/payment/success',
            name: 'successPage',
            component: SuccessPage
        },
        {
            path: '/payment/cancel',
            name: 'cancelPage',
            component: CancelPage
        },
        {
            path: '/community/new',
            name: 'createBoard',
            component: CreateBoard
        },
        {
            path: '/community/board/:boardId',
            name: 'getBoard',
            component: GetBoard,
            props: true
        },
        {
            path: '/community/board/:boardId/modify',
            name: 'modifyBoard',
            component: ModifyBoard,
            props: true
        },
        {
            path: '/notFound',
            name: "notFound",
            component: ErrorPage
        },
        {
            path: "/:pathMatch(.*)*",
            redirect: "/notFound"
        }
    ]
})

export default router
