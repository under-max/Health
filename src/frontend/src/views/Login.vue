<template>
    <div class="login-container mt-4">
        <form class="login-form" style="color: white">
            <div class="form-group">
                <label for="email">이메일</label>
                <input id="email" type="email" v-model="email"/>
            </div>
            <div class="form-group">
                <label for="password">비밀번호</label>
                <input id="password" type="password" v-model="password"/>
            </div>

            <div class="btn" style="color: white">
                <label for="keep">로그인 정보저장 &nbsp;  </label>
                <input type="checkbox" id="keep" v-model="keepLogin" model-value=true/>
            </div>
            <button  class="btn mt-2" @click.prevent="login()">로그인</button>
            <div>
                <RouterLink style="color:white" :to="{ path: '/signup', replace: true }">
                    <button class="btn mt-2">
                        회원가입
                    </button>
                </RouterLink>
            </div>
            <div class="wrap">
                <div class="title" style="color:black">소셜로그인</div>
                <a class="kakao" :href="URI">
                    <!-- REST_API키 및 REDIRECT_URI는 본인걸로 수정하세요 -->

                    <div class="kakao_i"></div>
                    <div class="kakao_txt">카카오톡로그인 </div>
                </a>
            </div>
        </form>




    </div>
</template>


<script setup>
import {mapMutations} from "vuex";
import axios from "axios";
import {onMounted, ref} from "vue";
import {RouterLink, useRouter} from "vue-router";
import store from "@/stores/store";
import Cookies from 'vue-cookies';
import {showCustomAlert} from "@/main";

const email = ref("");
const password = ref("");
const {setToken} = mapMutations(["setToken"]);
const router = useRouter();
const keepLogin = ref(false);
const URI = ref(`https://kauth.kakao.com/oauth/authorize?client_id=${process.env.VUE_APP_REST_API_KEY}&redirect_uri=${process.env.VUE_APP_REDIRECT_URI}&response_type=code`)
const login = () => {
    axios.post("/api/auth/login", {
        email: email.value,
        password: password.value,
    }).then((response) => {
        const accessToken = response.data.accessToken;
        console.log(accessToken)
        if (accessToken) {
            Cookies.set('accessToken', accessToken, 60 * 60);
            store.commit('setToken', accessToken);
            // 1시간 동안 유효한 쿠키 설정 // 서버의 token 유효기간보다 30분 길게 설정
        }
        const refreshToken = response.data.refreshToken;
        console.log(refreshToken)
        if (keepLogin.value == true) { //
            Cookies.set('refreshToken', refreshToken, 60 * 60 * 24 * 31);
            // 31일 동안 유효한 쿠키 설정 // 서버의 token 유효기간보다 1일 길게 설정
        }

      sessionStorage.setItem("loginType", response.data.authority);


        router.replace("/");
    }).catch((error) => {
        if (error.response) {
            showCustomAlert(error.response.data.message)
        }
    });
}


</script>

<style scoped>
.login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
}

.login-form {
    width: 300px;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 5px;
}

.form-group {
    margin-bottom: 10px;
}

label {
    display: block;
    margin-bottom: 5px;
}

input {
    width: 100%;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 3px;
    font-size: 1rem;
}

.btn {
    width: 100%;
    padding: 8px;
    border: none;
    border-radius: 3px;
    font-size: 1rem;
    background-color: #1a73e8;
    color: #fff;
    cursor: pointer;
}

.btn:hover {
    background-color: #0d47a1;
}
.wrap {
    margin-top: 170px;
    width: 350px;
    padding: 40px 20px 20px 20px;
    background-color: #f5f6f7;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    border-radius: 30px;
    box-sizing: border-box;
}

.title {
    margin: 0 auto;
    width: 200px;
    height: 44px;
    text-align: center;
    font-size: 25px;
    background-repeat: no-repeat;
    background-position: 0 0;
    background-size: 240px auto;
    margin-bottom: 20px;
}

.kakao {
    margin-top: 15px;
    height: 60px;
    border: solid 1px #FEE500;
    background: #FEE500;
    color: #3c1d1e;
    font-size: 18px;
    box-sizing: border-box;
    border-radius: 5px;
    cursor: pointer;
    width: 100%; /* Updated width to match the container width */
    max-width: 450px; /* Added max-width to limit the button size */
    display: flex;
}

.kakao_i {
    width: 40px;
    height: 100%;
    background-size: 90%;
    background-position: 50%;
    margin: 0 20px;
}

.kakao_txt {
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 16px;
    padding-right: 60px;
}

a {
    text-decoration: none;
}
</style>