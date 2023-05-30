<template>

</template>

<script setup>
import axios from "axios";
import {onMounted, ref} from "vue";
import Cookies from "vue-cookies";
import store from "@/stores/store";
import {createRouter, createWebHistory} from "vue-router";
import Main from "@/views/Main.vue";
import {showCustomAlert} from "@/main";

const urlParams = new URLSearchParams(window.location.search);
const params = urlParams.get("code");
const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'main',
            component: Main
        },
    ],
});

onMounted(async () => {
    try {
        const response1 = await axios.get(`/api/app/login/kakao/${params}`);

        const accessToken = response1.data.accessToken;
        if (accessToken) {
            Cookies.set('accessToken', accessToken, 60 * 60);
            store.commit('setToken', accessToken);
            // 1시간 동안 유효한 쿠키 설정 // 서버의 token 유효기간보다 30분 길게 설정
        }
        const refreshToken = response1.data.refreshToken;
        if (refreshToken) { //
            Cookies.set('refreshToken', refreshToken, 60 * 60 * 24 * 31);
            // 31일 동안 유효한 쿠키 설정 // 서버의 token 유효기간보다 1일 길게 설정
        }
        const response2 = await axios.post(`/api/auth/user/auth`,
            {},
            {
                headers: {
                    Authorization: accessToken
                }
            })
        console.log(response2.data.nickName)
        if (response2.data.nickName) {
            await router.replace('/')
            await window.location.reload();
        } else {
            await router.replace('/addinfo')
            await window.location.reload();
        }
    } catch (error) {
        if (error.response) {
            showCustomAlert(error.response.data.message)
        }
    }
});
</script>

<style scoped>

</style>