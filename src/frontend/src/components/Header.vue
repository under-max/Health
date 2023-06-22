<template>
<header class="navBars">
    <nav class="navBar">
      <div class="desktop">
        <ul class="no_dot">
          <li>
            <RouterLink to="/">
              <div class="logo">
                <img :src="logo" alt="LogoBar">
              </div>
            </RouterLink>
          </li>

          <li>
            <template v-if="isLoggedIn">
              <button>
                <RouterLink to="/exercise">운동관리</RouterLink>
              </button>
            </template>
          </li>

          <li>
            <button>
              <RouterLink to="/center">지점검색</RouterLink>
            </button>
          </li>

          <li>
            <button>
              <RouterLink to="/membership">이용권 안내</RouterLink>
            </button>
          </li>

          <li>
            <button>
              <RouterLink to="/community">커뮤니티</RouterLink>
            </button>
          </li>

          <li>
            <template v-if="!isLoggedIn">
              <button>
                <RouterLink :to="{ path: '/login', replace: true }">로그인</RouterLink>
              </button>
            </template>
            <template v-else>
              <button @click="logout()">
                  <RouterLink :to="{ path: '/', replace: true }">로그아웃</RouterLink>
              </button>
              <button>
                  <RouterLink :to="{ path: '/info', replace: true }">개인정보수정</RouterLink>
              </button>
            </template>
          </li>

        </ul>
      </div>
    </nav>   
  </header>
</template>

<script setup>
import logo from '@/assets/Logo.png';
import {RouterLink} from "vue-router";
import {computed} from "vue";
import store from "@/stores/store";
import Cookies from "vue-cookies";
import router from "@/router";
const token = computed(() => store.getters.getToken);
const isLoggedIn = computed(() => !!token.value);
const logout = function () {
  store.commit("setToken", ""); // 로그아웃 시 토큰 초기화
  Cookies.remove('accessToken'); // 쿠키에서 access token 값 삭제
  Cookies.remove('refreshToken'); // 쿠키에서 refresh token 값 삭제
  router.replace("/")
}

</script>

<style scoped>
.navBars{
  /* height: auto;
  width: 100%;
  margin-bottom: 0px; */

  height: auto;
  width: 100%;
  margin-bottom: 0;
  display: flex;
  justify-content: center;
  
}
.navBar {

  width: 100%;
  max-width: auto;
  padding: 0 20px;
  background-color: #112470;
}

.no_dot {
  list-style-type: none;
  display: flex;  
  justify-content: space-around;
  
  margin: 0px;
}

.no_dot a{
  text-decoration: none;
  color: white;   
  font-size: 21px;
}

.no_dot button {
  background-color: #112470;
  border: none;
  border-radius: 50px;
  padding: 10px 30px;
  transition: background-color 0.3s ease;
}

.no_dot button:hover{
  background-color: rgba(255, 255, 255, 0.3);
  padding: 10px 30px;
}

.no_dot li:not(:first-of-type){
  margin-top: 15px;
  margin-bottom: 15px;

}

.no_dot li:first-child {
  margin-top: 10px;
}


.logo img{
  padding-top: 1vh;
}

</style>

<style>
.navBar{
  background-color: #112470;
}
</style>