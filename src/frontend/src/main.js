
import {computed, createApp} from 'vue'
import App from './App.vue'
import router from './router'
import Cookies from "vue-cookies";
const app = createApp(App)
import ElementPlus from 'element-plus'
import store from "@/stores/store";
import axios from "axios";
import "bootstrap/dist/css//bootstrap-utilities.css"

app.use(ElementPlus)
app.use(router)
app.use(Cookies)
app.mount('#app')





const token = Cookies.get('accessToken');
if (token) {
    store.commit('setToken', token);
}
axios.interceptors.response.use(
    response => response,
    error => {
        if (error.response && error.response.data.message == "로그인이 필요합니다.") {
            // Unauthorized 오류 발생 시 로그인 페이지로 이동
            router.push({name: "login"})
        }

        if (error.response && error.response.data.message == "로그인 인증이 만료되었습니다.") {
            // 쿠키에서 refresh 토큰이 존재하면 서버로 토큰 전송
            const refreshToken = Cookies.get('refreshToken');
            if (refreshToken) {
                axios
                    .post("api/auth/login/refresh", {}, {
                        headers: {
                            RefreshToken : refreshToken,
                        },
                    })
                    .then((response) => {
                        const accessToken = response.data.accessToken;
                        if (accessToken) {
                            Cookies.set('accessToken', accessToken, 60*60);
                            store.commit('setToken', accessToken);
                        }
                        const refreshToken = response.data.refreshToken;
                        if(refreshToken){ //
                            Cookies.set('refreshToken', refreshToken, 60*60*24*30);
                        }
                        // router.replace({ name: "/" });
                        return Promise.resolve(response);
                    })
            } else {
            store.commit("setToken", "");
            Cookies.remove('accessToken'); // 쿠키에서 access token 값 삭제
            Cookies.remove('refreshToken'); // 쿠키에서 refresh token 값 삭제
            router.replace("/").then(()=>{window.location.reload();});
            }
        }
        return Promise.reject(error);
    }
)

export const showCustomAlert = (message) => {
    const customAlert = document.createElement('div');
    customAlert.classList.add('custom-alert');
    customAlert.textContent = message;

    document.body.appendChild(customAlert);

    setTimeout(() => {
        customAlert.remove();
    }, 1500);
}


